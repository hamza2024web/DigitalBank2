package dto;

import domain.User;

public class AdminCreditDTO {
    private User admin;

    public AdminCreditDTO(User admin){
        this.admin = admin;
    }

    public User getAdmin(){
        return admin;
    }
}
