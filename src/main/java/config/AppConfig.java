package config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    private static AppConfig instance;
    private final Map<String , BigDecimal> exchangeRates = new HashMap<>();
    private final BigDecimal amlThreshold;

    private AppConfig(){
        exchangeRates.put("MAD_EUR",new BigDecimal("0.091"));
        exchangeRates.put("EUR_MAD", new BigDecimal("11.00"));
        exchangeRates.put("MAD_USD", new BigDecimal("0.10"));
        exchangeRates.put("USD_MAD", new BigDecimal("10.00"));
        amlThreshold = new BigDecimal("50000");
    }

    public static synchronized AppConfig getInstance (){
        if (instance == null) instance = new AppConfig();
        return instance;
    }

    public BigDecimal getRate(String from , String to){
        if (from.equalsIgnoreCase(to)) return BigDecimal.ONE;
        BigDecimal r = exchangeRates.get(from.toUpperCase() + "_" + to.toUpperCase());
        if (r == null) throw new IllegalArgumentException("Exchange rate not found for " + from + "->" + to);
        return r;
    }

    public BigDecimal getAmlThreshold() {
        return amlThreshold;
    }


}
