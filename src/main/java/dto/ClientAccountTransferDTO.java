package dto;

import domain.User;

public class ClientAccountTransferDTO {
    private String sendClientIban;
    private String destinationClientIban;
    private String amountTransaction;
    private User teller;

    public ClientAccountTransferDTO(String sendClientIban , String destinationClientIban , String amountTransaction , User teller){
        this.sendClientIban = sendClientIban;
        this.destinationClientIban = destinationClientIban;
        this.amountTransaction = amountTransaction;
        this.teller = teller;
    }

    public String getSendClientIban(){
        return sendClientIban;
    }

    public String getDestinationClientIban(){
        return destinationClientIban;
    }

    public String getAmountTransaction(){
        return amountTransaction;
    }

    public User getTeller(){
        return teller;
    }
}
