package dto;

import domain.User;

public class ClientAccountCloseDTO {
    private String clientIban;
    private User teller;

    public ClientAccountCloseDTO(String clientIban , User teller){
        this.clientIban = clientIban;
        this.teller = teller;
    }

    public String getClientIban (){
        return clientIban;
    }

    public User getTeller (){
        return teller;
    }
}
