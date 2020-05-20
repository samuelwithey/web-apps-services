package com.sw501.onlinepaymentservice.entity;

import java.io.Serializable;
import java.util.Objects;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@NamedQuery(name = "viewAllRequests", query = "SELECT r FROM Request r")

@NamedQuery(name = "viewPendingReceivedRequests", query = "SELECT r FROM Request r WHERE r.recipient = ?1 AND r.pending = TRUE")

@NamedQuery(name = "viewPendingSentRequests", query = "SELECT r FROM Request r WHERE r.sender = ?1 AND r.pending = TRUE")

@NamedQuery(name = "viewReceivedAcceptedDeclinedRequests", query = "SELECT r FROM Request r WHERE r.recipient = ?1 AND r.pending = FALSE")

@NamedQuery(name = "viewSentAcceptedDeclinedRequests", query = "SELECT r FROM Request r WHERE r.sender = ?1 AND r.pending = FALSE")

@Entity
public class Request implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    protected double amount;
    
    @ManyToOne(cascade = ALL)
    @JoinColumn
    protected UserAccount sender;
    
    @OneToOne
    protected UserAccount recipient;
    
    @NotNull
    protected boolean pending;
    
    protected Boolean accepted;

    public Request() {
    }

    public Request(double amount, UserAccount sender, UserAccount recipient) {
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.pending = true;
        this.accepted = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public UserAccount getSender() {
        return sender;
    }

    public void setSender(UserAccount sender) {
        this.sender = sender;
    }

    public UserAccount getRecipient() {
        return recipient;
    }

    public void setRecipient(UserAccount recipient) {
        this.recipient = recipient;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
        hash = 23 * hash + Objects.hashCode(this.sender);
        hash = 23 * hash + Objects.hashCode(this.recipient);
        hash = 23 * hash + (this.pending ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(this.accepted);
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
        final Request other = (Request) obj;
        if (Double.doubleToLongBits(this.amount) != Double.doubleToLongBits(other.amount)) {
            return false;
        }
        if (this.pending != other.pending) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Objects.equals(this.recipient, other.recipient)) {
            return false;
        }
        if (!Objects.equals(this.accepted, other.accepted)) {
            return false;
        }
        return true;
    }
}
