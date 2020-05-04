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

    public UserService() {
    } 
    
    public double currencyConversion(CurrencyType currency, double amount) {
         Client client = ClientBuilder.newClient();
         WebTarget myResource = client.target("http://localhost:10000/CurrencyConversionRS/conversion")
                 .path("{currency1}").resolveTemplate("currency1", "GBP")
                 .path("{currency2}").resolveTemplate("currency2", currency.name())
                 .path("{amount_of_currency}").resolveTemplate("amount_of_currency", amount);
        String response = myResource.request(MediaType.APPLICATION_JSON).get(String.class);
        return Double.parseDouble(response);
    }

    public void registerUser(String username, String userpassword, CurrencyType currency) {
        try {
            SystemUser sys_user;
            SystemUserGroup sys_user_group;
            UserAccount account;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = userpassword;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            String paswdToStoreInDB = sb.toString();

            sys_user = new SystemUser(username, paswdToStoreInDB);
            sys_user_group = new SystemUserGroup(username, "users");
            
            account = new UserAccount();
            account.setCurrency(currency);
            
            if(currency.name().equals("GBP")) {
                account.setBalance(1000);
            } else {
                account.setBalance(currencyConversion(currency, 1000));
            }
            account.setUser(sys_user);
            sys_user.setUserAccount(account);
            
            em.persist(sys_user);
            em.persist(sys_user_group);
            em.persist(account);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @RolesAllowed("admins")
    public void registerAdmin(String username, String userpassword) {
        try {
            SystemUser sys_user;
            SystemUserGroup sys_user_group;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = userpassword;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            String paswdToStoreInDB = sb.toString();

            sys_user = new SystemUser(username, paswdToStoreInDB);
            sys_user_group = new SystemUserGroup(username, "admins");

            em.persist(sys_user);
            em.persist(sys_user_group);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
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
