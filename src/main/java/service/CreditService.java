package service;

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

}
