package domain;

public class Client {
    private Long id;
    private String nom;
    private String prenom;
    private Double revenueMensuel;

    public Client(String nom , String prenom , Double revenueMensuel){
        this.nom = nom;
        this.prenom = prenom;
        this.revenueMensuel = revenueMensuel;
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

    public void setNom(String nom){
        this.nom = nom;
    }

    public void SetPrenom(String prenom){
        this.prenom = prenom;
    }

    public void setRevenueMensuel(Double revenueMensuel){
        this.revenueMensuel = revenueMensuel;
    }

}
