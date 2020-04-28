package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.UserService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AdminRegistrationBean {

    @EJB
    UserService usrSrv;
    
    String username;
    String userpassword;

    public AdminRegistrationBean() {

    }
    
    public String register() {
        usrSrv.registerAdmin(username, userpassword);
        return "admin";
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
    
}