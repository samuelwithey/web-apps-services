package com.sw501.onlinepaymentservice.ejb;

import com.sw501.onlinepaymentservice.entity.CurrencyType;
import com.sw501.onlinepaymentservice.entity.Payment;
import com.sw501.onlinepaymentservice.entity.Request;
import com.sw501.onlinepaymentservice.entity.SystemUser;
import com.sw501.onlinepaymentservice.entity.UserAccount;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@DeclareRoles({"users", "admins"})
@Stateless
public class Transaction {

    @PersistenceContext
    EntityManager em;
    
    @Resource
    SessionContext ctx;
    
    @Inject
    Converter converter;
    
    @RolesAllowed("users")
    public SystemUser getUser() {
        String username = ctx.getCallerPrincipal().getName();
        SystemUser user =(SystemUser) em.createNamedQuery("lookupUser").setParameter(1, username).getSingleResult();
        return user;
    }
    
    @RolesAllowed("users")
    public SystemUser getUser(String username) {
        SystemUser user =(SystemUser) em.createNamedQuery("lookupUser").setParameter(1, username).getSingleResult();
        return user;
    }
    
    @RolesAllowed("users")
    public UserAccount getUserAccount() {
        SystemUser user = getUser();
        return user.getUserAccount();
    }
    
    @RolesAllowed("users")
    public double getUserAccountBalance() {
        SystemUser user = getUser();
        return user.getUserAccount().getBalance();
    }
    
    @RolesAllowed("users")
    public String getUserAccountCurrencyType() {
        SystemUser user = getUser();
        return user.getUserAccount().getCurrency().getLabel();
    }
    
    @RolesAllowed("users")
    public List<Payment> viewSentPayments() {
        List<Payment> sent_payments = em.createNamedQuery("viewSentPayments").setParameter(1, getUser()).getResultList();
        return sent_payments;
    }
    
    @RolesAllowed("users")
    public List<Payment> viewReceivedPayments() {
        List<Payment> received_payments = em.createNamedQuery("viewReceivedPayments").setParameter(1, getUser()).getResultList();
        return received_payments;
    }
    
    @RolesAllowed("users")
    public List<Request> viewReceivedRequests() {
        List<Request> incoming_requests = em.createNamedQuery("viewReceivedRequests").setParameter(1, getUser()).getResultList();
        return incoming_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewPendingReceivedRequests() {
        List<Request> incoming_requests = em.createNamedQuery("viewPendingReceivedRequests").setParameter(1, getUser()).getResultList();
        return incoming_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewAcceptedReceivedRequests() {
        List<Request> incoming_requests = em.createNamedQuery("viewAcceptedReceivedRequests").setParameter(1, getUser()).getResultList();
        return incoming_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewSentRequests() {
        List<Request> outgoing_requests = em.createNamedQuery("viewSentRequests").setParameter(1, getUser()).getResultList();
        return outgoing_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewPendingSentRequests() {
        List<Request> outgoing_requests = em.createNamedQuery("viewPendingSentRequests").setParameter(1, getUser()).getResultList();
        return outgoing_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewAcceptedSentRequests() {
        List<Request> outgoing_requests = em.createNamedQuery("viewAcceptedSentRequests").setParameter(1, getUser()).getResultList();
        return outgoing_requests;
    }
    
    @RolesAllowed("admins")
    public List<Payment> viewAllPayments() {
        List<Payment> payments = em.createNamedQuery("viewAllPayments").getResultList();
        return payments;
    }
    
    @RolesAllowed("admins")
    public List<Request> viewAllRequests() {
        List<Request> requests = em.createNamedQuery("viewAllRequests").getResultList();
        return requests;
    }
    
    @RolesAllowed("users")
    public void makePayment(String username, double amount) {
        SystemUser sender = getUser();
        if(checkFunds(sender, amount)) {
            SystemUser recipient = getUser(username);
            double converted_currency_amount = converter.currencyConversion(sender.getUserAccount().getCurrency(), 
                    recipient.getUserAccount().getCurrency(), amount);
            Payment payment_transaction  = new Payment(converted_currency_amount, sender.getUserAccount(), recipient.getUserAccount());
            em.persist(payment_transaction);
            em.flush();
        } else {
            // log not enough funds
        }
    }
    
    @RolesAllowed("users")
    public void makeRequest(String username, double amount) {
        SystemUser sender = getUser();
        SystemUser recipient = getUser(username);
        double converted_currency_amount = converter.currencyConversion(sender.getUserAccount().getCurrency(), 
                    recipient.getUserAccount().getCurrency(), amount);
        Request request = new Request(converted_currency_amount, sender.getUserAccount(), recipient.getUserAccount());
        em.persist(request);
    }
    
    @RolesAllowed("users")
    public void respondToRequest() {
        
    }
    
    public boolean checkFunds(SystemUser user, double amount) {
        return (user.getUserAccount().getBalance() - amount) > 0;
    }
    
}
