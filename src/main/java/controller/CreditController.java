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
                creditView.showError("Client not found with this last Name : " + creditRequestDto.getNom() + " + dto.getPrenom()");
                return;
            }

            AccountDTO relatedAccount = accountService.findAccountByIban(creditRequestDto.getIban());
            if (relatedAccount == null){
                creditView.showError("No Account found by this IBAN : " + creditRequestDto.getIban());
            }

            if (!relatedAccount.getClient().getId().equals(client.getId())){
                creditView.showError("The Iban provided not belonge to the specfied Client");
                return;
            }

            if (!relatedAccount.isActive()){
                creditView.showError("This Account is not Active");
            }

            BigDecimal amount = new BigDecimal(creditRequestDto.getAmount());
            int dureeMois = Integer.parseInt(creditRequestDto.getDuration());
            BigDecimal tauxAnnuel = new BigDecimal(creditRequestDto.getInterestRate());

            if (amount.compareTo(BigDecimal.ZERO) <= 0){
                creditView.showError("The amount should be positive");
                return ;
            }

            if (dureeMois <= 0 || dureeMois > 360){
                creditView.showError("The Duree should be between 1 and 360 month");
                return ;
            }

            if (tauxAnnuel.compareTo(BigDecimal.ZERO) < 0 || tauxAnnuel.compareTo(new BigDecimal("100")) > 0){
                creditView.showError("The interest Rate should be between 0 and 100%");
                return ;
            }

            String requestId = UUID.randomUUID().toString();
            CreditReqDTO creditRequest = new CreditReqDTO(
                    requestId,
                    client,
                    amount,
                    Currency.MAD,
                    dureeMois,
                    tauxAnnuel,
                    creditRequestDto.getDescription(),
                    CreditStatus.PENDING,
                    LocalDate.now(),
                    teller.getEmail()
            );

            boolean success = creditService.createCreditRequest(creditRequest);

            if (success){
                creditView.showSuccess("Credit Application created successfully . IBAN : " + creditRequestDto.getIban());
                creditView.showCreditRequestDetails(creditRequest);
            } else {
                creditView.showError("Error creating Credit Application .");
            }
        } catch (NumberFormatException e){
            creditView.showError("Number Format Invalid : " + e.getMessage());
        } catch (Exception e){
            creditView.showError("Error taitement Credit Applivation .");
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
        if (creditRequestDto.getInterestRate() == null || creditRequestDto.getInterestRate().trim().isEmpty()) {
            throw new IllegalArgumentException("Interest rate require");
        }
        if (creditRequestDto.getIban() == null || creditRequestDto.getIban().trim().isEmpty()) {
            throw new IllegalArgumentException("The iban require");
        }
        if (creditRequestDto.getDescription() == null || creditRequestDto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("The description require");
        }
    }
}