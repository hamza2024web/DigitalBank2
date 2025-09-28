package view;

import controller.AccountController;
import controller.AuthController;
import domain.User;
import dto.CreateAccountDTO;
import dto.UserDTO;

import java.util.Scanner;

public class MenuNavigator {
    private final AuthController authController;
    private final TellerView tellerView;
    private final AccountController accountController;

    private final Scanner scanner = new Scanner(System.in);
    private final User loggedInUser;

    public MenuNavigator(AuthController authController, TellerView tellerView , AccountController accountController , User loggedInUser){
        this.authController = authController;
        this.tellerView = tellerView;
        this.accountController = accountController;
        this.loggedInUser = loggedInUser;
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
                    authController.login();
                    UserDTO userDTO = authController.getCurrentUserDTO();
                    if (userDTO != null){
                        handleRoleBasedMenu(userDTO);
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
            case TELLER -> showTellerMenu();
        }
    }

    private void showAdminMenu(){
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Manage Users");
            System.out.println("2. View Reports");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");

            int choice = getValidChoice();

            switch (choice) {
                case 1 -> {
                    System.out.println("Managing users...");
                    // Add admin user management logic here
                }
                case 2 -> {
                    System.out.println("Viewing reports...");
                    // Add reports logic here
                }
                case 3 -> {
                    return; // Back to main menu
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showManagerMenu(){
        while (true) {
            System.out.println("\n=== MANAGER MENU ===");
            System.out.println("1. Approve Transaction");
            System.out.println("2. View Teller Performance");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");

            int choice = getValidChoice();

            switch (choice) {
                case 1 -> {
                    System.out.println("Approving transactions...");
                    // Add transaction approval logic here
                }
                case 2 -> {
                    System.out.println("Viewing teller performance...");
                    // Add performance viewing logic here
                }
                case 3 -> {
                    return; // Back to main menu
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showTellerMenu() {
        while (true) {
            tellerView.displayMenu();
            int choice = tellerView.getMenuChoice();

            switch (choice) {
                case 1 -> {
                    String firstName = tellerView.askClientFirstName();
                    String lastName = tellerView.askClientLastName();
                    String mounthlyIncome = tellerView.askClientMounthlyIncome();
                    String accountType = tellerView.askAccountType();
                    String initialBalance = tellerView.askInitialBalanceInput();
                    String currency = tellerView.askCurrency();

                    CreateAccountDTO createAccountDTO = new CreateAccountDTO(firstName,lastName,mounthlyIncome,accountType,initialBalance,currency);
                    accountController.createAccount(createAccountDTO,loggedInUser);
                }
                case 2 -> {
                    System.out.println("Listing accounts...");
                }
                case 3 -> {
                    System.out.println("Requesting account closure...");
                }
                case 4 -> {
                    System.out.println("Processing deposit...");
                }
                case 5 -> {
                    System.out.println("Processing withdrawal...");
                }
                case 6 -> {
                    System.out.println("Processing transfer...");
                }
                case 7 -> {
                    System.out.println("Showing transaction history...");
                }
                case 8 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int getValidChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return choice;
        } catch (Exception e) {
            scanner.nextLine(); // consume invalid input
            return -1; // invalid choice
        }
    }
}