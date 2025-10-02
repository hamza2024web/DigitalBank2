package repository.Interface;

import domain.AuditLog;

public interface AuditLogRepository {
    void save(AuditLog auditLog);
}
