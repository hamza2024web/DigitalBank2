package repository.Interface;

import domain.AuditLog;
import domain.User;
import dto.AdminCreditLogDTO;

import java.util.List;

public interface AuditLogRepository {
    void save(AuditLog auditLog);
    List<AuditLog> getTellerManagerLog(User teller);
}
