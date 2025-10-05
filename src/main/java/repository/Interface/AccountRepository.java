package repository.Interface;

import domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Account saveCreditAccount(Account account);
    Optional<Account> findById(String accountId);
    Optional<Account> findByIban(String iban);
    List<Account> findByClientId(Long clientId);
    List<Account> findAll();
    void update(Account account);
    void delete(String accountId);
    boolean exists(String accountId);
    boolean ibanExists(String iban);
    void updateCloseStatus(Account account);
}
