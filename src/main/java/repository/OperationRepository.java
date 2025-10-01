package repository;

import domain.OperationHistory;

public interface OperationRepository {
    void save(OperationHistory operationHistory);
}
