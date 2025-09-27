package domain;

import domain.Enums.AccountType;
import domain.Enums.CreditStatus;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditAccount extends Account {
    private BigDecimal montantDemande;
    private int dureeMois;
    private Double tauxAnnuel;
    private CreditStatus statut;
    private BigDecimal soldeRestant;
    private LocalDate dateDemande;
    private LocalDate dateProchaineEcheance;
    private Account relatedAccount; // Account linked to this credit

    // Constructor with proper name and parent constructor call
    public CreditAccount(String id, String iban, AccountType type, BigDecimal solde,
                         Currency devise, LocalDate dateCreation, boolean isActive, Client client,
                         BigDecimal montantDemande, int dureeMois, Double tauxAnnuel,
                         CreditStatus statut, BigDecimal soldeRestant, LocalDate dateDemande,
                         LocalDate dateProchaineEcheance, Account relatedAccount) {

        // Call parent constructor
        super(id, iban, type, solde, devise, dateCreation, isActive, client);

        // Initialize credit-specific fields
        this.montantDemande = montantDemande;
        this.dureeMois = dureeMois;
        this.tauxAnnuel = tauxAnnuel;
        this.statut = statut;
        this.soldeRestant = soldeRestant;
        this.dateDemande = dateDemande;
        this.dateProchaineEcheance = dateProchaineEcheance;
        this.relatedAccount = relatedAccount;
    }

    // Simplified constructor for creating new credit requests
    public CreditAccount(String id, String iban, Client client,
                         BigDecimal montantDemande, int dureeMois, Double tauxAnnuel,
                         Account relatedAccount) {

        // Call parent constructor with default values
        super(id, iban, AccountType.CREDIT, BigDecimal.ZERO,
                Currency.MAD, LocalDate.now(), true, client);

        // Initialize credit-specific fields
        this.montantDemande = montantDemande;
        this.dureeMois = dureeMois;
        this.tauxAnnuel = tauxAnnuel;
        this.statut = CreditStatus.PENDING; // Default status
        this.soldeRestant = montantDemande;
        this.dateDemande = LocalDate.now();
        this.dateProchaineEcheance = LocalDate.now().plusMonths(1);
        this.relatedAccount = relatedAccount;
    }

    public BigDecimal getMontantDemande() {
        return montantDemande;
    }
    public int getDureeMois() {
        return dureeMois;
    }
    public Double getTauxAnnuel() {
        return tauxAnnuel;
    }
    public CreditStatus getStatut() {
        return statut;
    }
    public BigDecimal getSoldeRestant() {
        return soldeRestant;
    }
    public LocalDate getDateDemande() {
        return dateDemande;
    }
    public LocalDate getDateProchaineEcheance() {
        return dateProchaineEcheance;
    }
    public Account getRelatedAccount() {
        return relatedAccount;
    }

    public void setMontantDemande(BigDecimal montantDemande) {
        this.montantDemande = montantDemande;
    }
    public void setDureeMois(int dureeMois) {
        this.dureeMois = dureeMois;
    }
    public void setTauxAnnuel(Double tauxAnnuel) {
        this.tauxAnnuel = tauxAnnuel;
    }
    public void setStatut(CreditStatus statut) {
        this.statut = statut;
    }
    public void setSoldeRestant(BigDecimal soldeRestant) {
        this.soldeRestant = soldeRestant;
    }
    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }
    public void setDateProchaineEcheance(LocalDate dateProchaineEcheance) {
        this.dateProchaineEcheance = dateProchaineEcheance;
    }
    public void setRelatedAccount(Account relatedAccount) {
        this.relatedAccount = relatedAccount;
    }

}