package repository;

import domain.AuditLog;
import repository.Interface.AuditLogRepository;
import util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditLogRepositoryImpl implements AuditLogRepository {
    @Override
    public void save(AuditLog auditLog) {
        String sql = "INSERT INTO audit_log (timestamp,user_id,user_role,action,details,success,error_message) VALUES (?,?,?::role_enum,?,?,?,?) ";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(auditLog.getTimestamp()));
            stmt.setLong(2,auditLog.getUserId());
            stmt.setString(3,auditLog.getUserRole().toString());
            stmt.setString(4,auditLog.getAction());
            stmt.setString(5,auditLog.getDetails());
            stmt.setBoolean(6,auditLog.isSuccess());
            stmt.setString(7,auditLog.getErrorMessage());

            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
