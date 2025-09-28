package repository;

import domain.Account;
import util.JDBCUtil;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository{

    @Override
    public void save(Account account) {

    }

    @Override
    public Optional<Account> findById(String accountId) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByIban(String iban) {
        return Optional.empty();
    }

    @Override
    public List<Account> findByClientId(String clientId) {
        return List.of();
    }

    @Override
    public List<Account> findAll() {
        return List.of();
    }

    @Override
    public void update(Account account) {

    }

    @Override
    public void delete(String accountId) {

    }

    @Override
    public boolean exists(String accountId) {
        return false;
    }

    @Override
    public boolean ibanExists(String iban) {
        return false;
    }
}
