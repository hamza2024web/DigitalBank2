package repository;

import domain.Transaction;

public interface TransactionRepository {
    void save(Transaction transaction);
}
