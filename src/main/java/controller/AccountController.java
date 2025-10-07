package controller;

import domain.OperationHistory;
import domain.User;
import dto.*;
import service.AccountService;

import java.util.List;

public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void createAccount(CreateAccountDTO createAccountDTO, User teller) {
        accountService.createAccount(createAccountDTO, teller);
        System.out.println("✓ Creating account for: " + createAccountDTO.getFirstName() + " " + createAccountDTO.getLastName()
                + " | Type: " + createAccountDTO.getAccountType()
                + " | Balance: " + createAccountDTO.getInitialBalance()
                + " | Currency: " + createAccountDTO.getCurrency());
    }

    public void clientAccountRequest(ClientAccountsRequestDTO clientAccountRequest, User teller) {
        List<AccountDTO> accounts = accountService.clientAccountRequest(clientAccountRequest, teller);

        if (accounts.isEmpty()) {
            System.out.println("No accounts found for this client.");
        } else {
            System.out.println("Accounts for client:");
            for (AccountDTO account : accounts) {
                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("Name: " + account.getClient().getNom() + " " + account.getClient().getPrenom());
                System.out.println("Monthly Income: " + account.getClient().getRevenueMensuel());
                System.out.println("IBAN: " + account.getIban());
                System.out.println("Type: " + account.getType());
                System.out.println("Balance: " + account.getSolde() + " " + account.getDevise());
                System.out.println("Created Date: " + account.getDateCreation());
                System.out.println("Status: " + (account.isActive() ? "Active" : "Inactive"));
                System.out.println("─────────────────────────────────────────────────────────");
            }
        }
    }

    public void clientAccountDeposit(ClientAccountDepositDTO clientAccountDeposit, User teller) {
        try {
            AccountDTO depositAccount = accountService.clientAccountDeposit(clientAccountDeposit, teller);

            System.out.println("✓ Deposit successful!");
            System.out.println("Client: " + depositAccount.getClient().getNom() + " " + depositAccount.getClient().getPrenom());
            System.out.println("IBAN: " + depositAccount.getIban());
            System.out.println("New Balance: " + depositAccount.getSolde() + " " + depositAccount.getDevise());
            System.out.println("Operation performed by: " + teller.getEmail());

        } catch (IllegalArgumentException e) {
            System.out.println("✗ Deposit failed: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("✗ Error processing deposit: " + e.getMessage());
        }
    }

    public void clientAccountWithdrawal(ClientAccountWithdrawalDTO clientAccountWithdrawal, User teller) {
        try {
            AccountDTO updatedAccount = accountService.clientAccountWithdrawal(clientAccountWithdrawal, teller);
            System.out.println("✓ Withdrawal completed successfully!");
            System.out.println("Client: " + updatedAccount.getClient().getNom() + " " + updatedAccount.getClient().getPrenom());
            System.out.println("IBAN: " + updatedAccount.getIban());
            System.out.println("Amount withdrawn: " + clientAccountWithdrawal.getAmount());
            System.out.println("Remaining balance: " + updatedAccount.getSolde() + " " + updatedAccount.getDevise());
            System.out.println("Operation performed by teller: " + teller.getEmail());
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
            System.out.println("Sender IBAN: " + clientTransferAccount.getSendClientIban());
            System.out.println("Recipient IBAN: " + clientTransferAccount.getDestinationClientIban());
            System.out.println("Amount transferred: " + clientTransferAccount.getAmountTransaction());
            System.out.println("Remaining sender balance: " + transferAccount.getSolde() + " " + transferAccount.getDevise());
            System.out.println("Operation performed by teller: " + teller.getEmail());
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Transfer failed: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("✗ Error processing transfer: " + e.getMessage());
        }
    }

    public void clientAccountClose(ClientAccountCloseDTO clientAccountClose, User teller) {
        try {
            AccountDTO closeAccount = accountService.clientAccountClose(clientAccountClose, teller);
            System.out.println("✓ Account closure request processed successfully , Please Wait for the manager to validate the process !");
            System.out.println("Client: " + closeAccount.getClient().getNom() + " " + closeAccount.getClient().getPrenom());
            System.out.println("IBAN: " + closeAccount.getIban());
            System.out.println("Account Type: " + closeAccount.getType());
            System.out.println("Final Balance: " + closeAccount.getSolde() + " " + closeAccount.getDevise());
            System.out.println("Close Status: " + closeAccount.getCloseStatus());
            System.out.println("Operation performed by teller: " + teller.getEmail());
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Account closure failed: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("✗ Error processing account closure: " + e.getMessage());
        }
    }

    public void clientAccountHistory(ClientAccountHistoryDTO clientAccountHistory, User teller) {
        try {
            AccountDTO accountDTO = accountService.findAccountByIban(clientAccountHistory.getClientIban());

            List<OperationHistory> operations = accountService.clientAccountHistory(clientAccountHistory, teller);

            accountDTO.setOperations(operations);

            System.out.println("==================================================");
            System.out.println("               ACCOUNT HISTORY DETAILS");
            System.out.println("==================================================");
            System.out.println("Client       : " + accountDTO.getClient().getNom() + " " + accountDTO.getClient().getPrenom());
            System.out.println("IBAN         : " + accountDTO.getIban());
            System.out.println("Account Type : " + accountDTO.getType());
            System.out.println("Balance      : " + accountDTO.getSolde() + " " + accountDTO.getDevise());
            System.out.println("Close Status : " + accountDTO.getCloseStatus());
            System.out.println("--------------------------------------------------");
            System.out.println("Operations History:");
            System.out.println("--------------------------------------------------");
            System.out.printf("| %-21s | %-13s | %-10s | %-8s | %-8s |\n",
                    "Date/Time", "Type", "Amount", "Currency", "Status");
            System.out.println("|-----------------------|---------------|------------|----------|----------|");

            if (accountDTO.getOperations() == null || accountDTO.getOperations().isEmpty()) {
                System.out.println("| No operations found for this account.                                   |");
            } else {
                for (OperationHistory op : accountDTO.getOperations()) {
                    String sign = op.getOperationType().contains("WITHDRAW") || op.getOperationType().contains("TRANSFER_OUT") ? "-" : "+";
                    System.out.printf("| %-21s | %-13s | %s%-9s | %-8s | %-8s |\n",
                            op.getDateOperation(),
                            op.getOperationType(),
                            sign,
                            op.getAmount(),
                            op.getCurrency(),
                            op.getStatus());
                }
            }

            System.out.println("--------------------------------------------------");
            System.out.println("Operation performed by teller: " + teller.getEmail());
            System.out.println("==================================================");

        } catch (IllegalArgumentException e) {
            System.out.println("✗ Account history failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ An error occurred while retrieving account history: " + e.getMessage());
        }
    }

}