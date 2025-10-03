package dto;

import domain.Enums.Currency;

public class CreditRequestDTO {
    private String nom;
    private String prenom;
    private String amount;
    private String monthly_income;
    private String currency;
    private String duration;
    private String description;

    public CreditRequestDTO(String nom , String prenom , String amount , String monthly_income,String currency, String duration , String description){
        this.nom = nom ;
        this.prenom = prenom ;
        this.amount = amount;
        this.monthly_income = monthly_income;
        this.currency = currency;
        this.duration = duration;
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

    public String getMonthlyIncome() {
        return monthly_income;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDuration() {
        return duration;
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

    public void setMonthly_income(String monthly_income) {
        this.monthly_income = monthly_income;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
