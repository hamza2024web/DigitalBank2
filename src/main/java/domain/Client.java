package domain;

public class Client {
    private Long id;
    private String nom;
    private String prenom;
    private Double revenueMensuel;
    private User utilisateur;

    public Client(String nom , String prenom , Double revenueMensuel , User utilisateur){
        this.nom = nom;
        this.prenom = prenom;
        this.revenueMensuel = revenueMensuel;
        this.utilisateur = utilisateur;
    }

    public Long getId(){
        return id;
    }

    public String getNom(){
        return nom;
    }

    public String getPrenom(){
        return prenom;
    }

    public Double getRevenueMensuel(){
        return revenueMensuel;
    }

    public User getUtilisateur(){
        return utilisateur;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public void SetPrenom(String prenom){
        this.prenom = prenom;
    }

    public void setRevenueMensuel(Double revenueMensuel){
        this.revenueMensuel = revenueMensuel;
    }

    public void setUtilisateur(User utlisateur){
        this.utilisateur = utilisateur;
    }

}
