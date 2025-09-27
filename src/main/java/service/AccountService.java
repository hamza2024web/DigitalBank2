package service;

import domain.Account;
import domain.Client;
import dto.AccountDTO;
import dto.CreateAccountDTO;
import mapper.AccountMapper;
import repository.AccountRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public AccountDTO createAccount(CreateAccountDTO createAccountDTO){
        Client client = findOrCreateClient(createAccountDTO.getClientName());

        Account account = AccountMapper.toAccount(createAccountDTO , client);

        account.setId(generateAccountId());
        account.setIban(generateIban);
        account.setDateCreation(LocalDate.now());
        account.setActive(true);

        if (account instanceof domain.CurrentAccount currentAccount){
            currentAccount.setDecouvertAutorise(BigDecimal.ZERO);
        } else if (account instanceof domain.SavingAccount savingAccount){
            savingAccount.setTauxInteret(BigDecimal.valueOf(0.03));
        }

        accountRepository.save(account);

        return AccountMapper.toAccountDTO(account);
    }

    private Client findOrCreateClient(String clientName){
        return new Client(clientName);
    }

    private String generateAccountId(){
        return "BK-" +LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0,8);
    }

    private String generateIban(){
        return "IBAN" + UUID.randomUUID().toString().substring(0,16);
    }
}
