package dto;

import domain.Client;
import domain.Enums.AccountType;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountDTO {
    private String id;
    private String iban;
    private AccountType type;
    private BigDecimal solde;
    private Currency devise;
    private LocalDate dateCreation;
    private boolean isActive;
    private Client client;
    private BigDecimal decouvertAutorise;
    private BigDecimal tauxInteret;

    public AccountDTO(String id, AccountType type, String iban, BigDecimal solde, Currency devise, LocalDate dateCreation, boolean isActive, Client client , BigDecimal decouvertAutorise , BigDecimal tauxInteret) {
        this.id = id;
        this.type = type;
        this.iban = iban;
        this.solde = solde;
        this.devise = devise;
        this.dateCreation = dateCreation;
        this.isActive = isActive;
        this.client = client;
        this.decouvertAutorise = decouvertAutorise;
        this.tauxInteret = tauxInteret;
    }

    public AccountDTO(String id, String iban, AccountType accountType, BigDecimal solde, Currency devise, LocalDate date, boolean active, Client client) {
        this.id = id;
        this.iban = iban;
        this.type = accountType;
        this.solde = solde;
        this.devise = devise;
        this.dateCreation = date;
        this.isActive = active;
        this.client = client;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public Currency getDevise() {
        return devise;
    }

    public void setDevise(Currency devise) {
        this.devise = devise;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BigDecimal getDecouvertAutorise(){
        return decouvertAutorise;
    }

    public BigDecimal getTauxInteret(){
        return tauxInteret;
    }
}
