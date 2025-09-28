package repository;

import domain.AuditLog;

public class AuditLogRepositoryImpl implements AuditLogRepository{
    @Override
    public void save(AuditLog auditLog) {
        String sql = "INSERT INTO audit_log (timestamp,user_id,user_role,action,details,success,error_message) VALUES (?,?,?,?,?,?,?) ";
    }
}
