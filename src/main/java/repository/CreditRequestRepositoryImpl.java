package repository;

import domain.Client;
import domain.CreditRequest;
import domain.Enums.CreditStatus;
import domain.Enums.Currency;
import dto.CreditReqDTO;
import repository.Interface.CreditRequestRepository;
import util.JDBCUtil;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditRequestRepositoryImpl implements CreditRequestRepository {
    @Override
    public boolean save(CreditRequest creditRequest) {
        String sql = "INSERT INTO credit_requests (id, client_id, montant, currency, duree_mois, " +
                "taux_annuel, description, status, request_date, requested_by, monthly_income) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, creditRequest.getReferenceId());
            stmt.setLong(2, creditRequest.getClient().getId());
            stmt.setBigDecimal(3, creditRequest.getMontant());
            stmt.setString(4, creditRequest.getCurrency().name());
            stmt.setInt(5, creditRequest.getDureeMois());
            stmt.setBigDecimal(6, creditRequest.getTauxAnnuel());
            stmt.setString(7, creditRequest.getDescription());
            stmt.setString(8, creditRequest.getStatus().name());
            stmt.setDate(9, Date.valueOf(creditRequest.getRequestDate()));
            stmt.setString(10, creditRequest.getRequestedBy());
            stmt.setBigDecimal(11, creditRequest.getMonthlyIncome());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CreditRequest findById(String requestId) {
        String sql = "SELECT cr.*, c.id as client_id, c.nom, c.prenom, " +
                "c.revenue_mensuel " +
                "FROM credit_requests cr " +
                "INNER JOIN clients c ON cr.client_id = c.id " +
                "WHERE cr.id = ?";

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, requestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCreditRequest(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CreditRequest> findByStatus(CreditStatus status) {
        String sql = "SELECT " +
                "clients.id as client_id, " +
                "clients.nom, " +
                "clients.prenom, " +
                "clients.revenue_mensuel, " +
                "credit_requests.id as credit_id, " +
                "credit_requests.montant, " +
                "credit_requests.monthly_income, " +
                "credit_requests.currency, " +
                "credit_requests.duree_mois, " +
                "credit_requests.taux_annuel, " +
                "credit_requests.description, " +
                "credit_requests.status, " +
                "credit_requests.request_date, " +
                "credit_requests.requested_by " +
                "FROM credit_requests " +
                "INNER JOIN clients ON clients.id = credit_requests.client_id " +
                "WHERE credit_requests.status = ? " +
                "ORDER BY credit_requests.request_date DESC";
        List<CreditRequest> requests = new ArrayList<>();
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setString(1,status.name());
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                requests.add(mapResultSetToCreditRequest(rs));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }

        return requests;
    }

    @Override
    public boolean update(CreditRequest request) {
        return false;
    }

    @Override
    public List<CreditRequest> findByClientId(String clientId) {
        return List.of();
    }

    @Override
    public List<CreditRequest> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public List<CreditRequest> findByRequestedBy(String tellerEmail) {
        return List.of();
    }

    @Override
    public boolean delete(String requestId) {
        return false;
    }

    @Override
    public int countByStatus(CreditStatus status) {
        return 0;
    }

    @Override
    public List<CreditRequest> findAll() {
        return List.of();
    }

    @Override
    public List<CreditRequest> findOldestPendingRequests(int limit) {
        return List.of();
    }

    private CreditRequest mapResultSetToCreditRequest(ResultSet rs) throws SQLException {
        Client client = new Client(
                rs.getLong("client_id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getBigDecimal("revenue_mensuel")
        );

        return new CreditRequest(
                rs.getString("credit_id"),
                client,
                rs.getBigDecimal("montant"),
                rs.getBigDecimal("monthly_income"),
                Currency.valueOf(rs.getString("currency")),
                rs.getInt("duree_mois"),
                rs.getBigDecimal("taux_annuel"),
                rs.getString("description"),
                CreditStatus.valueOf(rs.getString("status")),
                rs.getDate("request_date").toLocalDate(),
                rs.getString("requested_by")
        );
    }
}
