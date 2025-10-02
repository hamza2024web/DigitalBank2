package controller;

import domain.User;
import dto.CreditRequestDTO;
import service.ClientService;
import service.CreditService;

public class CreditController {
    private final CreditService creditService;
    private final ClientService clientService;

    public CreditController (CreditService creditService, ClientService clientService){
        this.creditService = creditService;
        this.clientService = clientService;
    }

    public void creditRequest(CreditRequestDTO creditRequest, User teller){

    }
}