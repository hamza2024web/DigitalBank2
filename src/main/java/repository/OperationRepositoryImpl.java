package repository;

import domain.OperationHistory;
import util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OperationRepositoryImpl implements OperationRepository {
    @Override
    public void save(OperationHistory operationHistory) {
        String sql = "INSERT INTO operation_history (date_operation,operation_type,source_account_id,destination_account_id,description,status,amount,currency,reference) VALUES (?,?,?,?,?,?,?,?,?)";
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
}
