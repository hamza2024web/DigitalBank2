package repository;

import domain.AuditLog;

public interface AuditLogRepository {
    void save(AuditLog auditLog);
}
