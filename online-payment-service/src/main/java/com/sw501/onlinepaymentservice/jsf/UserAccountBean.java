package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.Transactions;
import com.sw501.onlinepaymentservice.ejb.UserService;
import com.sw501.onlinepaymentservice.entity.CurrencyType;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class UserAccountBean implements Serializable {
    
    
    @EJB
    UserService usrSrv;
    

    public UserAccountBean() {
    }

    public String getUsername() {
        return usrSrv.getUser().getUsername();
    }
    
    public String getCurrency() {
        return usrSrv.getUserAccountCurrencyType();
    }
    
    public double getBalance() {
        return usrSrv.getUserAccountBalance();
    }
    
}
