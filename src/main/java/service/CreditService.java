package service;

import domain.CreditRequest;
import dto.CreditReqDTO;
import mapper.CreditMapper;
import repository.CreditAccountRepositoryImpl;
import repository.CreditRequestRepositoryImpl;
import repository.Interface.CreditScheduleRepositoryImpl;
import repository.TransactionRepositoryImpl;

public class CreditService {
    private final CreditRequestRepositoryImpl creditRequestRepository;
    private final CreditAccountRepositoryImpl creditAccountRepository;
    private final CreditScheduleRepositoryImpl creditScheduleRepository;
    private final TransactionRepositoryImpl transactionRepository;

    public CreditService(CreditRequestRepositoryImpl creditRequestRepository, CreditAccountRepositoryImpl creditAccountRepository, CreditScheduleRepositoryImpl creditScheduleRepository, TransactionRepositoryImpl transactionRepository){
        this.creditRequestRepository = creditRequestRepository;
        this.creditAccountRepository = creditAccountRepository;
        this.creditScheduleRepository = creditScheduleRepository;
        this.transactionRepository = transactionRepository;
    }

    public boolean createCreditRequest(CreditReqDTO creditReqDTO) {
        try {
            CreditRequest creditRequestDomain = CreditMapper.toCreditRequest(creditReqDTO);

            return creditRequestRepository.save(creditRequestDomain);

        } catch (Exception e) {
            System.err.println("Failed to create credit request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
