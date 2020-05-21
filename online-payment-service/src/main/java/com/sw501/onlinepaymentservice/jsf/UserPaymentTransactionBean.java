package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.TransactionService;
import com.sw501.onlinepaymentservice.entity.Payment;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class UserPaymentTransactionBean implements Serializable {
       
    @EJB
    TransactionService transaction_srv;
    
    String recipient_username;
    double amount;
    
    public UserPaymentTransactionBean() {
    }
    
    public String makePayment() {
        FacesContext context = FacesContext.getCurrentInstance();
        String sender_username = transaction_srv.getLoggedInUsername();
        if(transaction_srv.checkFunds(sender_username, amount)) {
            transaction_srv.makePayment(sender_username, recipient_username, amount);
            return "user";
        } else {
            context.addMessage(null, new FacesMessage("Not enough funds"));
        }
        return null;
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
    
    public List<Payment> getSentPayments() {
        return transaction_srv.viewSentPayments();
    }
    
    public List<Payment> getReceivedPayments() {
        return transaction_srv.viewReceivedPayments();
    }
    
}
