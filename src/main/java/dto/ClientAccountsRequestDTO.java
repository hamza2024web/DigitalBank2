package dto;

import domain.User;

public class ClientAccountsRequestDTO {
    private String clientIban;
    private User teller;

    public ClientAccountsRequestDTO(String clientIban , User teller){
        this.clientIban = clientIban;
        this.teller = teller;
    }

    public String getClientIban(){
        return clientIban;
    }

    public User getTeller (){
        return teller;
    }
}
