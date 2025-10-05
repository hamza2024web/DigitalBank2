package repository;

import domain.Enums.Currency;
import domain.Enums.FeeMode; // Assuming you have this enum
import domain.Enums.TransactionType;
import domain.FeeRule;
import repository.Interface.FeeRuleRepository;
import util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class FeeRuleRepositoryImpl implements FeeRuleRepository {
    @Override
    public Optional<FeeRule> findActiveByOperationAndCurrency(TransactionType operationType, Currency currency) {
        String sql = "SELECT id, operation_type, mode, value, currency, is_active " +
                "FROM fee_rules " +
                "WHERE operation_type = ? AND is_active = TRUE AND (currency = ? OR currency IS NULL) " +
                "ORDER BY currency DESC NULLS LAST " +
                "LIMIT 1";

        try (PreparedStatement statement = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {

            statement.setString(1, operationType.name());
            statement.setString(2, currency.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    FeeRule rule = new FeeRule();
                    rule.setId(resultSet.getLong("id"));
                    rule.setOperationType(TransactionType.valueOf(resultSet.getString("operation_type")));
                    rule.setMode(FeeMode.valueOf(resultSet.getString("mode")));
                    rule.setValue(resultSet.getBigDecimal("value"));

                    String currencyStr = resultSet.getString("currency");
                    if (currencyStr != null) {
                        rule.setDevise(Currency.valueOf(currencyStr));
                    }

                    rule.setActive(resultSet.getBoolean("is_active"));

                    return Optional.of(rule);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}