package service;

import domain.Account;
import domain.Client;
import dto.AccountDTO;
import dto.CreateAccountDTO;
import mapper.AccountMapper;
import repository.AccountRepositoryImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class AccountService {
    private final AccountRepositoryImpl accountRepository;

    public AccountService(AccountRepositoryImpl accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDTO createAccount(CreateAccountDTO createAccountDTO) {
        try {
            Client client = findOrCreateClient(createAccountDTO.getLastName(), createAccountDTO.getFirstName(), createAccountDTO.getMonthlyIncome());

            Account account = AccountMapper.toAccount(createAccountDTO, client);

            account.setId(generateAccountId());
            account.setIban(generateIban());
            account.setDateCreation(LocalDate.now());
            account.setActive(true);

            setAccountSpecificProperties(account);

            accountRepository.save(account);

            return AccountMapper.toAccountDTO(account);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create account: " + e.getMessage(), e);
        }
    }

    private Client findOrCreateClient(String lastName, String firstName, String monthlyIncome) {
        try {
            BigDecimal income = new BigDecimal(monthlyIncome);

            String fullName = firstName + " " + lastName;

            Client client = new Client(generateClientId(),lastName, fullName, income);

            return client;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid monthly income format: " + monthlyIncome, e);
        }
    }

    private void setAccountSpecificProperties(Account account) {
        if (account instanceof domain.CurrentAccount currentAccount) {
            if (currentAccount.getDecouvertAutorise() == null) {
                currentAccount.setDecouvertAutorise(BigDecimal.ZERO);
            }
        } else if (account instanceof domain.SavingAccount savingAccount) {
            if (savingAccount.getTauxInteret() == null) {
                savingAccount.setTauxInteret(new BigDecimal("0.03")); // 3%
            }
        }
    }

    private String generateAccountId() {
        return "ACC-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateIban() {
        // Generate proper Moroccan IBAN format
        return "MA64BMCE" + String.format("%020d", System.currentTimeMillis() % 100000000000000000L);
    }

    private String generateClientId() {
        return "CLI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}