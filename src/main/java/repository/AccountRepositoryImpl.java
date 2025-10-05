package repository;

import domain.*;
import domain.Enums.AccountCloseStatus;
import domain.Enums.AccountType;
import domain.Enums.Currency;
import repository.Interface.AccountRepository;
import util.JDBCUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    @Override
    public void save(Account account) {
        Connection connection = null;
        try {
            connection = JDBCUtil.getInstance().getConnection();
            connection.setAutoCommit(false);

            saveAccount(connection, account);
            saveAccountSpecificData(connection, account);

            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Error saving account", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Account saveCreditAccount(Account account) {
        String sql = "INSERT INTO accounts (id, iban, type, solde, devise, date_creation, is_active, client_id, close_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, account.getId());
            statement.setString(2, account.getIban());
            statement.setString(3, account.getAccountType().name());
            statement.setBigDecimal(4, account.getSolde());
            statement.setString(5, account.getDevise().name());
            statement.setDate(6, java.sql.Date.valueOf(account.getDate()));
            statement.setBoolean(7, account.getActive());
            statement.setLong(8, account.getClient().getId());
            statement.setString(9, account.getCloseStatus().name());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public Optional<Account> findById(String accountId) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByIban(String iban) {
        String sql = "SELECT * FROM accounts " +
                "LEFT JOIN courant_accounts ON courant_accounts.account_id = accounts.id " +
                "LEFT JOIN saving_accounts ON saving_accounts.account_id = accounts.id " +
                "WHERE iban = ?";

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, iban);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    Account account;

                    String accountId = rs.getString("id");
                    String accountIban = rs.getString("iban");
                    BigDecimal solde = rs.getBigDecimal("solde");
                    Currency devise = Currency.valueOf(rs.getString("devise"));
                    LocalDate dateCreation = rs.getDate("date_creation").toLocalDate();
                    boolean isActive = rs.getBoolean("is_active"); // Fixed: Added missing field
                    AccountCloseStatus closeStatus = AccountCloseStatus.valueOf(rs.getString("close_status"));

                    ClientRepositoryImpl clientRepository = new ClientRepositoryImpl();
                    Long clientId = rs.getLong("client_id");
                    Client client = clientRepository.findById(clientId)
                            .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

                    if ("COURANT".equalsIgnoreCase(type)) {
                        BigDecimal decouvertAutorise = rs.getBigDecimal("decouvert_autorise");
                        account = new CurrentAccount(
                                accountId,
                                accountIban,
                                solde,
                                devise,
                                dateCreation,
                                isActive,
                                client,
                                decouvertAutorise,
                                closeStatus
                        );
                    } else if ("EPARGNE".equalsIgnoreCase(type)) {
                        BigDecimal tauxInteret = rs.getBigDecimal("taux_interet");
                        account = new SavingAccount(
                                accountId,
                                accountIban,
                                solde,
                                devise,
                                dateCreation,
                                isActive,
                                client,
                                tauxInteret,
                                closeStatus
                        );
                    } else {
                        throw new IllegalStateException("Unknown account type: " + type);
                    }

                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding account by IBAN: " + iban, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Account> findByClientId(Long clientId) {
        String sql = "SELECT * FROM accounts " +
                "LEFT JOIN courant_accounts ON courant_accounts.account_id = accounts.id " +
                "LEFT JOIN saving_accounts ON saving_accounts.account_id = accounts.id " +
                "WHERE accounts.client_id = ?";

        List<Account> accounts = new ArrayList<>();

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setLong(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String accountId = rs.getString("id");
                    String accountIban = rs.getString("iban");
                    BigDecimal solde = rs.getBigDecimal("solde");
                    Currency devise = Currency.valueOf(rs.getString("devise"));
                    LocalDate dateCreation = rs.getDate("date_creation").toLocalDate();
                    boolean isActive = rs.getBoolean("is_active");
                    AccountCloseStatus closeStatus = AccountCloseStatus.valueOf(rs.getString("close_status"));
                    AccountType accountType = AccountType.valueOf(rs.getString("type"));

                    ClientRepositoryImpl clientRepository = new ClientRepositoryImpl();
                    Client client = clientRepository.findById(clientId)
                            .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

                    Account account;

                    if (accountType == AccountType.COURANT) {
                        BigDecimal decouvertAutorise = rs.getBigDecimal("decouvert_autorise");
                        account = new CurrentAccount(
                                accountId,
                                accountIban,
                                solde,
                                devise,
                                dateCreation,
                                isActive,
                                client,
                                decouvertAutorise,
                                closeStatus
                        );
                    } else if (accountType == AccountType.EPARGNE) {
                        BigDecimal tauxInteret = rs.getBigDecimal("taux_interet");
                        account = new SavingAccount(
                                accountId,
                                accountIban,
                                solde,
                                devise,
                                dateCreation,
                                isActive,
                                client,
                                tauxInteret,
                                closeStatus
                        );
                    } else {
                        throw new IllegalStateException("Unsupported account type: " + accountType);
                    }

                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding accounts by clientId: " + clientId, e);
        }

        return accounts;
    }


    @Override
    public List<Account> findAll() {
        return List.of();
    }

    @Override
    public void update(Account account) {
        String sql = "UPDATE accounts SET solde = ? WHERE id = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setBigDecimal(1, account.getSolde());
            stmt.setString(2, account.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("No account found with ID: " + account.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account", e);
        }
    }

    @Override
    public void delete(String accountId) {
        // Implementation needed
    }

    @Override
    public boolean exists(String accountId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE id = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking account existence", e);
        }
    }

    @Override
    public boolean ibanExists(String iban) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE iban = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, iban);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking IBAN existence", e);
        }
    }

    @Override
    public void updateCloseStatus(Account account) {
        String sql = "UPDATE accounts SET close_status = ?::close_status_enum WHERE id = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, account.getCloseStatus().toString());
            stmt.setString(2, account.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("No account found with ID: " + account.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account close status", e);
        }
    }

    private void saveAccount(Connection connection, Account account) throws SQLException {
        String sql = "INSERT INTO accounts (id, iban, type, solde, devise, date_creation, is_active, client_id, close_status) " +
                "VALUES (?, ?, ?::account_type_enum, ?, ?::currency_enum, ?, ?, ?, ?::close_status_enum)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getId());
            stmt.setString(2, account.getIban());
            stmt.setString(3, account.getAccountType().toString());
            stmt.setBigDecimal(4, account.getSolde());
            stmt.setString(5, account.getDevise().toString());
            stmt.setDate(6, java.sql.Date.valueOf(account.getDate()));
            stmt.setBoolean(7, account.getActive());
            stmt.setLong(8, account.getClient().getId());
            stmt.setString(9, account.getCloseStatus().toString());

            stmt.executeUpdate();
        }
    }

    private void saveAccountSpecificData(Connection connection, Account account) throws SQLException {
        if (account instanceof CurrentAccount currentAccount) {
            saveCourantAccountData(connection, currentAccount);
        } else if (account instanceof SavingAccount savingAccount) {
            saveEpargneAccountData(connection, savingAccount);
        }
    }

    private void saveCourantAccountData(Connection connection, CurrentAccount account) throws SQLException {
        String sql = "INSERT INTO courant_accounts (account_id, decouvert_autorise) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getId());
            stmt.setBigDecimal(2, account.getDecouvertAutorise());
            stmt.executeUpdate();
        }
    }

    private void saveEpargneAccountData(Connection connection, SavingAccount account) throws SQLException {
        String sql = "INSERT INTO saving_accounts (account_id, taux_interet) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getId());
            stmt.setBigDecimal(2, account.getTauxInteret());
            stmt.executeUpdate();
        }
    }

}