package repository;

import domain.Account;
import domain.OperationHistory;

import java.util.List;

public interface OperationRepository {
    void save(OperationHistory operationHistory);
    List<OperationHistory> findByAccountId(Account account);
}
