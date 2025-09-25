package domain;

import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperationHistory {
    private Long id;
    private LocalDateTime dateOperation;
    private String operationType;
    private String sourceAccountId;
    private String destinationAccountId;
    private Long userId;
    private String description;
    private String status;
    private BigDecimal amount;
    private Currency currency;
    private String reference;

    public OperationHistory(Long id, LocalDateTime dateOperation, String operationType, String sourceAccountId, String destinationAccountId, Long userId, String description, String status, BigDecimal amount, Currency currency, String reference) {
        this.id = id;
        this.dateOperation = dateOperation;
        this.operationType = operationType;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.userId = userId;
        this.description = description;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.reference = reference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(LocalDateTime dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(String sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public String getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(String destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
