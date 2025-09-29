package view;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TellerView {
    private Scanner scanner;

    public TellerView(){
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu(){
        System.out.println("\n=== TELLER MENU ===");
        System.out.println("Accounts:");
        System.out.println("1. Create account");
        System.out.println("2. List my accounts");
        System.out.println("3. Request account closure");
        System.out.println("Transactions:");
        System.out.println("4. Deposit");
        System.out.println("5. Withdraw");
        System.out.println("6. Transfer");
        System.out.println("7. Transaction history");
        System.out.println("Account:");
        System.out.println("8. Logout");
        System.out.print("Enter your choice: ");
    }

    public int getMenuChoice(){
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    public String askClientFirstName(){
        System.out.print("Enter client first name: ");
        String firstName = scanner.nextLine().trim();
        while (firstName.isEmpty()) {
            System.out.print("Client name cannot be empty. Please enter client name: ");
            firstName = scanner.nextLine().trim();
        }
        return firstName;
    }

    public String askClientLastName(){
        System.out.println("Enter client last name : ");
        String lastName = scanner.nextLine().trim();
        while (lastName.isEmpty()){
            System.out.println("Client name cannot be empty. Please enter client last name: ");
            lastName = scanner.nextLine().trim();
        }
        return lastName;
    }
    public String askAccountType(){
        System.out.print("Enter account type (Courant/Epargne/Credit): ");
        String type = scanner.nextLine().trim().toUpperCase();
        while (!type.equals("COURANT") && !type.equals("EPARGNE") && !type.equals("CREDIT")) {
            System.out.print("Invalid account type. Please enter (Courant/Epargne/Credit): ");
            type = scanner.nextLine().trim().toUpperCase();
        }
        return type;
    }

    public String askClientMounthlyIncome(){
        System.out.println("Enter mounthly income : ");
        return scanner.nextLine().trim();
    }

    public String askInitialBalanceInput() {
        System.out.print("Enter initial balance: ");
        return scanner.nextLine().trim();
    }

    public String askCurrency(){
        System.out.print("Enter currency (MAD/EUR/USD): ");
        String currency = scanner.nextLine().trim().toUpperCase();
        while (!currency.equals("MAD") && !currency.equals("EUR") && !currency.equals("USD")) {
            System.out.print("Invalid currency. Please enter (MAD/EUR/USD): ");
            currency = scanner.nextLine().trim().toUpperCase();
        }
        return currency;
    }

    public String askTellerAmountClient(){
        System.out.println("Enter amount : ");
        return scanner.nextLine();
    }

    public int askAccountId(){
        int id = -1;
        while (id <= 0) {
            System.out.print("Enter account ID: ");
            try {
                id = scanner.nextInt();
                scanner.nextLine();
                if (id <= 0) {
                    System.out.println("Account ID must be positive. Please enter a valid ID.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid account ID.");
                scanner.nextLine();
            }
        }
        return id;
    }

    public String askDestinationAccountId(){
        System.out.println("Enter Destination Account IBAN: ");
        return scanner.nextLine();
    }

    public String askTransactionAmount(){
        System.out.println("Enter Amount: ");
        return scanner.nextLine();
    }

    public String askTellerIbanClient(){
        System.out.println("Enter Account IBAN : ");
        return scanner.nextLine();
    }
    public void showMessage(String message){
        System.out.println(message);
    }

    public void showError(String errorMessage){
        System.err.println("ERROR: " + errorMessage);
    }

    public void showSuccess(String successMessage){
        System.out.println("SUCCESS: " + successMessage);
    }
}