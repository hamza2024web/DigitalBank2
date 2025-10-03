package repository;

import domain.Account;
import domain.Enums.Currency;
import domain.OperationHistory;
import repository.Interface.OperationRepository;
import util.JDBCUtil;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OperationRepositoryImpl implements OperationRepository {
    @Override
    public void save(OperationHistory operationHistory) {
        String sql = "INSERT INTO operation_history (date_operation,operation_type,source_account_id,destination_account_id,description,status,amount,currency,reference) VALUES (?,?,?,?,?,?,?,?::currency_enum,?)";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(operationHistory.getDateOperation()));
            stmt.setString(2,operationHistory.getOperationType());
            stmt.setString(3,operationHistory.getSourceAccountId());
            stmt.setString(4,operationHistory.getDestinationAccountId());
            stmt.setString(5,operationHistory.getDescription());
            stmt.setString(6,operationHistory.getStatus());
            stmt.setBigDecimal(7,operationHistory.getAmount());
            stmt.setString(8,operationHistory.getCurrency().toString());
            stmt.setString(9,operationHistory.getReference());

            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public List<OperationHistory> findByAccountId(Account account) {
        String sql = "SELECT * FROM operation_history WHERE source_account_id = ? ORDER BY date_operation DESC";
        List<OperationHistory> operations = new ArrayList<>();

        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, account.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    LocalDateTime dateOperation = rs.getTimestamp("date_operation").toLocalDateTime();
                    String operationType = rs.getString("operation_type");
                    String sourceAccountId = rs.getString("source_account_id");
                    String destinationAccountId = rs.getString("destination_account_id");
                    String description = rs.getString("description");
                    String status = rs.getString("status");
                    BigDecimal amount = rs.getBigDecimal("amount");
                    Currency currency = Currency.valueOf(rs.getString("currency"));
                    String reference = rs.getString("reference");

                    OperationHistory operation = new OperationHistory(
                            dateOperation,
                            operationType,
                            sourceAccountId,
                            destinationAccountId,
                            description,
                            status,
                            amount,
                            currency,
                            reference
                    );
                    operation.setId(id);

                    operations.add(operation);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching operations for account: " + account.getIban(), e);
        }

        return operations;
    }


}
