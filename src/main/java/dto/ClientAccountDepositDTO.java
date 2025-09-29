package dto;

import domain.User;

public class ClientAccountDepositDTO {
    private String clientIban;
    private String amount;
    private User teller;

    public ClientAccountDepositDTO(String clientIban , String amount ,User teller){
        this.clientIban = clientIban;
        this.amount = amount;
        this.teller = teller;
    }

    public String getClientIban(){
        return clientIban;
    }

    public String getAmount(){
        return amount;
    }

    public User getTeller(){
        return teller;
    }
}
