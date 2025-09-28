package repository;

import domain.Client;
import util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository {

    @Override
    public void save(Client client) {
        String sql = "INSERT INTO clients (id, prenom, nom, revenue_mensuel) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, client.getId());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getNom());
            stmt.setBigDecimal(4, client.getRevenueMensuel());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Client> findById(String clientId) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, clientId);
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
    public Optional<Client> findByFirsName(String firstName) {
        String sql = "SELECT * FROM clients WHERE prenom = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, firstName);
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
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE clients SET prenom = ?, nom = ?, revenue_mensuel = ? WHERE id = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, client.getPrenom());
            stmt.setString(2, client.getNom());
            stmt.setBigDecimal(3, client.getRevenueMensuel());
            stmt.setString(4, client.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String clientId) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, clientId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String clientId) {
        String sql = "SELECT COUNT(*) FROM clients WHERE id = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existsByFirsName(String firstName) {
        String sql = "SELECT COUNT(*) FROM clients WHERE prenom = ?";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, firstName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getString("id"),
                rs.getString("prenom"),
                rs.getString("nom"),
                rs.getBigDecimal("revenue_mensuel")
        );
    }
}
