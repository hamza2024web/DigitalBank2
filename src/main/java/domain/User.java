package domain;

import domain.Enums.Role;

public class User {
    private Long id;
    private String email;
    private String password;
    private Role role;

    public User(String email , String password , Role role){
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String email) {
        this.email = email;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getMotDePasseHash(){
        return password;
    }

    public void setMotDePasseHash(String motDePasseHash){
        this.password = motDePasseHash;
    }

    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }
}
