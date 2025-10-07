package domain;

import domain.Enums.AccountCloseStatus;
import domain.Enums.AccountType;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrentAccount extends Account {
    private BigDecimal decouvertAutorise;

    public CurrentAccount(String id, String iban, AccountType type, BigDecimal solde, Currency devise, LocalDate dateCreation, boolean isActive, Client client, AccountCloseStatus closeStatus, BigDecimal decouvertAutorise) {
        super(id, iban, type, solde, devise, dateCreation, isActive, client, closeStatus);
        this.decouvertAutorise = decouvertAutorise;
    }

    public CurrentAccount(String iban, BigDecimal solde, Currency devise, LocalDate dateCreation, BigDecimal decouvertAutorise) {
        super(null, iban, AccountType.COURANT, solde, devise, dateCreation, true, null, AccountCloseStatus.NONE);
        this.decouvertAutorise = decouvertAutorise;
    }

    public CurrentAccount(String id, String iban, BigDecimal solde, Currency devise, LocalDate dateCreation, BigDecimal decouvertAutorise, AccountCloseStatus closeStatus) {
        super(id, iban, AccountType.COURANT, solde, devise, dateCreation, true, null, closeStatus);
        this.decouvertAutorise = decouvertAutorise;
    }

    public CurrentAccount(BigDecimal solde, Currency devise, Client client, BigDecimal decouvertAutorise) {
        super(null, null, AccountType.COURANT, solde, devise, null, true, client, AccountCloseStatus.NONE);
        this.decouvertAutorise = decouvertAutorise;
    }

    public CurrentAccount(String id, String iban, BigDecimal solde, Currency devise, LocalDate dateCreation, boolean isActive, Client client, BigDecimal decouvertAutorise, AccountCloseStatus closeStatus) {
        super(id, iban, AccountType.COURANT, solde, devise, dateCreation, isActive, client, closeStatus);
        this.decouvertAutorise = decouvertAutorise;
    }

    public BigDecimal getDecouvertAutorise() {
        return decouvertAutorise;
    }

    public void setDecouvertAutorise(BigDecimal decouvertAutorise) {
        this.decouvertAutorise = decouvertAutorise;
    }
}
