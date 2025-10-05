package service;

import domain.AuditLog;
import domain.User;
import dto.AdminCreditLogDTO;
import repository.AuditLogRepositoryImpl;
import repository.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class AuditLogService {
    private final AuditLogRepositoryImpl auditLogRepository;
    private final UserRepositoryImpl userRepository;
    public AuditLogService(AuditLogRepositoryImpl auditLogRepository, UserRepositoryImpl userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public List<AuditLog> getTellerManagerLog(AdminCreditLogDTO adminLogDTO) {
        User teller = userRepository.findByEmail(adminLogDTO.getEmail());

        if (teller == null) {
            System.err.println("User not found with email: " + adminLogDTO.getEmail());
            return new ArrayList<>();
        }

        List<AuditLog> logs = auditLogRepository.getTellerManagerLog(teller);
        return logs;
    }
}
