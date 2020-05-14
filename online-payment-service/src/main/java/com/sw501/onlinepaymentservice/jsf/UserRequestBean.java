package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.TransactionService;
import com.sw501.onlinepaymentservice.entity.Request;
import java.io.Serializable;
import java.util.List;
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
    
    public List<Request> getPendingReceivedRequests() {
        return transaction_srv.viewPendingReceivedRequests();
    }
    
    public void acceptRequest(Request request) {
        transaction_srv.acceptRequest(request);
    }
    
    public void declineRequest(Request request) {
        transaction_srv.declineRequest(request);
    }
}
