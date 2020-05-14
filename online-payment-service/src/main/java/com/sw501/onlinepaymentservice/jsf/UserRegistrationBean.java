package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.RegistrationService;
import com.sw501.onlinepaymentservice.entity.CurrencyType;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class UserRegistrationBean {

    @EJB
    RegistrationService registration;
    
    String username;
    String userpassword;
    CurrencyType currency;
    
    public UserRegistrationBean() {

    }
    
    public String register() {
        registration.registerUser(username, userpassword, currency);
        return "index";
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