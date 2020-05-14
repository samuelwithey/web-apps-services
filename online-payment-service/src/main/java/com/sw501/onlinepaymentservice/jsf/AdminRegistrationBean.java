package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.RegistrationService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AdminRegistrationBean {

    @EJB
    RegistrationService registration;
    
    String username;
    String userpassword;

    public AdminRegistrationBean() {

    }
    
    public String register() {
        registration.registerAdmin(username, userpassword);
        return "admin";
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
    
}