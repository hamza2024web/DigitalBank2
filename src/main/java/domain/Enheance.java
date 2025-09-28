package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Enheance {
    private Long id;
    private BigDecimal montant;
    private LocalDate dateEcheance;
    private boolean estPayee;
    private CreditAccount credit;

    public Enheance(Long id, BigDecimal montant, LocalDate dateEcheance, boolean estPayee, CreditAccount credit) {
        this.id = id;
        this.montant = montant;
        this.dateEcheance = dateEcheance;
        this.estPayee = estPayee;
        this.credit = credit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public boolean isEstPayee() {
        return estPayee;
    }

    public void setEstPayee(boolean estPayee) {
        this.estPayee = estPayee;
    }

    public CreditAccount getCredit() {
        return credit;
    }

    public void setCredit(CreditAccount credit) {
        this.credit = credit;
    }
}
