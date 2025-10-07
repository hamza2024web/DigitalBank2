package repository.Interface;

import java.math.BigDecimal;

public interface ExchangeRateRepository {
    BigDecimal findActiveByOperationAndCurrency(String fromCurrency, String toCurrency);
}
