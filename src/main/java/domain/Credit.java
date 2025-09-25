package domain;

import domain.Enums.CreditStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Credit {
    private Long id;
    private BigDecimal montantDemande;
    private int dureeMois;
    private Double tauxAnnuel;
    private CreditStatus statut;
    private BigDecimal soldeRestant;
    private LocalDate dateDemande;
    private LocalDate dateProchaineEcheance;
    private Account relatedAccount;
    private Client client;

    public Credit(Long id, BigDecimal montantDemande, int dureeMois, Double tauxAnnuel, CreditStatus statut, BigDecimal soldeRestant, LocalDate dateDemande, LocalDate dateProchaineEcheance, Account relatedAccount, Client client) {
        this.id = id;
        this.montantDemande = montantDemande;
        this.dureeMois = dureeMois;
        this.tauxAnnuel = tauxAnnuel;
        this.statut = statut;
        this.soldeRestant = soldeRestant;
        this.dateDemande = dateDemande;
        this.dateProchaineEcheance = dateProchaineEcheance;
        this.relatedAccount = relatedAccount;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontantDemande() {
        return montantDemande;
    }

    public void setMontantDemande(BigDecimal montantDemande) {
        this.montantDemande = montantDemande;
    }

    public int getDureeMois() {
        return dureeMois;
    }

    public void setDureeMois(int dureeMois) {
        this.dureeMois = dureeMois;
    }

    public Double getTauxAnnuel() {
        return tauxAnnuel;
    }

    public void setTauxAnnuel(Double tauxAnnuel) {
        this.tauxAnnuel = tauxAnnuel;
    }

    public CreditStatus getStatut() {
        return statut;
    }

    public void setStatut(CreditStatus statut) {
        this.statut = statut;
    }

    public BigDecimal getSoldeRestant() {
        return soldeRestant;
    }

    public void setSoldeRestant(BigDecimal soldeRestant) {
        this.soldeRestant = soldeRestant;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public LocalDate getDateProchaineEcheance() {
        return dateProchaineEcheance;
    }

    public void setDateProchaineEcheance(LocalDate dateProchaineEcheance) {
        this.dateProchaineEcheance = dateProchaineEcheance;
    }

    public Account getRelatedAccount() {
        return relatedAccount;
    }

    public void setRelatedAccount(Account relatedAccount) {
        this.relatedAccount = relatedAccount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
