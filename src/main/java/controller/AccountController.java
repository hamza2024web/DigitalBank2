package controller;

import domain.User;
import dto.*;
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
                System.out.println("Nom : " + account.getClient().getNom() + "Prenom : " + account.getClient().getPrenom() + "Mounthly Income : " + account.getClient().getRevenueMensuel() + "IBAN: " + account.getIban() + ", Type: " + account.getType() + ", Balance: " + account.getSolde() + ", Currency: " + account.getDevise() + ", Created Date: " + account.getDateCreation());
            }
        }
    }

    public void clientAccountDeposit(ClientAccountDepositDTO clientAccountDeposit, User teller) {
        AccountDTO updatedAccount = accountService.clientAccountDeposit(clientAccountDeposit, teller);

        System.out.println("Deposit successful!");
        System.out.println("Client: " + updatedAccount.getClient().getNom() + " " + updatedAccount.getClient().getPrenom());
        System.out.println("IBAN: " + updatedAccount.getIban());
        System.out.println("New Balance: " + updatedAccount.getSolde() + " " + updatedAccount.getDevise());
        System.out.println("Deposit performed by teller: " + teller.getEmail() + " . ");
    }

    public void clientAccountWithdrawal(ClientAccountWithdrawalDTO clientAccountWithdrawal, User teller) {
        try {
            AccountDTO updatedAccount = accountService.clientAccountWithdrawal(clientAccountWithdrawal, teller);
            System.out.println("✓ Withdrawal completed successfully!");
            System.out.println("Client: " + updatedAccount.getClient().getNom() + " " + updatedAccount.getClient().getPrenom());
            System.out.println("IBAN: " + updatedAccount.getIban());
            System.out.println("Amount withdrawn: " + clientAccountWithdrawal.getAmount());
            System.out.println("Remaining balance: " + updatedAccount.getSolde() + " " + updatedAccount.getDevise());
            System.out.println("Deposit performed by teller: " + teller.getEmail() + " . ");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Withdrawal failed: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("✗ Error processing withdrawal: " + e.getMessage());
        }
    }

    public void clientTransferAccount(ClientAccountTransferDTO clientTransferAccount, User teller) {
        try {
            AccountDTO transferAccount = accountService.clientTransferAccount(clientTransferAccount, teller);
            System.out.println("✓ Transfer completed successfully!");
            System.out.println("Client: " + transferAccount.getClient().getNom() + " " + transferAccount.getClient().getPrenom());
            System.out.println("IBAN: " + transferAccount.getIban());
            System.out.println("Amount transferred: " + clientTransferAccount.getAmountTransaction());
            System.out.println("From: " + clientTransferAccount.getSendClientIban());
            System.out.println("To: " + clientTransferAccount.getDestinationClientIban());
            System.out.println("Remaining sender balance: " + transferAccount.getSolde() + " " + transferAccount.getDevise());
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Transfer failed: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("✗ Error processing transfer: " + e.getMessage());
        }
    }

}
