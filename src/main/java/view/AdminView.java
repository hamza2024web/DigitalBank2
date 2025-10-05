package view;

import java.util.Scanner;

public class AdminView {
    private Scanner scanner;

    public AdminView(){
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu(){
        System.out.println("1. Account Management");
        System.out.println("2. Approve or reject a credit request");
        System.out.println("3. View all approved credit accounts");
        System.out.println("4. View pending credit requests");
        System.out.println("5. Audit logs (view teller operations)");
        System.out.println("6. Logout .");
        System.out.println("Enter Your Choice :");
    }

    public String askAdminReferenceId(){
        System.out.println("Enter Reference of Credit Request : ");
        return scanner.nextLine();
    }

    public String askAdminIbanAccount(){
        System.out.println("Enter The IBAN of Account :");
        return scanner.nextLine();
    }

    public String askAdminTellerEmail(){
        System.out.println("Enter the Teller Email : ");
        return scanner.nextLine();
    }
}
