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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@DeclareRoles({"users", "admins"})
@Stateless
public class Transaction {

    @PersistenceContext
    EntityManager em;
    
    @Resource
    SessionContext ctx;
    
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
        List<Payment> incoming_payments = em.createNamedQuery("viewIncomingPayments").getResultList();
        return incoming_payments;
    }
    
    @RolesAllowed("users")
    public List<Payment> viewReceivedPayments() {
        List<Payment> outgoing_payments = em.createNamedQuery("viewOutgoingPayments").getResultList();
        return outgoing_payments;
    }
    
    @RolesAllowed("users")
    public List<Request> viewIncomingRequests() {
        List<Request> incoming_requests = em.createNamedQuery("viewIncomingRequests").getResultList();
        return incoming_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewPendingIncomingRequests() {
        List<Request> incoming_requests = em.createNamedQuery("viewIncomingRequests").getResultList();
        return incoming_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewAcceptedIncomingRequests() {
        List<Request> incoming_requests = em.createNamedQuery("viewIncomingRequests").getResultList();
        return incoming_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewOutgoingRequests() {
        List<Request> outgoing_requests = em.createNamedQuery("viewOutgoingRequests").getResultList();
        return outgoing_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewPendingOutgoingRequests() {
        List<Request> outgoing_requests = em.createNamedQuery("viewPendingOutgoingRequests").getResultList();
        return outgoing_requests;
    }
    
    @RolesAllowed("users")
    public List<Request> viewAcceptedOutgoingRequests() {
        List<Request> outgoing_requests = em.createNamedQuery("viewPendingOutgoingRequests").getResultList();
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
    public void makePayment() {
    
    }
    
    @RolesAllowed("users")
    public void makeRequest() {
    
    }
    
    @RolesAllowed("users")
    public void respondToRequest() {
    
    }
}
