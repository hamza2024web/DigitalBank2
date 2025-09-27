package mapper;

import domain.Account;
import domain.CreditAccount;
import domain.CurrentAccount;
import domain.SavingAccount;
import dto.AccountDTO;
import domain.Enums.AccountType;

public class AccountMapper {
    public static Account fromDTO(AccountDTO dto) {
        switch (dto.getType()) {
            case COURANT:
                return new CurrentAccount(
                        dto.getId(),
                        dto.getIban(),
                        dto.getType(),
                        dto.getSolde(),
                        dto.getDevise(),
                        dto.getDateCreation(),
                        dto.isActive(),
                        dto.getClient(),
                        dto.getDecouvertAutorise()
                );

            case EPARGNE:
                return new SavingAccount(
                        dto.getId(),
                        dto.getIban(),
                        dto.getType(),
                        dto.getSolde(),
                        dto.getDevise(),
                        dto.getDateCreation(),
                        dto.isActive(),
                        dto.getClient(),
                        dto.getTauxInteret()
                );

            case CREDIT:
                return new CreditAccount(

                );

            default:
                throw new IllegalArgumentException("Unknown account type: " + dto.getType());
        }
    }
}
