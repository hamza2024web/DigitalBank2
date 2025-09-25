package dto;

import domain.Enums.Role;

public class LoginRequestDTO {
    private String email;
    private Role role;

    public LoginRequestDTO(String email , Role role){
        this.email = email;
        this.role = role;
    }

    public String getEmail(){
        return email;
    }

    public Role getRole(){
        return role;
    }

}
