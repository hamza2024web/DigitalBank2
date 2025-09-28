package controller;

import domain.User;
import dto.CreateAccountDTO;
import service.AccountService;

public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    public void createAccount(CreateAccountDTO createAccountDTO , User teller){
        accountService.createAccount(createAccountDTO,teller);
        System.out.println("Creating account for: " + createAccountDTO.getFirstName() + createAccountDTO.getLastName()
                + " | Type: " + createAccountDTO.getAccountType()
                + " | Balance: " + createAccountDTO.getInitialBalance()
                + " | Currency: " + createAccountDTO.getCurrency());
    }
}
