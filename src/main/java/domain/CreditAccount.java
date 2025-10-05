package domain;

import domain.Enums.AccountCloseStatus;
import domain.Enums.AccountType;
import domain.Enums.CreditStatus;
import domain.Enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditAccount extends Account {
    private BigDecimal montantDemande;
    private int dureeMois;
    private BigDecimal tauxAnnuel;
    private CreditStatus statut;
    private BigDecimal soldeRestant;
    private LocalDate dateDemande;
    private LocalDate dateProchaineEcheance;

    public CreditAccount(String id, String iban, BigDecimal solde, Currency devise, LocalDate dateCreation, boolean isActive, Client client, AccountCloseStatus closeStatus, BigDecimal montantDemande, int dureeMois, BigDecimal tauxAnnuel, CreditStatus statut, BigDecimal soldeRestant, LocalDate dateDemande,
                         LocalDate dateProchaineEcheance) {

        super(id, iban, AccountType.CREDIT, solde, devise, dateCreation, isActive, client, closeStatus);

        this.montantDemande = montantDemande;
        this.dureeMois = dureeMois;
        this.tauxAnnuel = tauxAnnuel;
        this.statut = statut;
        this.soldeRestant = soldeRestant;
        this.dateDemande = dateDemande;
        this.dateProchaineEcheance = dateProchaineEcheance;
    }

    public CreditAccount(String id, String iban, Client client, BigDecimal montantDemande, int dureeMois,
                         BigDecimal tauxAnnuel) {
        super(id, iban, AccountType.CREDIT, BigDecimal.ZERO, Currency.MAD, LocalDate.now(), true, client, AccountCloseStatus.NONE);
        this.montantDemande = montantDemande;
        this.dureeMois = dureeMois;
        this.tauxAnnuel = tauxAnnuel;
        this.statut = CreditStatus.PENDING;
        this.soldeRestant = montantDemande;
        this.dateDemande = LocalDate.now();
        this.dateProchaineEcheance = LocalDate.now().plusMonths(1);
    }

    public BigDecimal getMontantDemande() {
        return montantDemande;
    }

    public int getDureeMois() {
        return dureeMois;
    }

    public BigDecimal getTauxAnnuel() {
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

    public void setMontantDemande(BigDecimal montantDemande) {
        this.montantDemande = montantDemande;
    }

    public void setDureeMois(int dureeMois) {
        this.dureeMois = dureeMois;
    }

    public void setTauxAnnuel(BigDecimal tauxAnnuel) {
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

}
