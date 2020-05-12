package com.sw501.onlinepaymentservice.ejb;

import com.sw501.onlinepaymentservice.entity.SystemUser;
import com.sw501.onlinepaymentservice.entity.UserAccount;
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
    public void viewIncomingPayments() {
    
    }
    
    @RolesAllowed("users")
    public void viewOutgoingPayments() {
    
    }
    
    @RolesAllowed("users")
    public void viewIncomingRequests() {
    
    }
    
    @RolesAllowed("users")
    public void viewOutgoingRequests() {
    
    }
    
    @RolesAllowed("admins")
    public void viewAllPayments() {
    
    }
    
    @RolesAllowed("admins")
    public void viewAllRequests() {
    
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