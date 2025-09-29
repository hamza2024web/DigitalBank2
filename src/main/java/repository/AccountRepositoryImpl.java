package repository;

import domain.*;
import domain.Enums.Currency;
import util.JDBCUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository{

    @Override
    public void save(Account account) {
        Connection connection = null;
        try {
            connection = JDBCUtil.getInstance().getConnection();
            connection.setAutoCommit(false);

            saveAccount(connection,account);

            saveAccountSpecificData(connection,account);

            connection.commit();
        } catch(SQLException e){
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Account> findById(String accountId) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByIban(String iban) {
        String sql = "SELECT * FROM accounts LEFT JOIN courant_accounts ON courant_accounts.account_id = accounts.id LEFT JOIN saving_accounts ON saving_accounts.account_id = accounts.id WHERE iban = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, iban);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    Account account;

                    String AccountIban = rs.getString("iban");
                    BigDecimal solde = rs.getBigDecimal("solde");
                    Currency devise = Currency.valueOf(rs.getString("devise"));
                    LocalDate dateCreation = rs.getDate("date_creation").toLocalDate();

                    ClientRepositoryImpl clientRepository = new ClientRepositoryImpl();
                    Long client_id = rs.getLong("client_id");

                    Client client = clientRepository.findById(client_id).orElseThrow(() -> new RuntimeException("Client not found with id: " + client_id));

                    if ("COURANT".equalsIgnoreCase(type)) {
                        BigDecimal decouvertAutorise = rs.getBigDecimal("decouvert_autorise");
                        account = new CurrentAccount(AccountIban, solde, devise, dateCreation, decouvertAutorise);
                    } else if ("EPARGNE".equalsIgnoreCase(type)) {
                        BigDecimal tauxInteret = rs.getBigDecimal("taux_interet");
                        account = new SavingAccount(AccountIban, solde, devise, dateCreation, tauxInteret);
                    } else {
                        throw new IllegalStateException("Unknown account type: " + type);
                    }

                    account.setClient(client);

                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        String sql = "SELECT COUNT(*) FROM accounts WHERE id = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setString(1,accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean ibanExists(String iban) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE iban = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setString(1,iban);

            try (ResultSet rs = stmt.executeQuery()){
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private void saveAccount(Connection connection , Account account) throws SQLException{
        String sql = "INSERT INTO accounts (id,iban,type,solde,devise,date_creation,is_active,client_id) VALUES (?,?,?::account_type_enum,?,?::currency_enum,?,?,?) ";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setString(1, account.getId());
            stmt.setString(2, account.getIban());
            stmt.setString(3, account.getAccountType().toString());
            stmt.setBigDecimal(4, account.getSolde());
            stmt.setString(5, account.getDevise().toString());
            stmt.setDate(6, java.sql.Date.valueOf(account.getDate()));
            stmt.setBoolean(7, account.getActive());
            stmt.setLong(8, account.getClient().getId());

            stmt.executeUpdate();
        }
    }

    private void saveAccountSpecificData(Connection connection, Account account) throws SQLException {
        if (account instanceof CurrentAccount currentAccount){
            saveCourantAccountData(connection,currentAccount);
        } else if (account instanceof SavingAccount savingAccount){
            saveEpargneAccountData(connection,savingAccount);
        } else if (account instanceof CreditAccount creditAccount){
            saveCreditAccountData(connection, creditAccount);
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

    private void saveCreditAccountData(Connection connection, CreditAccount account) throws SQLException {
        String sql = "INSERT INTO credit_accounts (account_id, montant_demande, duree_mois, taux_annuel, statut, solde_restant, date_demande, date_prochaine_echeance, compte_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getId());
            stmt.setBigDecimal(2, account.getMontantDemande());
            stmt.setInt(3, account.getDureeMois());
            stmt.setDouble(4, account.getTauxAnnuel());
            stmt.setString(5, account.getStatut().toString());
            stmt.setBigDecimal(6, account.getSoldeRestant());
            stmt.setDate(7, java.sql.Date.valueOf(account.getDateDemande()));
            stmt.setDate(8, java.sql.Date.valueOf(account.getDateProchaineEcheance()));
            stmt.setString(9, account.getRelatedAccount() != null ? account.getRelatedAccount().getId() : null);
            stmt.executeUpdate();
        }
    }
}
