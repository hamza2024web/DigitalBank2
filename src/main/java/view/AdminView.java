package view;

import java.util.Scanner;

public class AdminView {
    private Scanner scanner;

    public AdminView(){
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu(){
        System.out.println("1. Account Management");
        System.out.println("2. Approve a credit request");
        System.out.println("3. Reject a credit request");
        System.out.println("4. View pending credit requests");
        System.out.println("5. View all approved credit accounts");
        System.out.println("6. Audit logs (view teller operations)");
        System.out.println("7. Logout .");
        System.out.println("Enter Your Choice :");
    }

    public String askAdminTellerEmail(){
        System.out.println("Enter the Teller Email : ");
        return scanner.nextLine();
    }
}
