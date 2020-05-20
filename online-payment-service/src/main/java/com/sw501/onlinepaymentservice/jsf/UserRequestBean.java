package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.TransactionService;
import com.sw501.onlinepaymentservice.entity.Request;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class UserRequestBean implements Serializable {
    
    
    @EJB
    TransactionService transaction_srv;
    
    String recipient_username;
    double amount;
    

    public UserRequestBean() {
    }
    
    public void makeRequest() {
        transaction_srv.makeRequest(recipient_username, amount);
    }

    public String getRecipient_username() {
        return recipient_username;
    }

    public void setRecipient_username(String recipient_username) {
        this.recipient_username = recipient_username;
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
    
    public List<Request> getReceivedAcceptedDeclinedRequests() {
        return transaction_srv.viewReceivedAcceptedDeclinedRequests();
    }
    
    public List<Request> getPendingSentRequests() {
        return transaction_srv.viewPendingSentRequests();
    }
    
    public List<Request> getSentAcceptedDeclinedReceivedRequests() {
        return transaction_srv.viewSentAcceptedDeclinedRequests();
    }
    
    public void acceptRequest(Request requestVar) {
        transaction_srv.acceptRequest(requestVar);
    }
    
    public void declineRequest(Request requestVar) {
        transaction_srv.declineRequest(requestVar);
    }
}
