package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.TransactionService;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class UserRequestBean implements Serializable {
    
    
    @EJB
    TransactionService transaction_srv;
    
    String username;
    double amount;
    

    public UserRequestBean() {
    }
    
    public void makeRequest() {
        transaction_srv.makeRequest(username, amount);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}