package repository;

import domain.CreditRequest;
import dto.CreditReqDTO;
import repository.Interface.CreditRequestRepository;
import util.JDBCUtil;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreditRequestRepositoryImpl implements CreditRequestRepository {
    @Override
    public boolean save(CreditRequest creditRequest) {
        String sql = "INSERT INTO credit_requests (id, client_id, montant, currency, duree_mois, " +
                "taux_annuel, description, status, request_date, requested_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, creditRequest.getId());
            stmt.setString(2, creditRequest.getClient().getId().toString());
            stmt.setBigDecimal(3, creditRequest.getMontant());
            stmt.setString(4, creditRequest.getCurrency().name());
            stmt.setInt(5, creditRequest.getDureeMois());
            stmt.setBigDecimal(6, creditRequest.getTauxAnnuel());
            stmt.setString(7, creditRequest.getDescription());
            stmt.setString(8, creditRequest.getStatus().name());
            stmt.setDate(9, Date.valueOf(creditRequest.getRequestDate()));
            stmt.setString(10, creditRequest.getRequestedBy());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
