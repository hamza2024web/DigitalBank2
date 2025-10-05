package view;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ManagerView {
    private Scanner scanner;

    public ManagerView(){
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu(){
        System.out.println("\n=== Manager MENU ===");
        System.out.println("1. View all pending credit applications");
        System.out.println("2. Validate or reject a credit request . Validate or Reject a request .");
        System.out.println("3. View existing credit accounts .");
        System.out.println("4. Decision History");
        System.out.println("Enter your choice: ");
    }

    public int getMenuChoice(){
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (InputMismatchException e){
            scanner.nextLine();
            return -1;
        }
    }
}
