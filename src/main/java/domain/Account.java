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
}
