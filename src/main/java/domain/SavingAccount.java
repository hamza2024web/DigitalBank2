package domain;

import domain.Enums.AccountType;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingAccount extends Account{
    private BigDecimal tauxInteret;

    public SavingAccount(String id, String iban, AccountType type, BigDecimal solde, Currency devise, LocalDate dateCreation, boolean isActive, Client client, BigDecimal tauxInteret){
        super(id,iban,type,solde,devise,dateCreation,isActive,client);
        this.tauxInteret = tauxInteret;
    }

    public BigDecimal getTauxInteret(){
        return tauxInteret;
    }

    public void setTauxInteret(BigDecimal tauxInteret){
        this.tauxInteret = tauxInteret;
    }
}
