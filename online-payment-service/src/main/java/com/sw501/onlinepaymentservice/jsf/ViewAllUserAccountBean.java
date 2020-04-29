package com.sw501.onlinepaymentservice.jsf;

import com.sw501.onlinepaymentservice.ejb.UserService;
import com.sw501.onlinepaymentservice.entity.UserAccount;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class ViewAllUserAccountBean implements Serializable {
    
    @EJB
    UserService usrSrv;

    public ViewAllUserAccountBean() {
    }
    
    public List<UserAccount> getUserAccounts() {
        return usrSrv.viewAllUserAccounts();
    }
    
    public int numOfUserAccounts() {
        return getUserAccounts().size();
    }
    
}
