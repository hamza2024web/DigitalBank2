package domain;

import domain.Enums.Currency;
import domain.Enums.FeeMode;
import domain.Enums.TransactionType;

import java.math.BigDecimal;

public class FeeRule {
    private Long id;
    private TransactionType operationType;
    private FeeMode mode;
    private BigDecimal value;
    private Currency devise;
    private boolean isActive;

    public FeeRule(Long id, TransactionType operationType, FeeMode mode, BigDecimal value, Currency devise, boolean isActive) {
        this.id = id;
        this.operationType = operationType;
        this.mode = mode;
        this.value = value;
        this.devise = devise;
        this.isActive = isActive;
    }

    public FeeRule() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getOperationType() {
        return operationType;
    }

    public void setOperationType(TransactionType operationType) {
        this.operationType = operationType;
    }

    public FeeMode getMode() {
        return mode;
    }

    public void setMode(FeeMode mode) {
        this.mode = mode;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Currency getDevise() {
        return devise;
    }

    public void setDevise(Currency devise) {
        this.devise = devise;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
