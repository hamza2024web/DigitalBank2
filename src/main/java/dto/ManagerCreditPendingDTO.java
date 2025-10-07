package dto;

import domain.User;

public class ManagerCreditPendingDTO {
    private User manager;

    public ManagerCreditPendingDTO (User manager){
        this.manager = manager;
    }

    public User getUser(){
        return manager;
    }
}
