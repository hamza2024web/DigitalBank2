package repository.Interface;

import domain.Enums.Currency;
import domain.Enums.TransactionType;
import domain.FeeRule;

import java.util.Optional;

public interface FeeRuleRepository {
    Optional<FeeRule> findActiveByOperationAndCurrency(TransactionType operationType, Currency currency);
}
