package dto;

import domain.Client;
import domain.Enums.AccountCloseStatus;
import domain.Enums.AccountType;
import domain.Enums.Currency;
import domain.OperationHistory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AccountDTO {
    private String id;
    private String iban;
    private AccountType type;
    private BigDecimal solde;
    private Currency devise;
    private LocalDate dateCreation;
    private boolean isActive;
    private Client client;
    private AccountCloseStatus closeStatus;
    private List<OperationHistory> operations;

    public AccountDTO(String id, String iban, AccountType type, BigDecimal solde, Currency devise,
                      LocalDate dateCreation, boolean isActive, Client client) {
        this.id = id;
        this.iban = iban;
        this.type = type;
        this.solde = solde;
        this.devise = devise;
        this.dateCreation = dateCreation;
        this.isActive = isActive;
        this.client = client;
        this.closeStatus = AccountCloseStatus.NONE;
    }

    public AccountDTO(String id, String iban, AccountType type, BigDecimal solde, Currency devise,
                      LocalDate dateCreation, boolean isActive, Client client, AccountCloseStatus closeStatus) {
        this(id, iban, type, solde, devise, dateCreation, isActive, client);
        this.closeStatus = closeStatus;
    }

    public String getId() { return id; }
    public String getIban() { return iban; }
    public AccountType getType() { return type; }
    public BigDecimal getSolde() { return solde; }
    public Currency getDevise() { return devise; }
    public LocalDate getDateCreation() { return dateCreation; }
    public boolean isActive() { return isActive; }
    public Client getClient() { return client; }
    public AccountCloseStatus getCloseStatus() { return closeStatus; }

    public List<OperationHistory> getOperations() { // <-- getter pour la liste
        return operations;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }
    public void setType(AccountType type) {
        this.type = type;
    }
    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }
    public void setDevise(Currency devise) {
        this.devise = devise;
    }
    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public void setCloseStatus(AccountCloseStatus closeStatus) {
        this.closeStatus = closeStatus;
    }
    public void setOperations(List<OperationHistory> operations) {
        this.operations = operations;
    }
}
