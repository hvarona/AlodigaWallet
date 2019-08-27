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
@Table(name = "promotion_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PromotionItem.findAll", query = "SELECT p FROM PromotionItem p"),
    @NamedQuery(name = "PromotionItem.findById", query = "SELECT p FROM PromotionItem p WHERE p.id = :id"),
    @NamedQuery(name = "PromotionItem.findByPromotionalAmount", query = "SELECT p FROM PromotionItem p WHERE p.promotionalAmount = :promotionalAmount"),
    @NamedQuery(name = "PromotionItem.findByPromotionApplicationDate", query = "SELECT p FROM PromotionItem p WHERE p.promotionApplicationDate = :promotionApplicationDate"),
    @NamedQuery(name = "PromotionItem.findByComments", query = "SELECT p FROM PromotionItem p WHERE p.comments = :comments")})
public class PromotionItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "promotionalAmount")
    private float promotionalAmount;
    @Basic(optional = false)
    @Column(name = "promotionApplicationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date promotionApplicationDate;
    @Column(name = "comments")
    private String comments;
    @JoinColumn(name = "transactionId", referencedColumnName = "id")
    @ManyToOne
    private Transaction transactionId;
    @JoinColumn(name = "promotionId", referencedColumnName = "id")
    @ManyToOne
    private Promotion promotionId;

    public PromotionItem() {
    }

    public PromotionItem(Long id) {
        this.id = id;
    }

    public PromotionItem(Long id, float promotionalAmount, Date promotionApplicationDate) {
        this.id = id;
        this.promotionalAmount = promotionalAmount;
        this.promotionApplicationDate = promotionApplicationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getPromotionalAmount() {
        return promotionalAmount;
    }

    public void setPromotionalAmount(float promotionalAmount) {
        this.promotionalAmount = promotionalAmount;
    }

    public Date getPromotionApplicationDate() {
        return promotionApplicationDate;
    }

    public void setPromotionApplicationDate(Date promotionApplicationDate) {
        this.promotionApplicationDate = promotionApplicationDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Transaction getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Transaction transactionId) {
        this.transactionId = transactionId;
    }

    public Promotion getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Promotion promotionId) {
        this.promotionId = promotionId;
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
        if (!(object instanceof PromotionItem)) {
            return false;
        }
        PromotionItem other = (PromotionItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PromotionItem[ id=" + id + " ]";
    }
    
}
