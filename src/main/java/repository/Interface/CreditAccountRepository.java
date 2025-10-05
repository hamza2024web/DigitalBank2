package repository.Interface;

import domain.CreditAccount;

public interface CreditAccountRepository {
    boolean save(CreditAccount creditAccount);
}
