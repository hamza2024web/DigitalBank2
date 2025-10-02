package repository.Interface;


import domain.CreditRequest;
import domain.Enums.CreditStatus;

import java.time.LocalDate;
import java.util.List;

public interface CreditRequestRepository {
    boolean save(CreditRequest creditRequest);
    CreditRequest findById(String requestId);
    List<CreditRequest> findByStatus(CreditStatus status);
    boolean update(CreditRequest request);
    List<CreditRequest> findByClientId(String clientId);
    List<CreditRequest> findByDateRange(LocalDate startDate, LocalDate endDate);
    List<CreditRequest> findByRequestedBy(String tellerEmail);
    boolean delete(String requestId);
    int countByStatus(CreditStatus status);
    List<CreditRequest> findAll();
    List<CreditRequest> findOldestPendingRequests(int limit);
}
