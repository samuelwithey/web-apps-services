package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.TransactionService;
import com.sw501.onlinepaymentservice.entity.Payment;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class UserPaymentTransactionBean implements Serializable {
       
    @EJB
    TransactionService transaction_srv;
    
    String username;
    double amount;
    
    public UserPaymentTransactionBean() {
    }
    
    public void makePayment() {
        transaction_srv.makePayment(username, amount);
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
    
    public List<Payment> getSentPayments() {
        return transaction_srv.viewSentPayments();
    }
    
    public List<Payment> getReceivedPayments() {
        return transaction_srv.viewReceivedPayments();
    }
    
}
