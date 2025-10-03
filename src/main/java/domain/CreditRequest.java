package domain;

import domain.Enums.Currency;
import domain.Enums.CreditStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditRequest {
    private String id;
    private Client client;
    private BigDecimal montant;
    private BigDecimal monthly_income;
    private Currency currency;
    private int dureeMois;
    private BigDecimal tauxAnnuel;
    private String description;
    private CreditStatus status;
    private LocalDate requestDate;
    private String requestedBy;

    public CreditRequest(String id, Client client, BigDecimal montant, BigDecimal monthly_income,Currency currency, int dureeMois, BigDecimal tauxAnnuel, String description,
                         CreditStatus status, LocalDate requestDate, String requestedBy) {
        this.id = id;
        this.client = client;
        this.montant = montant;
        this.monthly_income = monthly_income;
        this.currency = currency;
        this.dureeMois = dureeMois;
        this.tauxAnnuel = tauxAnnuel;
        this.description = description;
        this.status = status;
        this.requestDate = requestDate;
        this.requestedBy = requestedBy;
    }

    public String getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public BigDecimal getMonthlyIncome() {
        return monthly_income;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getDureeMois() {
        return dureeMois;
    }

    public BigDecimal getTauxAnnuel() {
        return tauxAnnuel;
    }

    public String getDescription() {
        return description;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setMonthlyIncome(BigDecimal monthly_income) {
        this.monthly_income = monthly_income;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setDureeMois(int dureeMois) {
        this.dureeMois = dureeMois;
    }

    public void setTauxAnnuel(BigDecimal tauxAnnuel) {
        this.tauxAnnuel = tauxAnnuel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
}
