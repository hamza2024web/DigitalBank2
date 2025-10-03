package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditSchedule {
    private Long id;
    private CreditAccount creditAccount;
    private LocalDate dueDate;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal totalPayment;
    private BigDecimal remainingBalance;

    public CreditSchedule(Long id, CreditAccount creditAccount, LocalDate dueDate, BigDecimal principal, BigDecimal interest,
                          BigDecimal totalPayment, BigDecimal remainingBalance) {
        this.id = id;
        this.creditAccount = creditAccount;
        this.dueDate = dueDate;
        this.principal = principal;
        this.interest = interest;
        this.totalPayment = totalPayment;
        this.remainingBalance = remainingBalance;
    }

    public Long getId() {
        return id;
    }

    public CreditAccount getCreditAccount() {
        return creditAccount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreditAccount(CreditAccount creditAccount) {
        this.creditAccount = creditAccount;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
}
