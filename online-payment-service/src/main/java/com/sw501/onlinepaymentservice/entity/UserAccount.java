package com.sw501.onlinepaymentservice.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@NamedQuery(name="findAllUserAccounts", query="SELECT account FROM UserAccount account")

@Entity
public class UserAccount implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    protected double balance;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    protected CurrencyType currency;
    
    @OneToOne(mappedBy="account")
    protected SystemUser user;
    
    @OneToMany(mappedBy = "sender")
    protected List<Payment> payments;
    
    @OneToMany(mappedBy = "sender")
    protected List<Request> requests;

    public UserAccount() {
    }

    public UserAccount(double balance, CurrencyType currency, SystemUser user, List<Payment> payments, List<Request> requests) {
        this.balance = balance;
        this.currency = currency;
        this.user = user;
        this.payments = payments;
        this.requests = requests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.balance) ^ (Double.doubleToLongBits(this.balance) >>> 32));
        hash = 41 * hash + Objects.hashCode(this.currency);
        hash = 41 * hash + Objects.hashCode(this.user);
        hash = 41 * hash + Objects.hashCode(this.payments);
        hash = 41 * hash + Objects.hashCode(this.requests);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserAccount other = (UserAccount) obj;
        if (Double.doubleToLongBits(this.balance) != Double.doubleToLongBits(other.balance)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.currency != other.currency) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.payments, other.payments)) {
            return false;
        }
        if (!Objects.equals(this.requests, other.requests)) {
            return false;
        }
        return true;
    }
    
}
