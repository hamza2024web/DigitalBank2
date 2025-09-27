package dto;

public class CreateAccountDTO {
    private String clientName;
    private String accountType;
    private double initialeBalance;
    private String currency;

    public CreateAccountDTO(String clientName,String accountType,double initialeBalance,String currency){
        this.clientName = clientName;
        this.accountType = accountType;
        this.initialeBalance = initialeBalance;
        this.currency = currency;
    }

    public String getClientName(){
        return clientName;
    }

    public String getAccountType(){
        return accountType;
    }

    public Double getInitialeBalance(){
        return initialeBalance;
    }

    public String getCurrency(){
        return currency;
    }

    public void setClientName(String clientName){
        this.clientName = clientName;
    }

    public void setAccountType(String accountType){
        this.accountType = accountType;
    }

    public void setInitialeBalance(Double initialeBalance){
        this.initialeBalance = initialeBalance;
    }

    public void setCurrency(String currency){
        this.currency = currency;
    }
}
