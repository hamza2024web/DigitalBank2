package domain;

import domain.Enums.Role;

public class User {
    private Long id;
    private String nomUtilisateur;
    private String motDePasseHash;
    private Role role;

    public User(String nomUtilisateur , String motDePasseHash , Role role){
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNomUtilisateur(){
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur){
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMotDePasseHash(){
        return motDePasseHash;
    }

    public void SetMotDePasseHash(String motDePasseHash){
        this.motDePasseHash = motDePasseHash;
    }

    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }
}
