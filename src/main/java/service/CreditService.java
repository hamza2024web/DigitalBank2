package service;

import config.FeeConfig;
import domain.Client;
import domain.CreditAccount;
import domain.CreditRequest;
import domain.Enums.CreditStatus;
import domain.Enums.TransactionType;
import domain.User;
import dto.CreditReqDTO;
import mapper.CreditMapper;
import repository.CreditAccountRepositoryImpl;
import repository.CreditRequestRepositoryImpl;
import repository.CreditScheduleRepositoryImpl;
import repository.TransactionRepositoryImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

public class CreditService {
    private final CreditRequestRepositoryImpl creditRequestRepository;
    private final CreditAccountRepositoryImpl creditAccountRepository;
    private final CreditScheduleRepositoryImpl creditScheduleRepository;
    private final TransactionRepositoryImpl transactionRepository;
    private final FeeConfig feeConfig;

    public CreditService(CreditRequestRepositoryImpl creditRequestRepository, CreditAccountRepositoryImpl creditAccountRepository, CreditScheduleRepositoryImpl creditScheduleRepository, TransactionRepositoryImpl transactionRepository, FeeConfig feeConfig){
        this.creditRequestRepository = creditRequestRepository;
        this.creditAccountRepository = creditAccountRepository;
        this.creditScheduleRepository = creditScheduleRepository;
        this.transactionRepository = transactionRepository;
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

            CreditAccount creditAccount = new CreditAccount(
                    creditAccountId,
                    iban,
                    request.getClient(),
                    request.getMontant(),
                    request.getDureeMois(),
                    request.getTauxAnnuel()
            );
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
