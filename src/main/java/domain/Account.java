package domain;

import domain.Enums.AccountType;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Account{
    protected String id;
    protected String iban;
    protected AccountType type;
    protected BigDecimal solde;
    protected Currency devise;
    protected LocalDate dateCreation;
    protected boolean isActive;
    protected Client client;

    public Account(String iban , AccountType type , BigDecimal solde , Currency devise , LocalDate dateCreation , boolean isactive , Client client){
        this.iban = iban;
        this.type = type;
        this.solde = solde;
        this.devise = devise;
        this.dateCreation = dateCreation;
        this.isActive = isActive;
        this.client = client;
    }

    public String getIban(){
        return iban;
    }

    public AccountType getAccountType(){
        return type;
    }

    public BigDecimal getSolde(){
        return solde;
    }

    public Currency getDevise(){
        return devise;
    }

    public LocalDate getDate(){
        return dateCreation;
    }

    public boolean getActive(){
        return isActive;
    }

    public Client getClient(){
        return client;
    }

    public void SetIban(String iban){
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

    public void setType(AccountType type){
        this.type = type;
    }
}
