package com.sw501.onlinepaymentservice.ejb;

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
public class TransactionService {

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
        List<Payment> sent_payments = em.createNamedQuery("viewSentPayments").setParameter(1, getUser().getUserAccount()).getResultList();
        return sent_payments;
    }
    
    @RolesAllowed("users")
    public List<Payment> viewReceivedPayments() {
        List<Payment> received_payments = em.createNamedQuery("viewReceivedPayments").setParameter(1, getUser().getUserAccount()).getResultList();
        return received_payments;
    }
    
    @RolesAllowed("users")
    public List<Request> viewReceivedRequests() {
        List<Request> incoming_requests = em.createNamedQuery("viewReceivedRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return incoming_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewPendingReceivedRequests() {
        List<Request> pendingReceivedRequests = em.createNamedQuery("viewPendingReceivedRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return pendingReceivedRequests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewAcceptedReceivedRequests() {
        List<Request> acceptedReceivedRequests = em.createNamedQuery("viewAcceptedReceivedRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return acceptedReceivedRequests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewSentRequests() {
        List<Request> sentRequests = em.createNamedQuery("viewSentRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return sentRequests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewPendingSentRequests() {
        List<Request> pendingSentRequests = em.createNamedQuery("viewPendingSentRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return pendingSentRequests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewAcceptedSentRequests() {
        List<Request> acceptedSentRequests = em.createNamedQuery("viewAcceptedSentRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return acceptedSentRequests;
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
        if(checkFunds(sender.getUserAccount(), amount)) {
            SystemUser recipient = getUser(username);
            double converted_currency_amount = converter.currencyConversion(sender.getUserAccount().getCurrency(), 
                    recipient.getUserAccount().getCurrency(), amount);
            Payment payment_transaction  = new Payment(converted_currency_amount, sender.getUserAccount(), recipient.getUserAccount());
            sender.getUserAccount().setBalance(sender.getUserAccount().getBalance() - amount);
            recipient.getUserAccount().setBalance(recipient.getUserAccount().getBalance() + converted_currency_amount);
            em.persist(payment_transaction);
            em.persist(sender);
            em.persist(recipient);
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
    public void acceptRequest(Request request) {
        if(checkFunds(request.getRecipient().getUser().getUserAccount(), request.getAmount())) {
            request.setPending(false);
            request.setAccepted(Boolean.TRUE);
            em.persist(request);
            makePayment(request.getSender().getUser().getUsername(), request.getAmount());
        } else {
            // log that user does not have enough funds to accept the request
        }
        
    }
    
    @RolesAllowed("users")
    public void declineRequest(Request request) {
        request.setPending(false);
        request.setAccepted(Boolean.FALSE);
        em.persist(request);
    }
    
    public boolean checkFunds(UserAccount userAccount, double amount) {
        return (userAccount.getBalance() - amount) >= 0;
    }
    
}
