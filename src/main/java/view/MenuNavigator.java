package view;

import controller.AccountController;
import controller.AuthController;
import controller.CreditController;
import domain.User;
import dto.*;
import mapper.UserMapper;

import java.util.Scanner;

public class MenuNavigator {
    private final AuthController authController;
    private final TellerView tellerView;
    private final CreditView creditView;
    private final AccountController accountController;
    private final CreditController creditController;
    private User loggedInUser;
    private final ManagerView managerView;
    private final AdminView adminView;
    private final Scanner scanner = new Scanner(System.in);
    public MenuNavigator(AuthController authController, TellerView tellerView, CreditView creditView, AccountController accountController, CreditController creditController, ManagerView managerView, AdminView adminView){
        this.authController = authController;
        this.tellerView = tellerView;
        this.creditView = creditView;
        this.accountController = accountController;
        this.creditController = creditController;
        this.managerView = managerView;
        this.adminView = adminView;
    }

    public void start(){
        while (true){
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Login");
            System.out.println("2. Logout");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            int choice = getValidChoice();

            switch(choice){
                case 1 -> {
                    loggedInUser = authController.login();
                    if (loggedInUser != null){
                        handleRoleBasedMenu(UserMapper.toUserDTO(loggedInUser));
                    }
                }
                case 2 -> authController.logout();
                case 3 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleRoleBasedMenu(UserDTO userDTO) {
        switch (userDTO.getRole()){
            case ADMIN -> showAdminMenu();
            case MANAGER -> showManagerMenu();
            case TELLER -> showTellerMenu(loggedInUser);
        }
    }

    private void showAdminMenu(){
        while (true) {
            adminView.displayMenu();
            int choice = getValidChoice();

            switch (choice) {
                case 1 -> {
                    showTellerMenu(loggedInUser);
                }
                case 2 -> {
                    String creditId = managerView.askManagerRequestId();
                    System.out.println("request processing ...");
                    ManagerCreditApproveDTO managerCreditApproveDto = new ManagerCreditApproveDTO(creditId,loggedInUser);
                    creditController.approveCreditRequest(managerCreditApproveDto);
                }
                case 3 -> {
                    String creditId = managerView.askManagerRequestId();
                    System.out.println("request processing ...");
                    ManagerCreditApproveDTO managerCreditApproveDto = new ManagerCreditApproveDTO(creditId,loggedInUser);
                    creditController.rejectCreditRequest(managerCreditApproveDto);
                }
                case 4 -> {
                    System.out.println("Request processing ...");
                    ManagerCreditPendingDTO mangerCreditPendingDto = new ManagerCreditPendingDTO(loggedInUser);
                    creditController.creditPending(loggedInUser);
                }
                case 5 -> {
                    System.out.println("Request processing ...");
                    AdminCreditDTO adminCreditDTO = new AdminCreditDTO(loggedInUser);
                    creditController.getAllApproveCredit(loggedInUser);
                }
                case 6 -> {
                    String email = adminView.askAdminTellerEmail();
                    AdminCreditLogDTO adminCreditLogDTO = new AdminCreditLogDTO(email,loggedInUser);
                    creditController.getTellerManagerLog(adminCreditLogDTO);
                }
                case 7 -> {

                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showManagerMenu(){
        while (true){
            managerView.displayMenu();
            int choice = managerView.getMenuChoice();

            switch (choice){
                case 1 -> {
                    System.out.println("Request processing ...");
                    ManagerCreditPendingDTO mangerCreditPendingDto = new ManagerCreditPendingDTO(loggedInUser);
                    creditController.creditPending(loggedInUser);
                }
                case 2 -> {
                    String creditId = managerView.askManagerRequestId();
                    System.out.println("request processing ...");
                    ManagerCreditApproveDTO managerCreditApproveDto = new ManagerCreditApproveDTO(creditId,loggedInUser);
                    creditController.approveCreditRequest(managerCreditApproveDto);
                }
                case 3 -> {
                    String creditId = managerView.askManagerRequestId();
                    System.out.println("request processing ...");
                    ManagerCreditApproveDTO managerCreditApproveDto = new ManagerCreditApproveDTO(creditId,loggedInUser);
                    creditController.rejectCreditRequest(managerCreditApproveDto);
                }
                case 4 -> {
                    System.out.println("request processing ...");
                    creditController.getAllCreditAccount(loggedInUser);
                }
                case 5 -> {
                    return;
                }
            }
        }
    }

    private void showTellerMenu(User loggedInUser) {
        while (true) {
            tellerView.displayMenu(loggedInUser);
            int choice = tellerView.getMenuChoice();

            switch (choice) {
                case 1 -> {
                    String firstName = tellerView.askClientFirstName();
                    String lastName = tellerView.askClientLastName();
                    String mounthlyIncome = tellerView.askClientMounthlyIncome();
                    String accountType = tellerView.askAccountType();
                    String initialBalance = tellerView.askInitialBalanceInput();
                    String currency = tellerView.askCurrency();

                    System.out.println("Requesting create account ...");
                    CreateAccountDTO createAccountDTO = new CreateAccountDTO(firstName,lastName,mounthlyIncome,accountType,initialBalance,currency);
                    accountController.createAccount(createAccountDTO,loggedInUser);
                }
                case 2 -> {
                    String clientIban = tellerView.askTellerIbanClient();
                    System.out.println("Requesting account ...");
                    ClientAccountsRequestDTO clientAccountRequest = new ClientAccountsRequestDTO(clientIban,loggedInUser);
                    accountController.clientAccountRequest(clientAccountRequest,loggedInUser);
                }
                case 3 -> {
                    String clientIban = tellerView.askTellerIbanClient();
                    System.out.println("Requesting account closure...");
                    ClientAccountCloseDTO clientAccountClose = new ClientAccountCloseDTO(clientIban,loggedInUser);
                    accountController.clientAccountClose(clientAccountClose,loggedInUser);
                }
                case 4 -> {
                    String clientIban = tellerView.askTellerIbanClient();
                    String amount = tellerView.askTellerAmountClient();
                    System.out.println("Processing deposit...");
                    ClientAccountDepositDTO clientAccountDeposit = new ClientAccountDepositDTO(clientIban, amount, loggedInUser);
                    accountController.clientAccountDeposit(clientAccountDeposit,loggedInUser);
                }
                case 5 -> {
                    String clientIban = tellerView.askTellerIbanClient();
                    String amount = tellerView.askTellerAmountClient();
                    System.out.println("Processing withdrawal...");
                    ClientAccountWithdrawalDTO clientAccountWithdrawal = new ClientAccountWithdrawalDTO(clientIban, amount , loggedInUser);
                    accountController.clientAccountWithdrawal(clientAccountWithdrawal,loggedInUser);
                }
                case 6 -> {
                    tellerView.askTellerClientTypeTransafer();
                    int choice2 = tellerView.getMenuChoice();

                    switch (choice2){
                        case 1 -> {
                            String sendClientIban = tellerView.askTellerIbanClient();
                            String destinationClientIban = tellerView.askDestinationAccountId();
                            String amountTransaction = tellerView.askTransactionAmount();
                            System.out.println("Processing transfer...");
                            ClientAccountTransferDTO clientTransferAccount = new ClientAccountTransferDTO(sendClientIban,destinationClientIban,amountTransaction,loggedInUser);
                            accountController.clientTransferAccount(clientTransferAccount,loggedInUser);
                        }
                        case 2 -> {

                        }
                    }
                }
                case 7 -> {
                    String clientIban = tellerView.askTellerIbanClient();
                    System.out.println("Showing transaction history...");
                    ClientAccountHistoryDTO clientAccountHistroy = new ClientAccountHistoryDTO(clientIban,loggedInUser);
                    accountController.clientAccountHistory(clientAccountHistroy,loggedInUser);
                }
                case 8 -> {
                    String nom = creditView.askTellerClientConfirmNom();
                    String prenom = creditView.askTellerClientConfirmPrenom();
                    String amount = creditView.askTellerClientAmountCredit();
                    String monthly_income = creditView.askTellerClientIncome();
                    String currency = creditView.askTellerClientCurrency();
                    String duration = creditView.askTellerClientTime();
                    String description = creditView.askTellerClientPurposeCredit();
                    CreateAccountDTO creatAccountdto = new CreateAccountDTO(prenom,nom,monthly_income,"CREDIT","0",currency);
                    CreditRequestDTO creditRequest = new CreditRequestDTO(nom,prenom,amount,monthly_income,currency,duration,description);
                    creditController.creditRequest(creditRequest,creatAccountdto,loggedInUser);
                }
                case 9 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int getValidChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}