package domain;

import domain.Enums.AccountType;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrentAccount extends Account{
    private BigDecimal decouvertAutorise;

    public CurrentAccount(String id , String iban , AccountType type , BigDecimal solde , Currency devise , LocalDate dateCreation , boolean isActive , Client client , BigDecimal decouvertAutorise){
        super(id , iban , type , solde , devise , dateCreation , isActive , client);
        this.decouvertAutorise = decouvertAutorise;
    }

    public CurrentAccount(String iban, BigDecimal solde, Currency devise, LocalDate dateCreation, BigDecimal decouvertAutorise) {
        super(null, iban, AccountType.COURANT, solde, devise, dateCreation, true, null);
        this.decouvertAutorise = decouvertAutorise;
    }
    public BigDecimal getDecouvertAutorise(){
        return decouvertAutorise;
    }

    public void setDecouvertAutorise(BigDecimal decouvertAutorise){
        this.decouvertAutorise = decouvertAutorise;
    }
}
