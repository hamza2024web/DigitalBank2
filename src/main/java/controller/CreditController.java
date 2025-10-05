package controller;

import domain.*;
import domain.Enums.CreditStatus;
import domain.Enums.Currency;
import domain.Enums.Role;
import dto.*;
import mapper.CreditMapper;
import service.AccountService;
import service.ClientService;
import service.CreditService;
import view.CreditView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class CreditController {
    private final CreditService creditService;
    private final ClientService clientService;
    private final AccountService accountService;
    private final CreditView creditView;

    public CreditController (CreditService creditService, ClientService clientService, AccountService accountService, CreditView creditView){
        this.creditService = creditService;
        this.clientService = clientService;
        this.accountService = accountService;
        this.creditView = creditView;
    }

    public void creditRequest(CreditRequestDTO creditRequestDto,CreateAccountDTO creatAccountdto, User teller){
        try {
            validateCreditRequestDTO(creditRequestDto);

            ClientDTO client = clientService.findOrCreateClient(creatAccountdto);

            if (client == null){
                creditView.showError("Client not found with this last Name : " + creditRequestDto.getNom() + " " + creditRequestDto.getPrenom());
                return;
            }

            BigDecimal amount = new BigDecimal(creditRequestDto.getAmount());
            BigDecimal monthly_income = new BigDecimal(creditRequestDto.getMonthlyIncome());
            int dureeMois = Integer.parseInt(creditRequestDto.getDuration());

            if (amount.compareTo(BigDecimal.ZERO) <= 0){
                creditView.showError("The amount should be positive");
                return ;
            }

            if (dureeMois <= 0 || dureeMois > 360){
                creditView.showError("The Duree should be between 1 and 360 month");
                return ;
            }

            String requestId = UUID.randomUUID().toString();
            CreditReqDTO creditRequest = new CreditReqDTO(
                    requestId,
                    client,
                    amount,
                    monthly_income,
                    Currency.MAD,
                    dureeMois,
                    new BigDecimal(5),
                    creditRequestDto.getDescription(),
                    CreditStatus.PENDING,
                    LocalDate.now(),
                    teller.getEmail()
            );

            boolean success = creditService.createCreditRequest(creditRequest);

            if (success){
                creditView.showSuccess("Credit Application created successfully . By The Client : " + creditRequestDto.getNom() + " " + creditRequestDto.getPrenom());
                creditView.showCreditRequestDetails(creditRequest);
            } else {
                creditView.showError("Error creating Credit Application .");
            }
        } catch (NumberFormatException e){
            creditView.showError("Number Format Invalid : " + e.getMessage());
        } catch (Exception e){
            creditView.showError("Error statement Credit Application .");
        }

    }

    public void creditPending(User loggedInUser){
        List<CreditReqDTO> creditRequests = creditService.creditPending(loggedInUser);

        if (creditRequests.isEmpty()){
            System.out.println("No Credit Request Pending found.");
        } else {
            System.out.println("Pending Credit Requests :");
            for (CreditReqDTO request : creditRequests){
                ClientDTO client = request.getClient();

                System.out.println("─────────────────────────────────────────────────────────");
                System.out.println("Name of Client :" + client.getNom() + " " + client.getPrenom());
                System.out.println("Monthly Income : " + client.getRevenueMensuel());

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                System.out.println("  Request ID: " + request.getReferenceId());
                System.out.println("  Request Date: " + request.getRequestDate().format(dateFormatter));
                System.out.println("  Current Status: " + request.getStatus());
                System.out.println("---------------------------------------------------------");

                System.out.println("  Requested Amount: " + request.getMontant() + " " + request.getCurrency());
                System.out.println("  Duration: " + request.getDureeMois() + " months");
                System.out.println("  Annual Interest Rate: " + request.getTauxAnnuel() + " %");
                System.out.println("---------------------------------------------------------");

                System.out.println("  Description: " + request.getDescription());
                System.out.println("  Created by: " + request.getRequestedBy());
                System.out.println("---------------------------------------------------------\n");
            }
        }
    }

    public void approveCreditRequest(ManagerCreditApproveDTO managerCreditApproveDto){
        try {
            if (managerCreditApproveDto.getManager().getRole() != Role.MANAGER) {
                creditView.showError("Seul un manager peut approuver une demande de crédit");
                return;
            }

            CreditRequest request = creditService.findCreditRequestById(managerCreditApproveDto.getCreditId());
            if (request == null) {
                creditView.showError("Credit Application Not Found .");
                return;
            }

            if (request.getStatus() != CreditStatus.PENDING) {
                creditView.showError("This Application has Already traited .");
                return;
            }

            CreditAccount creditAccount = creditService.approveCreditRequest(request,managerCreditApproveDto);

            if (creditAccount != null) {
                creditView.showSuccess("Application Credit Approved , The Account created Successfully : " + creditAccount.getIban());
            } else {
                creditView.showError("Error during credit Approval");
            }

        } catch (Exception e) {
            creditView.showError("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void rejectCreditRequest(ManagerCreditApproveDTO managerCreditApproveDto) {
        try {
            if (managerCreditApproveDto.getManager().getRole() != Role.MANAGER) {
                creditView.showError("Only a manager can approve a credit application");
                return;
            }

            boolean success = creditService.rejectCreditRequest(managerCreditApproveDto);

            if (success) {
                creditView.showSuccess("Credit Request Rejected");
            } else {
                creditView.showError("Error rejecting request");
            }

        } catch (Exception e) {
            creditView.showError("Error: " + e.getMessage());
        }
    }

    public void getAllCreditAccount(User manager) {
        List<CreditAccount> accounts = creditService.getAllCreditAccount(manager);

        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            System.out.println("=== Credit Accounts ===");
            System.out.println("Total: " + accounts.size());
            System.out.println("─".repeat(80));

            for (CreditAccount account : accounts) {
                displayCreditAccount(account);
                System.out.println("─".repeat(80));
            }
        }
    }

    private void validateCreditRequestDTO(CreditRequestDTO creditRequestDto){
        if (creditRequestDto.getNom() == null || creditRequestDto.getNom().trim().isEmpty()){
            throw new IllegalArgumentException("The last name require");
        }
        if (creditRequestDto.getPrenom() == null || creditRequestDto.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("The first name require");
        }
        if (creditRequestDto.getAmount() == null || creditRequestDto.getAmount().trim().isEmpty()) {
            throw new IllegalArgumentException("the amount require");
        }
        if (creditRequestDto.getDuration() == null || creditRequestDto.getDuration().trim().isEmpty()) {
            throw new IllegalArgumentException("the duration require");
        }
        if (creditRequestDto.getMonthlyIncome() == null || creditRequestDto.getMonthlyIncome().trim().isEmpty()) {
            throw new IllegalArgumentException("Monthly income require");
        }
        if (creditRequestDto.getDescription() == null || creditRequestDto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("The description require");
        }
    }

    private void displayCreditAccount(CreditAccount account) {
        System.out.println("Account ID      : " + account.getId());
        System.out.println("IBAN            : " + account.getIban());
        System.out.println("Client          : " + account.getClient().getNom() + " " + account.getClient().getPrenom());
        System.out.println("Amount Requested : " + account.getMontantDemande() + " " + account.getDevise());
        System.out.println("Remaining Balance : " + account.getSoldeRestant() + " " + account.getDevise());
        System.out.println("Duration           : " + account.getDureeMois() + " Months");
        System.out.println("Annual Rate     : " + account.getTauxAnnuel() + "%");
        System.out.println("Statut          : " + account.getStatut());
        System.out.println("Date Request    : " + account.getDateDemande());
        System.out.println("Next Deadline: " + (account.getDateProchaineEcheance() != null ? account.getDateProchaineEcheance() : "N/A"));
        System.out.println("Active          : " + (account.getActive() ? "Yes" : "No"));

        if (account.getRelatedAccount() != null) {
            System.out.println("Related Accounts   : " + account.getRelatedAccount().getIban());
        }
    }
}