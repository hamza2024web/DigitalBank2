package mapper;

import domain.*;
import domain.Enums.AccountCloseStatus;
import domain.Enums.Currency;
import dto.AccountDTO;
import domain.Enums.AccountType;
import dto.CreateAccountDTO;

import java.math.BigDecimal;

public class AccountMapper {

    /**
     * Maps CreateAccountDTO to Account entity
     * @param dto The DTO containing account creation data
     * @param client The client who owns the account
     * @return Account entity (CurrentAccount, SavingAccount, or CreditAccount)
     */
    public static Account toAccount(CreateAccountDTO dto, Client client) {
        AccountType accountType = AccountType.valueOf(dto.getAccountType().toUpperCase());
        BigDecimal initialBalance = new BigDecimal(dto.getInitialBalance());
        Currency currency = Currency.valueOf(dto.getCurrency().toUpperCase());

        switch (accountType) {
            case COURANT:
                // Using the simple constructor: iban, solde, devise, dateCreation, decouvertAutorise
                return new CurrentAccount(
                        null,                    // iban (will be generated later)
                        initialBalance,          // solde
                        currency,                // devise
                        null,                    // dateCreation (will be set later)
                        BigDecimal.ZERO          // decouvertAutorise (no overdraft by default)
                );

            case EPARGNE:
                // Using the simple constructor: iban, solde, devise, dateCreation, tauxInteret
                return new SavingAccount(
                        null,                    // iban (will be generated later)
                        initialBalance,          // solde
                        currency,                // devise
                        null,                    // dateCreation (will be set later)
                        new BigDecimal("0.03")   // tauxInteret (3% interest rate)
                );

            case CREDIT:
                // Assuming CreditAccount has a similar constructor
                return new CreditAccount(
                        null,                    // id
                        null,                    // iban
                        client,                  // client
                        initialBalance,          // credit limit
                        36,                      // duration in months
                        5.0,                     // interest rate
                        null                     // start date
                );

            default:
                throw new IllegalArgumentException("Unsupported account type: " + accountType);
        }
    }

    /**
     * Maps CreateAccountDTO to Account with custom overdraft (for CurrentAccount)
     * @param dto The DTO containing account creation data
     * @param client The client who owns the account
     * @param decouvertAutorise The overdraft limit
     * @return CurrentAccount entity with custom overdraft
     */
    public static CurrentAccount toCurrentAccountWithOverdraft(CreateAccountDTO dto, Client client, BigDecimal decouvertAutorise) {
        BigDecimal initialBalance = new BigDecimal(dto.getInitialBalance());
        Currency currency = Currency.valueOf(dto.getCurrency().toUpperCase());

        return new CurrentAccount(
                null,                    // iban (will be generated later)
                initialBalance,          // solde
                currency,                // devise
                null,                    // dateCreation (will be set later)
                decouvertAutorise        // decouvertAutorise (custom overdraft)
        );
    }

    /**
     * Maps CreateAccountDTO to Account with custom interest rate (for SavingAccount)
     * @param dto The DTO containing account creation data
     * @param client The client who owns the account
     * @param tauxInteret The interest rate
     * @return SavingAccount entity with custom interest rate
     */
    public static SavingAccount toSavingAccountWithInterest(CreateAccountDTO dto, Client client, BigDecimal tauxInteret) {
        BigDecimal initialBalance = new BigDecimal(dto.getInitialBalance());
        Currency currency = Currency.valueOf(dto.getCurrency().toUpperCase());

        return new SavingAccount(
                null,                    // iban (will be generated later)
                initialBalance,          // solde
                currency,                // devise
                null,                    // dateCreation (will be set later)
                tauxInteret              // tauxInteret (custom interest rate)
        );
    }

    /**
     * Maps Account entity to AccountDTO
     * @param account The account entity
     * @return AccountDTO for presentation layer
     */
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
                account.getCloseStatus()    // Include close status
        );
    }

    /**
     * Maps Account entity to AccountDTO with additional account-specific details
     * @param account The account entity
     * @return AccountDTO with extended information
     */
    public static AccountDTO toDetailedAccountDTO(Account account) {
        if (account == null) {
            return null;
        }

        AccountDTO dto = toAccountDTO(account);

        // Add account-specific details based on type
        if (account instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) account;
            // You could add overdraft info to the DTO if needed
        } else if (account instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) account;
            // You could add interest rate info to the DTO if needed
        }

        return dto;
    }
}
