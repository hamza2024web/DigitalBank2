package util;

import config.AppConfig;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverter {
    public static BigDecimal convert (BigDecimal amount, Currency from, Currency to){
        if (from == to) return amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal rate = AppConfig.getInstance().getRate(from.name(), to.name());
        return amount.multiply(rate).setScale(2,RoundingMode.HALF_UP);
    }
}
