package dto;

import domain.User;

public class ManagerCreditApproveDTO {
    private String creditId;
    private User manager;

    public ManagerCreditApproveDTO( String creditId , User manager) {
        this.manager = manager;
        this.creditId = creditId;
    }

    public String getCreditId() {
        return creditId;
    }

    public User getManager() {
        return manager;
    }
}
