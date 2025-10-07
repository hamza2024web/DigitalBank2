package domain;

import domain.Enums.AccountCloseStatus;
import domain.Enums.AccountType;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingAccount extends Account {
    private BigDecimal tauxInteret;

    public SavingAccount(String id, String iban, AccountType type, BigDecimal solde, Currency devise, LocalDate dateCreation, boolean isActive, Client client, AccountCloseStatus closeStatus, BigDecimal tauxInteret) {
        super(id, iban, type, solde, devise, dateCreation, isActive, client, closeStatus);
        this.tauxInteret = tauxInteret;
    }

    public SavingAccount(String iban, BigDecimal solde, Currency devise, LocalDate dateCreation, BigDecimal tauxInteret) {
        super(null, iban, AccountType.EPARGNE, solde, devise, dateCreation, true, null, AccountCloseStatus.NONE);
        this.tauxInteret = tauxInteret;
    }

    public SavingAccount(String id, String iban, BigDecimal solde, Currency devise, LocalDate dateCreation, BigDecimal tauxInteret, AccountCloseStatus closeStatus) {
        super(id, iban, AccountType.EPARGNE, solde, devise, dateCreation, true, null, closeStatus);
        this.tauxInteret = tauxInteret;
    }

    public SavingAccount(String id, String iban, BigDecimal solde, Currency devise, LocalDate dateCreation, boolean isActive, Client client, BigDecimal tauxInteret, AccountCloseStatus closeStatus) {
        super(id, iban, AccountType.EPARGNE, solde, devise, dateCreation, isActive, client, closeStatus);
        this.tauxInteret = tauxInteret;
    }

    public SavingAccount(BigDecimal solde, Currency devise, Client client, BigDecimal tauxInteret) {
        super(null, null, AccountType.EPARGNE, solde, devise, null, true, client, AccountCloseStatus.NONE);
        this.tauxInteret = tauxInteret;
    }

    public BigDecimal getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(BigDecimal tauxInteret) {
        this.tauxInteret = tauxInteret;
    }
}
