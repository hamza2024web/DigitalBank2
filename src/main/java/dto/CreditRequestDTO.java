package dto;

public class CreditRequestDTO {
    private String nom;
    private String prenom;
    private String amount;
    private String duration;
    private String interestRate;
    private String iban;
    private String description;

    public CreditRequestDTO(String nom , String prenom , String amount , String duration , String interestRate , String description){
        this.nom = nom ;
        this.prenom = prenom ;
        this.amount = amount;
        this.duration = duration;
        this.interestRate = interestRate;
        this.description = description;
    }

    public String getNom(){
        return nom ;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAmount() {
        return amount;
    }

    public String getDuration() {
        return duration;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public String getDescription() {
        return description;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
