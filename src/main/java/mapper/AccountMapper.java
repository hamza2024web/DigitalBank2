package mapper;

import domain.*;
import domain.Enums.Currency;
import dto.AccountDTO;
import domain.Enums.AccountType;
import dto.CreateAccountDTO;

import java.math.BigDecimal;

public class AccountMapper {
    public static Account toAccount(CreateAccountDTO dto, Client client) {
        AccountType accountType = AccountType.valueOf(dto.getAccountType().toUpperCase());

        switch (accountType) {
            case COURANT:
                return new CurrentAccount(
                        null,
                        null,
                        AccountType.COURANT,
                        new BigDecimal(dto.getInitialBalance()),
                        Currency.valueOf(dto.getCurrency().toUpperCase()),
                        null,
                        true,
                        client,
                        BigDecimal.ZERO
                );

            case EPARGNE:
                return new SavingAccount(
                        null,
                        null,
                        AccountType.EPARGNE,
                        new BigDecimal(dto.getInitialBalance()),
                        Currency.valueOf(dto.getCurrency().toUpperCase()),
                        null,
                        true,
                        client,
                        new BigDecimal("0.03")
                );

            case CREDIT:
                return new CreditAccount(
                        null,
                        null,
                        client,
                        new BigDecimal(dto.getInitialBalance()),
                        36,
                        5.0,
                        null
                );

            default:
                throw new IllegalArgumentException("Unsupported account type: " + accountType);
        }
    }

    public static AccountDTO toAccountDTO(Account account){
        return new AccountDTO(
                account.getId(),
                account.getIban(),
                account.getAccountType(),
                account.getSolde(),
                account.getDevise(),
                account.getDate(),
                account.getActive(),
                account.getClient()
        );
    }
}