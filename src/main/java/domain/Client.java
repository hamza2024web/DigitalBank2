package domain;

import java.math.BigDecimal;

public class Client {
    private Long id;
    private String nom;
    private String prenom;
    private BigDecimal revenueMensuel;

    public Client(String nom , String prenom , BigDecimal revenueMensuel){
        this.nom = nom;
        this.prenom = prenom;
        this.revenueMensuel = revenueMensuel;
    }

    public long getId(){
        return id;
    }

    public String getNom(){
        return nom;
    }

    public String getPrenom(){
        return prenom;
    }

    public BigDecimal getRevenueMensuel(){
        return revenueMensuel;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public void SetPrenom(String prenom){
        this.prenom = prenom;
    }

    public void setRevenueMensuel(BigDecimal revenueMensuel){
        this.revenueMensuel = revenueMensuel;
    }

}
