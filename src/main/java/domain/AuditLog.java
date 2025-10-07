package domain;

import domain.Enums.Role;

import java.time.LocalDateTime;

public class AuditLog {
    private Long id;
    private LocalDateTime timestamp;
    private String action;
    private String details;
    private Long userId;
    private Role userRole;
    private boolean success;
    private String errorMessage;

    public AuditLog(LocalDateTime timestamp, String action, String details, Long userId, Role userRole, boolean success, String errorMessage) {
        this.timestamp = timestamp;
        this.action = action;
        this.details = details;
        this.userId = userId;
        this.userRole = userRole;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
