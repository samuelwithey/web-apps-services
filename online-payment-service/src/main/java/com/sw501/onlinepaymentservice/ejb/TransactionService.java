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
    public String getLoggedInUsername() {
        return ctx.getCallerPrincipal().getName();
    }
    
    @RolesAllowed("users")
    public SystemUser getUser() {
        String username = ctx.getCallerPrincipal().getName();
        SystemUser user =(SystemUser) em.createNamedQuery("lookupUser").setParameter(1, username).getSingleResult();
        return user;
    }
    
    @RolesAllowed("users")
    public SystemUser getUser(String username) {
        SystemUser user =(SystemUser) em.createNamedQuery("lookupUser").setParameter(1, username).getSingleResult();
        System.out.println("Found User");
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
    public Request getRequest(long id) {
        Request request = (Request) em.createNamedQuery("getRequest").setParameter(1, id).getSingleResult();
        return request;
    }
    
    @RolesAllowed("users")
    public List<Request> viewPendingReceivedRequests() {
        List<Request> pendingReceivedRequests = em.createNamedQuery("viewPendingReceivedRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return pendingReceivedRequests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewPendingSentRequests() {
        List<Request> pendingSentRequests = em.createNamedQuery("viewPendingSentRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return pendingSentRequests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewReceivedAcceptedDeclinedRequests() {
        List<Request> requests = em.createNamedQuery("viewReceivedAcceptedDeclinedRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewSentAcceptedDeclinedRequests() {
        List<Request> requests = em.createNamedQuery("viewSentAcceptedDeclinedRequests").setParameter(1, getUser().getUserAccount()).getResultList();
        return requests;
    }
    
    @RolesAllowed("users")
    public void makePayment(String sender, String recipient, double amount) {
        SystemUser sender_user = getUser(sender);
        SystemUser recipient_user = getUser(recipient);
        if(checkFunds(sender_user.getUserAccount(), amount)) {
            double converted_currency_amount = converter.currencyConversion(sender_user.getUserAccount().getCurrency(), 
                    recipient_user.getUserAccount().getCurrency(), amount);
            Payment payment_transaction  = new Payment(converted_currency_amount, sender_user.getUserAccount(), recipient_user.getUserAccount());
            sender_user.getUserAccount().setBalance(sender_user.getUserAccount().getBalance() - amount);
            recipient_user.getUserAccount().setBalance(recipient_user.getUserAccount().getBalance() + converted_currency_amount);
            em.persist(payment_transaction);
            em.persist(sender_user);
            em.persist(recipient_user);
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
    public void acceptRequest(Request accepted_request) {
        Request updated_request = getRequest(accepted_request.getId());
        if(checkFunds(updated_request.getRecipient().getUser().getUserAccount(), updated_request.getAmount())) {
            updated_request.setPending(false);
            updated_request.setAccepted(Boolean.TRUE);
            em.persist(updated_request);
            String request_username = updated_request.getRecipient().getUser().getUsername();
            String sender_username = updated_request.getSender().getUser().getUsername();
            makePayment(request_username, sender_username, updated_request.getAmount());
        } else {
            // log that user does not have enough funds to accept the accepted_request
        }
    }
    
    @RolesAllowed("users")
    public void declineRequest(Request declined_request) {
        Request updated_request = getRequest(declined_request.getId());
        updated_request.setPending(false);
        updated_request.setAccepted(Boolean.FALSE);
        em.persist(updated_request);
    }
    
    public boolean checkFunds(UserAccount userAccount, double amount) {
        return (userAccount.getBalance() - amount) >= 0;
    }
    
}
