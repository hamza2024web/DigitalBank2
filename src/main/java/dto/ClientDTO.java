package dto;

import java.math.BigDecimal;

public class ClientDTO {
    private Long id;
    private String nom;
    private String prenom;
    private BigDecimal revenueMensuel;

    public ClientDTO() {
    }

    public ClientDTO(Long id, String nom, String prenom, BigDecimal revenueMensuel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.revenueMensuel = revenueMensuel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public BigDecimal getRevenueMensuel() {
        return revenueMensuel;
    }

    public void setRevenueMensuel(BigDecimal revenueMensuel) {
        this.revenueMensuel = revenueMensuel;
    }
}