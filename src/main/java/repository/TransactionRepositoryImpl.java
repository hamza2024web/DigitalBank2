package repository;

import domain.Transaction;
import util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRepositoryImpl implements TransactionRepository{
    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions " +
                "(amount, type, status, timestamp, currency, description, source_account_id, destination_account_id) " +
                "VALUES (?, ?::transaction_type_enum, ?::transaction_status_enum, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setBigDecimal(1, transaction.getMontant());
            stmt.setString(2, transaction.getType().name());
            stmt.setString(3, transaction.getStatus().name());
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(transaction.getDateTransaction()));
            stmt.setString(5, transaction.getDevise().toString());
            stmt.setString(6, transaction.getDescription());
            stmt.setString(7, transaction.getAccountSource().getId());
            stmt.setString(8, transaction.getAccountDestination().getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save transaction: " + e.getMessage(), e);
        }
    }

}
