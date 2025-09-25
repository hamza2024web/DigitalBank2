package domain;

import domain.Enums.Currency;
import domain.Enums.TransactionStatus;
import domain.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private BigDecimal montant;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime dateTransaction;
    private Currency devise;
    private String description;
    private Account accountSource;
    private Account accountDestination;

    public Transaction(Long id , BigDecimal montant , TransactionType type , LocalDateTime dateTransaction , Currency devise , String description , Account accountSource , Account accountDestination) {
        this.id = id;
        this.montant = montant;
        this.type = type;
        this.status = status;
        this.dateTransaction = dateTransaction;
        this.devise = devise;
        this.description = description;
        this.accountSource = accountSource;
        this.accountDestination = accountDestination;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public Currency getDevise() {
        return devise;
    }

    public String getDescription() {
        return description;
    }

    public Account getAccountSource() {
        return accountSource;
    }

    public Account getAccountDestination() {
        return accountDestination;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public void setDevise(Currency devise) {
        this.devise = devise;
    }

    public void setAccountSource(Account accountSource) {
        this.accountSource = accountSource;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAccountDestination(Account accountDestination) {
        this.accountDestination = accountDestination;
    }
}
