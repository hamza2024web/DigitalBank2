package mapper;

import domain.*;
import domain.Enums.AccountCloseStatus;
import domain.Enums.Currency;
import dto.AccountDTO;
import domain.Enums.AccountType;
import dto.CreateAccountDTO;

import java.math.BigDecimal;

public class AccountMapper {

    public static Account toAccount(CreateAccountDTO dto, Client client) {
        AccountType accountType = AccountType.valueOf(dto.getAccountType().toUpperCase());
        BigDecimal initialBalance = new BigDecimal(dto.getInitialBalance());
        Currency currency = Currency.valueOf(dto.getCurrency().toUpperCase());

        switch (accountType) {
            case COURANT:
                return new CurrentAccount(
                        initialBalance,
                        currency,
                        client,
                        BigDecimal.ZERO
                );

            case EPARGNE:
                return new SavingAccount(
                        initialBalance,
                        currency,
                        client,
                        new BigDecimal("0.03")
                );

            default:
                throw new IllegalArgumentException("Unsupported account type: " + accountType);
        }
    }

    public static CurrentAccount toCurrentAccountWithOverdraft(CreateAccountDTO dto, Client client, BigDecimal decouvertAutorise) {
        BigDecimal initialBalance = new BigDecimal(dto.getInitialBalance());
        Currency currency = Currency.valueOf(dto.getCurrency().toUpperCase());

        return new CurrentAccount(
                null,
                initialBalance,
                currency,
                null,
                decouvertAutorise
        );
    }

    public static SavingAccount toSavingAccountWithInterest(CreateAccountDTO dto, Client client, BigDecimal tauxInteret) {
        BigDecimal initialBalance = new BigDecimal(dto.getInitialBalance());
        Currency currency = Currency.valueOf(dto.getCurrency().toUpperCase());

        return new SavingAccount(
                null,
                initialBalance,
                currency,
                null,
                tauxInteret
        );
    }

    public static AccountDTO toAccountDTO(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountDTO(
                account.getId(),
                account.getIban(),
                account.getAccountType(),
                account.getSolde(),
                account.getDevise(),
                account.getDate(),
                account.getActive(),
                account.getClient(),
                account.getCloseStatus()
        );
    }

}
