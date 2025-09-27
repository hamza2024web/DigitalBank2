package controller;

import dto.CreateAccountDTO;
import service.AccountService;

public class AccountController {


    public void createAccount(CreateAccountDTO createAccountDTO){
        System.out.println("Creating account for: " + createAccountDTO.getClientName()
                + " | Type: " + createAccountDTO.getAccountType()
                + " | Balance: " + createAccountDTO.getInitialeBalance()
                + " | Currency: " + createAccountDTO.getCurrency());
    }
}
