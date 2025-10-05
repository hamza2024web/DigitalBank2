package dto;

import domain.Enums.CreditStatus;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;


public class CreditReqDTO {
    private String id;
    private ClientDTO client;
    private BigDecimal montant;
    private BigDecimal monthly_income;
    private Currency currency;
    private int dureeMois;
    private BigDecimal tauxAnnuel;
    private String description;
    private CreditStatus status;
    private LocalDate requestDate;
    private String requestedBy;

    public CreditReqDTO() {
    }

    public CreditReqDTO(String id, ClientDTO client, BigDecimal montant, BigDecimal monthly_income,Currency currency, int dureeMois, BigDecimal tauxAnnuel, String description, CreditStatus status, LocalDate requestDate, String requestedBy) {
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


    public String getReferenceId() {
        return id;
    }

    public void setReferenceId(String id) {
        this.id = id;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public BigDecimal getMonthlyIncome() {
        return monthly_income;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getDureeMois() {
        return dureeMois;
    }

    public void setDureeMois(int dureeMois) {
        this.dureeMois = dureeMois;
    }

    public BigDecimal getTauxAnnuel() {
        return tauxAnnuel;
    }

    public void setTauxAnnuel(BigDecimal tauxAnnuel) {
        this.tauxAnnuel = tauxAnnuel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
}