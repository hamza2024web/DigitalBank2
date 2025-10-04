package service;

import config.FeeConfig;
import domain.Client;
import domain.CreditRequest;
import domain.Enums.CreditStatus;
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
                creditRequestDomain.setMontant(montantTotal);
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

}
