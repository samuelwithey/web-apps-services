package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.TransactionService;
import com.sw501.onlinepaymentservice.entity.Payment;
import com.sw501.onlinepaymentservice.entity.Request;
import java.util.List;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AdminPaymentTransactionBean implements Serializable {
    
    
    @EJB
    TransactionService transaction;
    

    public AdminPaymentTransactionBean() {
    }
    
    public List<Payment> getAllPayments() {
        return transaction.viewAllPayments();
    }
    
}
