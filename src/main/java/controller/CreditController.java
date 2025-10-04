package controller;

import domain.*;
import domain.Enums.CreditStatus;
import domain.Enums.Currency;
import dto.AccountDTO;
import dto.ClientDTO;
import dto.CreditReqDTO;
import dto.CreditRequestDTO;
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

    public void creditRequest(CreditRequestDTO creditRequestDto, User teller){
        try {
            validateCreditRequestDTO(creditRequestDto);

            ClientDTO client = clientService.findClientByNomAndPrenom(creditRequestDto.getNom() , creditRequestDto.getPrenom());

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

                System.out.println("  Request ID: " + request.getId());
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
}