/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "withdrawal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Withdrawal.findAll", query = "SELECT w FROM Withdrawal w"),
    @NamedQuery(name = "Withdrawal.findById", query = "SELECT w FROM Withdrawal w WHERE w.id = :id"),
    @NamedQuery(name = "Withdrawal.findByUserSourceId", query = "SELECT w FROM Withdrawal w WHERE w.userSourceId = :userSourceId"),
    @NamedQuery(name = "Withdrawal.findByUserHasBankId", query = "SELECT w FROM Withdrawal w WHERE w.userHasBankId = :userHasBankId"),
    @NamedQuery(name = "Withdrawal.findByAdditional", query = "SELECT w FROM Withdrawal w WHERE w.additional = :additional"),
    @NamedQuery(name = "Withdrawal.findByAdditional2", query = "SELECT w FROM Withdrawal w WHERE w.additional2 = :additional2")})
public class Withdrawal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "userSourceId")
    private BigInteger userSourceId;
    @Column(name = "accountBank")
    private String accountBank;
    @Size(max = 500)
    @Column(name = "additional")
    private String additional;
    @Size(max = 500)
    @Column(name = "additional2")
    private String additional2;
    @JoinColumn(name = "bankId", referencedColumnName = "id")
    @ManyToOne
    private Bank bankId;
    @JoinColumn(name = "transactionId", referencedColumnName = "id")
    @ManyToOne
    private Transaction transactionId;
    @JoinColumn(name = "productId", referencedColumnName = "id")
    @ManyToOne
    private Product productId;
    @JoinColumn(name = "commisionId", referencedColumnName = "id")
    @ManyToOne
    private Commission commisionId;
    @JoinColumn(name = "typeWithdrawalId", referencedColumnName = "id")
    @ManyToOne
    private WithdrawalType typeWithdrawalId;

    public Withdrawal() {
    }

    public Withdrawal(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getUserSourceId() {
        return userSourceId;
    }

    public void setUserSourceId(BigInteger userSourceId) {
        this.userSourceId = userSourceId;
    }
    
    public Bank getBankId() {
        return bankId;
    }
    
    public void setbankId(Bank bankId) {
        this.bankId = bankId;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getAdditional2() {
        return additional2;
    }

    public void setAdditional2(String additional2) {
        this.additional2 = additional2;
    }

    public Transaction getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Transaction transactionId) {
        this.transactionId = transactionId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Commission getCommisionId() {
        return commisionId;
    }

    public void setCommisionId(Commission commisionId) {
        this.commisionId = commisionId;
    }

    public WithdrawalType getTypeWithdrawalId() {
        return typeWithdrawalId;
    }

    public void setTypeWithdrawalId(WithdrawalType typeWithdrawalId) {
        this.typeWithdrawalId = typeWithdrawalId;
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
        if (!(object instanceof Withdrawal)) {
            return false;
        }
        Withdrawal other = (Withdrawal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.Withdrawal[ id=" + id + " ]";
    }
    
}
