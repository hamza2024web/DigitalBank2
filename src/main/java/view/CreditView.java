package view;

import dto.ClientDTO;
import dto.CreditReqDTO;

import java.time.format.DateTimeFormatter;
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

    public String askTellerClientCurrency(){
        System.out.print("Enter currency (MAD/EUR/USD): ");
        String currency = scanner.nextLine().trim().toUpperCase();
        while (!currency.equals("MAD") && !currency.equals("EUR") && !currency.equals("USD")) {
            System.out.print("Invalid currency. Please enter (MAD/EUR/USD): ");
            currency = scanner.nextLine().trim().toUpperCase();
        }
        return currency;
    }
    public String askTellerClientTime(){
        System.out.println("What is the desired repayment period (in months)?");
        return scanner.nextLine();
    }

    public String askTellerClientIncome(){
        System.out.println("How mush your monthly income : ");
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

    public void showCreditRequestDetails(CreditReqDTO creditRequest) {
        if (creditRequest == null) {
            showError("Cannot display details: the credit request is null.");
            return;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("\n✅ SUCCESS: The credit request has been created with the following details:");
        System.out.println("---------------------------------------------------------");

        System.out.println("  Request ID: " + creditRequest.getId());
        System.out.println("  Request Date: " + creditRequest.getRequestDate().format(dateFormatter));
        System.out.println("  Current Status: " + creditRequest.getStatus());
        System.out.println("---------------------------------------------------------");

        ClientDTO client = creditRequest.getClient();

        if (client != null) {
            System.out.println("  Client: " + client.getPrenom() + " " + client.getNom());
        }

        System.out.println("  Requested Amount: " + creditRequest.getMontant() + " " + creditRequest.getCurrency());
        System.out.println("  Duration: " + creditRequest.getDureeMois() + " months");
        System.out.println("  Annual Interest Rate: " + creditRequest.getTauxAnnuel() + " %");
        System.out.println("---------------------------------------------------------");

        System.out.println("  Description: " + creditRequest.getDescription());
        System.out.println("  Created by: " + creditRequest.getRequestedBy());
        System.out.println("---------------------------------------------------------\n");
    }
}
