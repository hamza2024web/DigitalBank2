package config;

import domain.Enums.Currency;
import domain.Enums.FeeMode;
import domain.Enums.TransactionType;
import domain.FeeRule;
import repository.FeeRuleRepositoryImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FeeConfig {
    private final FeeRuleRepositoryImpl feeRuleRepo;

    public FeeConfig(FeeRuleRepositoryImpl feeRuleRepo) {
        this.feeRuleRepo = feeRuleRepo;
    }

    public BigDecimal calculateFee(String operationType, BigDecimal amount, Currency currency) {
        try {
            TransactionType transactionType = TransactionType.valueOf(operationType.toUpperCase());
            return calculateFee(transactionType, amount, currency);
        } catch (IllegalArgumentException e) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calculateFee(TransactionType operationType, BigDecimal amount, Currency currency) {
        FeeRule rule = feeRuleRepo.findActiveByOperationAndCurrency(operationType, currency).orElse(null);

        if (rule == null) {
            return BigDecimal.ZERO;
        }

        if (rule.getMode() == FeeMode.FIX) {
            return rule.getValue().setScale(2, RoundingMode.HALF_UP);
        } else if (rule.getMode() == FeeMode.PERCENTAGE) {
            BigDecimal fee = amount.multiply(rule.getValue()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            return fee;
        } else {
            return BigDecimal.ZERO;
        }
    }
}