/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "close")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Close.findAll", query = "SELECT c FROM Close c"),
    @NamedQuery(name = "Close.findById", query = "SELECT c FROM Close c WHERE c.id = :id"),
    @NamedQuery(name = "Close.findByUserComerceId", query = "SELECT c FROM Close c WHERE c.userComerceId = :userComerceId"),
    @NamedQuery(name = "Close.findByCreationDate", query = "SELECT c FROM Close c WHERE c.creationDate = :creationDate"),
    @NamedQuery(name = "Close.findByTotalAmount", query = "SELECT c FROM Close c WHERE c.totalAmount = :totalAmount"),
    @NamedQuery(name = "Close.findByTotalTax", query = "SELECT c FROM Close c WHERE c.totalTax = :totalTax"),
    @NamedQuery(name = "Close.findByTotalToUser", query = "SELECT c FROM Close c WHERE c.totalToUser = :totalToUser"),
    @NamedQuery(name = "Close.findByStatus", query = "SELECT c FROM Close c WHERE c.status = :status")})
public class Close implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "userComerceId")
    private BigInteger userComerceId;
    @Basic(optional = false)
    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalAmount")
    private Float totalAmount;
    @Basic(optional = false)
    @Column(name = "totalTax")
    private float totalTax;
    @Basic(optional = false)
    @Column(name = "totalToUser")
    private float totalToUser;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "closeId")
    private Collection<Transaction> transactionCollection;

    public Close() {
    }

    public Close(Long id) {
        this.id = id;
    }

    public Close(Long id, Date creationDate, float totalTax, float totalToUser, String status) {
        this.id = id;
        this.creationDate = creationDate;
        this.totalTax = totalTax;
        this.totalToUser = totalToUser;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getUserComerceId() {
        return userComerceId;
    }

    public void setUserComerceId(BigInteger userComerceId) {
        this.userComerceId = userComerceId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(float totalTax) {
        this.totalTax = totalTax;
    }

    public float getTotalToUser() {
        return totalToUser;
    }

    public void setTotalToUser(float totalToUser) {
        this.totalToUser = totalToUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Transaction> getTransactionCollection() {
        return transactionCollection;
    }

    public void setTransactionCollection(Collection<Transaction> transactionCollection) {
        this.transactionCollection = transactionCollection;
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
        if (!(object instanceof Close)) {
            return false;
        }
        Close other = (Close) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Close[ id=" + id + " ]";
    }
    
}
