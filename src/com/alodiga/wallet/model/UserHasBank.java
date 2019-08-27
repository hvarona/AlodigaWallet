/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "user_has_bank")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserHasBank.findAll", query = "SELECT u FROM UserHasBank u"),
    @NamedQuery(name = "UserHasBank.findById", query = "SELECT u FROM UserHasBank u WHERE u.id = :id"),
    @NamedQuery(name = "UserHasBank.findByUserSourceId", query = "SELECT u FROM UserHasBank u WHERE u.userSourceId = :userSourceId"),
    @NamedQuery(name = "UserHasBank.findByAccountNumber", query = "SELECT u FROM UserHasBank u WHERE u.accountNumber = :accountNumber")})
public class UserHasBank implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "userSourceId")
    private long userSourceId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "accountNumber")
    private String accountNumber;
    @JoinColumn(name = "bankId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bank bankId;

    public UserHasBank() {
    }

    public UserHasBank(Long id) {
        this.id = id;
    }

    public UserHasBank(Long id, long userSourceId, String accountNumber) {
        this.id = id;
        this.userSourceId = userSourceId;
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserSourceId() {
        return userSourceId;
    }

    public void setUserSourceId(long userSourceId) {
        this.userSourceId = userSourceId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Bank getBankId() {
        return bankId;
    }

    public void setBankId(Bank bankId) {
        this.bankId = bankId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserHasBank)) {
            return false;
        }
        UserHasBank other = (UserHasBank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.UserHasBank[ id=" + id + " ]";
    }
    
}
