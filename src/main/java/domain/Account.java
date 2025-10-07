package domain;

import domain.Enums.AccountCloseStatus;
import domain.Enums.AccountType;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Account {
    protected String id;
    protected String iban;
    protected AccountType type;
    protected BigDecimal solde;
    protected Currency devise;
    protected LocalDate dateCreation;
    protected boolean isActive;
    protected AccountCloseStatus closeStatus;
    protected Client client;

    public Account(String id, String iban, AccountType type, BigDecimal solde, Currency devise, LocalDate dateCreation, boolean isActive, Client client, AccountCloseStatus closeStatus) {
        this.id = id;
        this.iban = iban;
        this.type = type;
        this.solde = solde;
        this.devise = devise;
        this.dateCreation = dateCreation;
        this.isActive = isActive;
        this.client = client;
        this.closeStatus = closeStatus;
    }

    public String getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public AccountType getAccountType() {
        return type;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public Currency getDevise() {
        return devise;
    }

    public LocalDate getDate() {
        return dateCreation;
    }

    public boolean getActive() {
        return isActive;
    }

    public Client getClient() {
        return client;
    }

    public AccountCloseStatus getCloseStatus() {
        return closeStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIban(String iban) {
        this.iban = iban;
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

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setCloseStatus(AccountCloseStatus closeStatus) {
        this.closeStatus = closeStatus;
    }
}