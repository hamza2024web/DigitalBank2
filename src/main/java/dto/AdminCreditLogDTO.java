package dto;

import domain.User;

public class AdminCreditLogDTO {
    private String email;
    private User admin;

    public AdminCreditLogDTO(String email, User admin){
        this.email = email;
        this.admin = admin;
    }

    public String getEmail(){
        return email;
    }

    public User getAdmin(){
        return admin;
    }
}
