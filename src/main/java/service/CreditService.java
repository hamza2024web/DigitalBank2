package service;

import config.FeeConfig;
import domain.*;
import domain.Enums.CreditStatus;
import domain.Enums.TransactionType;
import dto.CreditReqDTO;
import mapper.CreditMapper;
import repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreditService {
    private final CreditRequestRepositoryImpl creditRequestRepository;
    private final CreditAccountRepositoryImpl creditAccountRepository;
    private final CreditScheduleRepositoryImpl creditScheduleRepository;
    private final TransactionRepositoryImpl transactionRepository;
    private final AccountRepositoryImpl accountRepository;
    private final FeeConfig feeConfig;

    public CreditService(CreditRequestRepositoryImpl creditRequestRepository, CreditAccountRepositoryImpl creditAccountRepository, CreditScheduleRepositoryImpl creditScheduleRepository, TransactionRepositoryImpl transactionRepository, AccountRepositoryImpl accountRepository, FeeConfig feeConfig){
        this.creditRequestRepository = creditRequestRepository;
        this.creditAccountRepository = creditAccountRepository;
        this.creditScheduleRepository = creditScheduleRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.feeConfig = feeConfig;
    }

    public boolean createCreditRequest(CreditReqDTO creditReqDTO) {
        try {
            CreditRequest creditRequestDomain = CreditMapper.toCreditRequest(creditReqDTO);

            BigDecimal montant =  creditRequestDomain.getMontant();
            BigDecimal monthlyIncome = creditRequestDomain.getMonthlyIncome();
            int dureeMois = creditRequestDomain.getDureeMois();

            BigDecimal plafond = monthlyIncome.multiply(new BigDecimal("0.40"));
            BigDecimal frais = montant.multiply(new BigDecimal("0.05"));
            BigDecimal montantTotal = montant.add(frais);

            BigDecimal mensualite = montantTotal.divide(new BigDecimal(dureeMois), RoundingMode.HALF_UP);

            if (mensualite.compareTo(plafond) <= 0){
                creditRequestDomain.setStatus(CreditStatus.PENDING);
            } else {
                creditRequestDomain.setStatus(CreditStatus.REJECTED);
            }

            return creditRequestRepository.save(creditRequestDomain);

        } catch (Exception e) {
            System.err.println("Failed to create credit request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<CreditReqDTO> creditPending(User loggedInUser){
        List<CreditRequest> creditRequests = creditRequestRepository.findByStatus(CreditStatus.PENDING);

        return creditRequests.stream().map(CreditMapper::toCreditReqDTO).toList();
    }

    public CreditRequest findCreditRequestById(String requestId) {
        return creditRequestRepository.findById(requestId);
    }

    public CreditAccount approveCreditRequest(CreditRequest request){
        try {
            BigDecimal applicationFee = feeConfig.calculateFee(TransactionType.CREDIT_APPLICATION,request.getMontant(),request.getCurrency());

            request.setStatus(CreditStatus.ACTIVE);
            creditRequestRepository.update(request);

            String creditAccountId = UUID.randomUUID().toString();
            String iban = generateCreditIban();

            CreditAccount newcreditAccount = new CreditAccount(
                    creditAccountId,
                    iban,
                    request.getMontant(),
                    request.getCurrency(),
                    LocalDate.now(),
                    true,
                    request.getClient(),
                    "NONE"
            );

            accountRepository.saveCreditAccount(newcreditAccount);

            boolean saved = creditAccountRepository.save(creditAccount);

            if (!saved){
                throw new RuntimeException("Error during creation of credit account .");
            }

            List<CreditSchedule> schedule = calculateRepaymentSchedule(request.getMontant(),request.getDureeMois(),request.getTauxAnnuel());

            for (CreditSchedule scheduleItem : schedule) {
                scheduleItem.setCreditAccount(creditAccount);
                creditScheduleRepository.save(scheduleItem);
            }

            if (applicationFee.compareTo(BigDecimal.ZERO) > 0){
                deductApplicationFee(request.getClient().getAccounts().get(0),applicationFee);
            }

            transferCreditToClientAccount(creditAccount);

            return creditAccount;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean rejectCreditRequest(String requestId, String reason){
        try {
            CreditRequest request = creditRequestRepository.findById(requestId);
            if (request == null){
                return false;
            }

            request.setStatus(CreditStatus.REJECTED);
            return creditRequestRepository.update(request);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<CreditSchedule> calculateRepaymentSchedule(BigDecimal montant , int dureeMois , BigDecimal tauxAnnuel){
        List<CreditSchedule> schedule = new ArrayList<>();

        BigDecimal tauxMensuel = tauxAnnuel.divide(new BigDecimal("1200"),10,RoundingMode.HALF_UP);

        BigDecimal mensualite = calculateMonthlyPayment(montant,dureeMois,tauxMensuel);

        BigDecimal soldeRestant = montant;
        LocalDate currentDate = LocalDate.now().plusMonths(1);

        for (int i = 1; i<= dureeMois; i++){
            BigDecimal interets = soldeRestant.multiply(tauxMensuel).setScale(2,RoundingMode.HALF_UP);

            BigDecimal principal = mensualite.subtract(interets).setScale(2,RoundingMode.HALF_UP);

            if (i == dureeMois){
                principal = soldeRestant;
                mensualite = principal.add(interets);
            }

            soldeRestant = soldeRestant.subtract(principal).setScale(2,RoundingMode.HALF_UP);

            CreditSchedule scheduleItem = new CreditSchedule(
                    null, // L'ID sera généré lors de la sauvegarde
                    null, // Le creditAccount sera assigné plus tard
                    currentDate,
                    principal,
                    interets,
                    mensualite,
                    soldeRestant
            );

            schedule.add(scheduleItem);
            currentDate = currentDate.plusMonths(1);
        }
        return schedule;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, int months, BigDecimal monthlyRate) {
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(months), 2, RoundingMode.HALF_UP);
        }

        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal power = onePlusRate.pow(months);

        BigDecimal numerator = monthlyRate.multiply(power);

        BigDecimal denominator = power.subtract(BigDecimal.ONE);

        return principal.multiply(numerator.divide(denominator, 10, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void transferCreditToClientAccount(CreditAccount creditAccount) {
        Account relatedAccount = creditAccount.getRelatedAccount();

        // Calculer les frais de déblocage
        BigDecimal disbursementFee = feeConfig.calculateFee(
                TransactionType.CREDIT_DISBURSEMENT,
                creditAccount.getMontantDemande(),
                relatedAccount.getDevise()
        );

        // Montant net à créditer (montant demandé - frais de déblocage)
        BigDecimal netAmount = creditAccount.getMontantDemande().subtract(disbursementFee);

        // Créditer le compte du client
        relatedAccount.setSolde(relatedAccount.getSolde().add(netAmount));

        // Créer la transaction de déblocage
        Transaction disbursementTransaction = new Transaction(
                netAmount,
                TransactionType.CREDIT_DISBURSEMENT,
                "SUCCESS",
                LocalDate.now(),
                "MAD",
                String.format("Déblocage du crédit (Montant: %s MAD, Frais: %s MAD)",
                        creditAccount.getMontantDemande(), disbursementFee)
                UUID.randomUUID().toString(),
                creditAccount,
                relatedAccount
        );
        transactionRepository.save(disbursementTransaction);

        // Enregistrer les frais de déblocage (revenu banque)
        if (disbursementFee.compareTo(BigDecimal.ZERO) > 0) {
            Transaction feeTransaction = new Transaction(
                    UUID.randomUUID().toString(),
                    relatedAccount,
                    null, // Compte de la banque
                    disbursementFee,
                    LocalDate.now(),
                    TransactionType.FEE_DEDUCTION,
                    "Frais de déblocage crédit (revenu banque)"
            );
            transactionRepository.save(feeTransaction);
        }

        // Activer le compte crédit
        creditAccount.setStatut(CreditStatus.ACTIVE);
        creditAccountRepository.update(creditAccount);
    }

    private String generateCreditIban() {
        return "MA" + System.currentTimeMillis() + "CREDIT";
    }
}
