package repository;

import domain.Account;
import domain.Client;
import domain.CreditAccount;
import domain.CurrentAccount;
import domain.Enums.AccountCloseStatus;
import domain.Enums.AccountType;
import domain.Enums.CreditStatus;
import domain.Enums.Currency;
import repository.Interface.CreditAccountRepository;
import util.JDBCUtil;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditAccountRepositoryImpl implements CreditAccountRepository {
    @Override
    public boolean save(CreditAccount creditAccount) {
        String sql = "INSERT INTO credit_accounts (montant_initiale,duree_mois,taux_annuel,statut,date_demande,date_prochaine_eheance,related_account_id,solde_restant)" +
                "VALUES (?,?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setBigDecimal(1,creditAccount.getMontantDemande());
            stmt.setInt(2,creditAccount.getDureeMois());
            stmt.setBigDecimal(3,creditAccount.getTauxAnnuel());
            stmt.setString(4,creditAccount.getStatut().name());
            stmt.setObject(5,creditAccount.getDateDemande());
            stmt.setObject(6,creditAccount.getDateProchaineEcheance());
            stmt.setString(7,creditAccount.getRelatedAccount().getId());
            stmt.setBigDecimal(8,creditAccount.getSoldeRestant());
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CreditAccount> getAllCreditAccount() {
        String sql = "SELECT ca.*, a.iban, a.solde, a.devise, a.date_creation, a.is_active, a.close_status, " +
                "c.id as client_id, c.nom, c.prenom, c.revenue_mensuel, " +
                "ra.id as related_account_id, ra.iban as related_iban, ra.type as related_type, " +
                "ra.solde as related_solde, ra.devise as related_devise, ra.date_creation as related_date_creation, " +
                "ra.is_active as related_is_active, ra.close_status as related_close_status " +
                "FROM credit_accounts ca " +
                "INNER JOIN accounts a ON ca.account_id = a.id " +
                "INNER JOIN clients c ON a.client_id = c.id " +
                "LEFT JOIN accounts ra ON ca.related_account_id = ra.id";

        List<CreditAccount> accounts = new ArrayList<>();

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while(rs.next()){
                String accountId = rs.getString("account_id");
                String iban = rs.getString("iban");
                BigDecimal solde = rs.getBigDecimal("solde");
                Currency devise = Currency.valueOf(rs.getString("devise"));
                LocalDate dateCreation = rs.getDate("date_creation").toLocalDate();
                boolean isActive = rs.getBoolean("is_active");
                AccountCloseStatus closeStatus = AccountCloseStatus.valueOf(rs.getString("close_status"));

                Client client = new Client(
                        rs.getLong("client_id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getBigDecimal("revenue_mensuel")
                );

                BigDecimal montantDemande = rs.getBigDecimal("montant_demande");
                int dureeMois = rs.getInt("duree_mois");
                BigDecimal tauxAnnuel = rs.getBigDecimal("taux_annuel");
                CreditStatus statut = CreditStatus.valueOf(rs.getString("statut"));
                BigDecimal soldeRestant = rs.getBigDecimal("solde_restant");
                LocalDate dateDemande = rs.getDate("date_demande").toLocalDate();
                LocalDate dateProchaineEcheance = rs.getDate("date_prochaine_echeance") != null
                        ? rs.getDate("date_prochaine_echeance").toLocalDate()
                        : null;

                Account relatedAccount = null;
                String relatedAccountId = rs.getString("related_account_id");
                if (relatedAccountId != null && !rs.wasNull()) {
                    String relatedIban = rs.getString("related_iban");
                    AccountType relatedType = AccountType.valueOf(rs.getString("related_type"));
                    BigDecimal relatedSolde = rs.getBigDecimal("related_solde");
                    Currency relatedDevise = Currency.valueOf(rs.getString("related_devise"));
                    LocalDate relatedDateCreation = rs.getDate("related_date_creation").toLocalDate();
                    boolean relatedIsActive = rs.getBoolean("related_is_active");
                    AccountCloseStatus relatedCloseStatus = AccountCloseStatus.valueOf(rs.getString("related_close_status"));

                    if (relatedType == AccountType.CREDIT) {
                        relatedAccount = new CreditAccount(
                                relatedAccountId,
                                relatedIban,
                                relatedSolde,
                                relatedDevise,
                                relatedDateCreation,
                                relatedIsActive,
                                client,
                                relatedCloseStatus
                        );
                    }
                }

                CreditAccount creditAccount = new CreditAccount(
                        accountId,
                        iban,
                        solde,
                        devise,
                        dateCreation,
                        isActive,
                        client,
                        closeStatus,
                        montantDemande,
                        dureeMois,
                        tauxAnnuel,
                        statut,
                        soldeRestant,
                        dateDemande,
                        dateProchaineEcheance,
                        relatedAccount
                );

                accounts.add(creditAccount);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return accounts;
    }
}
