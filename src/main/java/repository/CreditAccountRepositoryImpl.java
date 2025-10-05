package repository;

import domain.CreditAccount;
import repository.Interface.CreditAccountRepository;
import util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
