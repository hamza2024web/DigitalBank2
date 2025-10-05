package repository;

import domain.Client;
import repository.Interface.ClientRepository;
import util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository {

    @Override
    public Client save(Client client) {
        String sql = "INSERT INTO clients (prenom, nom, revenue_mensuel) VALUES (?,?,?) RETURNING id";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, client.getPrenom());
            stmt.setString(2, client.getNom());
            stmt.setBigDecimal(3, client.getRevenueMensuel());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    client.setId(rs.getLong("id")); // set generated ID
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public Optional<Client> findById(Long clientId) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setLong(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToClient(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByLastName(String lastName) {
        String sql = "SELECT * FROM clients WHERE nom = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, lastName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToClient(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByNomAndPrenom(String lastName, String firstName) {
        String sql = "SELECT * FROM clients WHERE nom = ? AND prenom = ?";

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToClient(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getLong("id"),
                rs.getString("prenom"),
                rs.getString("nom"),
                rs.getBigDecimal("revenue_mensuel")
        );
    }
}
