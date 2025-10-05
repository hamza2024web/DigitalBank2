package repository.Interface;

import domain.CreditAccount;

import java.util.List;

public interface CreditAccountRepository {
    boolean save(CreditAccount creditAccount);
    List<CreditAccount> getAllCreditAccount();
    List<CreditAccount> getAllApproveCredit();
}
