package repository.Interface;

import domain.Transaction;

public interface TransactionRepository {
    void save(Transaction transaction);
}
