package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.UserService;
import com.sw501.onlinepaymentservice.entity.CurrencyType;
import com.sw501.onlinepaymentservice.entity.UserAccount;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class RegistrationBean {

    @EJB
    UserService usrSrv;
    
    String username;
    String userpassword;
    CurrencyType currency;
    UserAccount account;

    public RegistrationBean() {

    }
    
    public String register() {
        account = new UserAccount();
        account.setCurrency(currency);
        account.setBalance(1000);
        usrSrv.registerUser(username, userpassword, account);
        return "index";
    }
    
    public UserService getUsrSrv() {
        return usrSrv;
    }

    public void setUsrSrv(UserService usrSrv) {
        this.usrSrv = usrSrv;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }
    
    public CurrencyType[] getCurrencyTypes() {
        return CurrencyType.values();
    }
    
}