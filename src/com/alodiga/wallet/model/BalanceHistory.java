/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "balance_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BalanceHistory.findAll", query = "SELECT b FROM BalanceHistory b"),
    @NamedQuery(name = "BalanceHistory.findById", query = "SELECT b FROM BalanceHistory b WHERE b.id = :id"),
    @NamedQuery(name = "BalanceHistory.findByUserId", query = "SELECT b FROM BalanceHistory b WHERE b.userId = :userId"),
    @NamedQuery(name = "BalanceHistory.findByOldAmount", query = "SELECT b FROM BalanceHistory b WHERE b.oldAmount = :oldAmount"),
    @NamedQuery(name = "BalanceHistory.findByCurrentAmount", query = "SELECT b FROM BalanceHistory b WHERE b.currentAmount = :currentAmount"),
    @NamedQuery(name = "BalanceHistory.findByDate", query = "SELECT b FROM BalanceHistory b WHERE b.date = :date"),
    @NamedQuery(name = "BalanceHistory.findByVersion", query = "SELECT b FROM BalanceHistory b WHERE b.version = :version"),
    @NamedQuery(name = "BalanceHistory.findByAdjusmentInfo", query = "SELECT b FROM BalanceHistory b WHERE b.adjusmentInfo = :adjusmentInfo")})
public class BalanceHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "userId")
    private long userId;
    @Basic(optional = false)
    @Column(name = "oldAmount")
    private float oldAmount;
    @Basic(optional = false)
    @Column(name = "currentAmount")
    private float currentAmount;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "version")
    private long version;
    @Column(name = "adjusmentInfo")
    private String adjusmentInfo;
    @JoinColumn(name = "transactionId", referencedColumnName = "id")
    @ManyToOne
    private Transaction transactionId;
    @JoinColumn(name = "productId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;

    public BalanceHistory() {
    }

    public BalanceHistory(Long id) {
        this.id = id;
    }

    public BalanceHistory(Long id, long userId, float oldAmount, float currentAmount, Date date, long version) {
        this.id = id;
        this.userId = userId;
        this.oldAmount = oldAmount;
        this.currentAmount = currentAmount;
        this.date = date;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public float getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(float oldAmount) {
        this.oldAmount = oldAmount;
    }

    public float getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(float currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getAdjusmentInfo() {
        return adjusmentInfo;
    }

    public void setAdjusmentInfo(String adjusmentInfo) {
        this.adjusmentInfo = adjusmentInfo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BalanceHistory)) {
            return false;
        }
        BalanceHistory other = (BalanceHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.BalanceHistory[ id=" + id + " ]";
    }
    
}
