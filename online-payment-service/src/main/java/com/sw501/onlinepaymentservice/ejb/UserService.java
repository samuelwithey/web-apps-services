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

@DeclareRoles({"users", "admins"})
@Stateless
public class UserService {
    
    @Resource
    SessionContext ctx;

    @PersistenceContext
    EntityManager em;

    public UserService() {
    } 

    public void registerUser(String username, String userpassword, UserAccount account) {
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

            sys_user = new SystemUser(username, paswdToStoreInDB, account);
            sys_user_group = new SystemUserGroup(username, "users");
            account.setUser(sys_user);

            em.persist(sys_user);
            em.persist(sys_user_group);
            em.persist(account);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
   @RolesAllowed("admins")
    public List<UserAccount> viewAllUserAccounts() {
        return em.createQuery("SELECT account FROM UserAccount account").getResultList();
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
