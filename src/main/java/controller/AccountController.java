package controller;

import domain.User;
import dto.AccountDTO;
import dto.ClientAccountsRequestDTO;
import dto.CreateAccountDTO;
import service.AccountService;

import java.util.List;

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

    public void clientAccountRequest(ClientAccountsRequestDTO clientAccountRequest , User teller){
        List<AccountDTO> accounts = accountService.clientAccountRequest(clientAccountRequest,teller);

        if(accounts.isEmpty()){
            System.out.println("No accounts found for this client.");
        } else {
            System.out.println("Accounts for client:");
            for (AccountDTO account : accounts) {
                System.out.println("IBAN: " + account.getIban() + ", Type: " + account.getType() + ", Balance: " + account.getSolde() + ", Currency: " + account.getDevise() + ", Created Date: " + account.getDateCreation());
            }
        }
    }
}
