package mapper;

import domain.*;
import domain.Enums.Currency;
import dto.AccountDTO;
import domain.Enums.AccountType;
import dto.CreateAccountDTO;

import java.math.BigDecimal;

import static domain.Enums.AccountType.COURANT;
import static domain.Enums.AccountType.EPARGNE;

public class AccountMapper {
    public static Account toAccount(CreateAccountDTO dto, Client client) {
        switch (dto.getAccountType()) {
            case COURANT:
                return new CurrentAccount(
                        null,
                        null,
                        COURANT,
                        BigDecimal.valueOf(dto.getInitialeBalance()),
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
                        EPARGNE,
                        BigDecimal.valueOf(dto.getInitialeBalance()),
                        Currency.valueOf(dto.getCurrency().toUpperCase()),
                        null,
                        true,
                        client,
                        BigDecimal.valueOf(0.03)
                );

            default:
                throw new IllegalArgumentException("Unsupported account type: " + dto.getAccountType());
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
