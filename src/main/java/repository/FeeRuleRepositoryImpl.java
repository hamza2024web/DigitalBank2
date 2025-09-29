package repository;

import domain.Enums.Currency;
import domain.Enums.TransactionType;
import domain.FeeRule;

import java.util.Optional;

public class FeeRuleRepositoryImpl implements FeeRuleRepository{

    @Override
    public Optional<FeeRule> findActiveByOperationAndCurrency(TransactionType operationType, Currency currency) {

        return Optional.empty();
    }
}
