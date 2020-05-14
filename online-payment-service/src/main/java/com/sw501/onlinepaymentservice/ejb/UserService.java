package com.sw501.onlinepaymentservice.ejb;

import com.sw501.onlinepaymentservice.entity.CurrencyType;
import com.sw501.onlinepaymentservice.entity.SystemUser;
import com.sw501.onlinepaymentservice.entity.SystemUserGroup;
import com.sw501.onlinepaymentservice.entity.UserAccount;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@DeclareRoles({"users", "admins"})
@Stateless
public class UserService {
    
    @Resource
    SessionContext ctx;

    @PersistenceContext
    EntityManager em;
    
    @Inject
    Converter converter;

    public UserService() {
    } 
     
    @RolesAllowed("admins")
    public synchronized List<UserAccount> viewAllUserAccounts() {
        List<UserAccount> userAccounts = em.createNamedQuery("findAllUserAccounts").getResultList();
        return userAccounts;
    }
    
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

}
