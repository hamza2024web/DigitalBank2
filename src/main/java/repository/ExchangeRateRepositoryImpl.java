package repository;

import util.JDBCUtil;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRateRepositoryImpl implements ExchangeRateRepository{
    @Override
    public BigDecimal findActiveByOperationAndCurrency(String fromCurrency, String toCurrency) {
        String sql = "SELECT rate FROM exchange_rates WHERE from_currency = ? AND to_currency = ? AND active = true";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, fromCurrency);
            stmt.setString(2, toCurrency);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("rate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Exchange rate not found for " + fromCurrency + " -> " + toCurrency);
    }

}
