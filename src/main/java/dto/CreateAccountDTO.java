package dto;

public class CreateAccountDTO {
    private String firstName;
    private String lastName;
    private String monthlyIncome;
    private String accountType;
    private String initialBalance;
    private String currency;

    public CreateAccountDTO(String firstName, String lastName, String monthlyIncome, String accountType, String initialBalance, String currency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.monthlyIncome = monthlyIncome;
        this.accountType = accountType;
        this.initialBalance = initialBalance;
        this.currency = currency;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getMonthlyIncome() {
        return monthlyIncome;
    }
    public String getAccountType() {
        return accountType;
    }
    public String getInitialBalance() {
        return initialBalance;
    }
    public String getCurrency() {
        return currency;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public void setInitialBalance(String initialBalance) {
        this.initialBalance = initialBalance;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
