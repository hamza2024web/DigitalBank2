package service;

import config.FeeConfig;
import domain.*;
import domain.Enums.AccountCloseStatus;
import domain.Enums.CreditStatus;
import domain.Enums.TransactionType;
import dto.CreditReqDTO;
import dto.ManagerCreditApproveDTO;
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
            BigDecimal applicationFee = feeConfig.calculateFee(TransactionType.CREDIT_APPLICATION, request.getMontant(), request.getCurrency());

            BigDecimal montantTotal = request.getMontant().add(applicationFee);
            request.setStatus(CreditStatus.ACTIVE);
            creditRequestRepository.update(request);

            String creditAccountId = generateAccountId();
            String iban = generateCreditIban();


            CreditAccount newcreditAccount = new CreditAccount(
                    creditAccountId,
                    iban,
                    montantTotal,
                    request.getCurrency(),
                    LocalDate.now(),
                    true,
                    request.getClient(),
                    AccountCloseStatus.NONE
            );

            Account account = accountRepository.saveCreditAccount(newcreditAccount);

            CreditAccount creditAccount = new CreditAccount(
                    creditAccountId,
                    iban,
                    montantTotal,
                    request.getCurrency(),
                    LocalDate.now(),
                    true,
                    request.getClient(),
                    AccountCloseStatus.NONE,
                    montantTotal,
                    request.getDureeMois(),
                    request.getTauxAnnuel(),
                    CreditStatus.ACTIVE,
                    montantTotal,
                    request.getRequestDate(),
                    LocalDate.now().plusMonths(1),
                    account
            );

            boolean saved = creditAccountRepository.save(creditAccount);

            if (!saved) {
                throw new RuntimeException("Error during creation of credit account .");
            }

            List<CreditSchedule> schedule = calculateRepaymentSchedule(creditAccount, montantTotal, request.getDureeMois(), request.getTauxAnnuel());

            for (CreditSchedule scheduleItem : schedule) {
                scheduleItem.setCreditAccount(creditAccount);
                creditScheduleRepository.save(scheduleItem);
            }

            return creditAccount;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean rejectCreditRequest(ManagerCreditApproveDTO managerCreditApproveDto){
        try {
            CreditRequest request = creditRequestRepository.findById(managerCreditApproveDto.getCreditId());
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

    public List<CreditSchedule> calculateRepaymentSchedule(CreditAccount creditAccount,BigDecimal montant , int dureeMois , BigDecimal tauxAnnuel){
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
                    null,
                    creditAccount,
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

    private String generateCreditIban() {
        return "MA" + System.currentTimeMillis() + "CREDIT";
    }

    private String generateAccountId() {
        return "ACC-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
