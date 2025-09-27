package view;

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

    public String askClientName(){
        System.out.print("Enter client name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Client name cannot be empty. Please enter client name: ");
            name = scanner.nextLine().trim();
        }
        return name;
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

    public double askInitialBalance(){
        double balance = -1;
        while (balance < 0) {
            System.out.print("Enter initial balance: ");
            try {
                balance = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (balance < 0) {
                    System.out.println("Balance cannot be negative. Please enter a valid amount.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // consume invalid input
            }
        }
        return balance;
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

    public int askAccountId(){
        int id = -1;
        while (id <= 0) {
            System.out.print("Enter account ID: ");
            try {
                id = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (id <= 0) {
                    System.out.println("Account ID must be positive. Please enter a valid ID.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid account ID.");
                scanner.nextLine(); // consume invalid input
            }
        }
        return id;
    }

    public int askDestinationAccountId(){
        int id = -1;
        while (id <= 0) {
            System.out.print("Enter destination account ID: ");
            try {
                id = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (id <= 0) {
                    System.out.println("Destination account ID must be positive. Please enter a valid ID.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid destination account ID.");
                scanner.nextLine(); // consume invalid input
            }
        }
        return id;
    }

    public double askTransactionAmount(){
        double amount = -1;
        while (amount <= 0) {
            System.out.print("Enter amount: ");
            try {
                amount = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (amount <= 0) {
                    System.out.println("Amount must be positive. Please enter a valid amount.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.nextLine(); // consume invalid input
            }
        }
        return amount;
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