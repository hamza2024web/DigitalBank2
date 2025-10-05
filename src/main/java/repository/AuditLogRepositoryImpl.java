package repository;

import domain.AuditLog;
import domain.Enums.Role;
import domain.User;
import dto.AdminCreditLogDTO;
import repository.Interface.AuditLogRepository;
import util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<AuditLog> getTellerManagerLog(User user) {
        String sql = "SELECT * FROM audit_log WHERE user_id = ? ORDER BY timestamp DESC";
        List<AuditLog> logs = new ArrayList<>();

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");

                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

                String action = rs.getString("action");
                String details = rs.getString("details");

                Long userId = rs.getLong("user_id");

                String roleStr = rs.getString("role");
                Role userRole = Role.valueOf(roleStr);

                boolean success = rs.getBoolean("success");

                String errorMessage = rs.getString("error_message");

                AuditLog auditLog = new AuditLog(
                        timestamp,
                        action,
                        details,
                        userId,
                        userRole,
                        success,
                        errorMessage
                );

                logs.add(auditLog);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching audit logs: " + e.getMessage());
            e.printStackTrace();
        }

        return logs;
    }
}
