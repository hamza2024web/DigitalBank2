package view;

import java.util.Scanner;

public class CreditView {
    private Scanner scanner;

    public CreditView (){
        this.scanner = new Scanner(System.in);
    }

    public String askTellerClientConfirmNom(){
        System.out.println("Can you confirm your last name?");
        return scanner.nextLine();
    }

    public String askTellerClientConfirmPrenom(){
        System.out.println("Can you confirm your first name?");
        return scanner.nextLine();
    }

    public String askTellerClientAmountCredit(){
        System.out.println("What amount of credit would you like to request?");
        return scanner.nextLine();
    }

    public String askTellerClientTime(){
        System.out.println("What is the desired repayment period (in months)?");
        return scanner.nextLine();
    }

    public String askTellerClientRate(){
        System.out.println("What is the annual interest rate applicable for this type of credit?");
        return scanner.nextLine();
    }

    public String askTellerClientIbanCredit(){
        System.out.println("Which existing account should the credit amount be deposited into? (IBAN)");
        return scanner.nextLine();
    }

    public String askTellerClientPurposeCredit(){
        System.out.println("Can you provide a description or purpose of the credit?");
        return scanner.nextLine();
    }

    public void showError(String s){
        System.out.println("ERROR : " + s);
    }

    public void showSuccess(String s){
        System.out.println("Success : " + s);
    }
}
