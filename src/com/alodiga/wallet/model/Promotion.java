/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "promotion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Promotion.findAll", query = "SELECT p FROM Promotion p"),
    @NamedQuery(name = "Promotion.findById", query = "SELECT p FROM Promotion p WHERE p.id = :id"),
    @NamedQuery(name = "Promotion.findByName", query = "SELECT p FROM Promotion p WHERE p.name = :name"),
    @NamedQuery(name = "Promotion.findByPromotionType", query = "SELECT p FROM Promotion p WHERE p.promotionType = :promotionType"),
    @NamedQuery(name = "Promotion.findByPromotionalAction", query = "SELECT p FROM Promotion p WHERE p.promotionalAction = :promotionalAction"),
    @NamedQuery(name = "Promotion.findByBeginningDate", query = "SELECT p FROM Promotion p WHERE p.beginningDate = :beginningDate"),
    @NamedQuery(name = "Promotion.findByEndingDate", query = "SELECT p FROM Promotion p WHERE p.endingDate = :endingDate"),
    @NamedQuery(name = "Promotion.findByIsPercentage", query = "SELECT p FROM Promotion p WHERE p.isPercentage = :isPercentage"),
    @NamedQuery(name = "Promotion.findByGoalAmount", query = "SELECT p FROM Promotion p WHERE p.goalAmount = :goalAmount"),
    @NamedQuery(name = "Promotion.findByPromotionalAmount", query = "SELECT p FROM Promotion p WHERE p.promotionalAmount = :promotionalAmount"),
    @NamedQuery(name = "Promotion.findByPromotionValidityDays", query = "SELECT p FROM Promotion p WHERE p.promotionValidityDays = :promotionValidityDays"),
    @NamedQuery(name = "Promotion.findByEnabled", query = "SELECT p FROM Promotion p WHERE p.enabled = :enabled")})
public class Promotion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "promotionType")
    private String promotionType;
    @Basic(optional = false)
    @Column(name = "promotionalAction")
    private String promotionalAction;
    @Basic(optional = false)
    @Column(name = "beginningDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginningDate;
    @Basic(optional = false)
    @Column(name = "endingDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endingDate;
    @Basic(optional = false)
    @Column(name = "isPercentage")
    private boolean isPercentage;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "goalAmount")
    private Float goalAmount;
    @Basic(optional = false)
    @Column(name = "promotionalAmount")
    private float promotionalAmount;
    @Basic(optional = false)
    @Column(name = "promotionValidityDays")
    private int promotionValidityDays;
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    @JoinColumn(name = "transactionTypeId", referencedColumnName = "id")
    @ManyToOne
    private TransactionType transactionTypeId;
    @JoinColumn(name = "productId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;
    @JoinColumn(name = "periodId", referencedColumnName = "id")
    @ManyToOne
    private Period periodId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "promotionId")
    private Collection<PromotionData> promotionDataCollection;
    @OneToMany(mappedBy = "promotionId")
    private Collection<PromotionItem> promotionItemCollection;

    public Promotion() {
    }

    public Promotion(Long id) {
        this.id = id;
    }

    public Promotion(Long id, String name, String promotionType, String promotionalAction, Date beginningDate, Date endingDate, boolean isPercentage, float promotionalAmount, int promotionValidityDays, boolean enabled) {
        this.id = id;
        this.name = name;
        this.promotionType = promotionType;
        this.promotionalAction = promotionalAction;
        this.beginningDate = beginningDate;
        this.endingDate = endingDate;
        this.isPercentage = isPercentage;
        this.promotionalAmount = promotionalAmount;
        this.promotionValidityDays = promotionValidityDays;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getPromotionalAction() {
        return promotionalAction;
    }

    public void setPromotionalAction(String promotionalAction) {
        this.promotionalAction = promotionalAction;
    }

    public Date getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(Date beginningDate) {
        this.beginningDate = beginningDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public boolean getIsPercentage() {
        return isPercentage;
    }

    public void setIsPercentage(boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

    public Float getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(Float goalAmount) {
        this.goalAmount = goalAmount;
    }

    public float getPromotionalAmount() {
        return promotionalAmount;
    }

    public void setPromotionalAmount(float promotionalAmount) {
        this.promotionalAmount = promotionalAmount;
    }

    public int getPromotionValidityDays() {
        return promotionValidityDays;
    }

    public void setPromotionValidityDays(int promotionValidityDays) {
        this.promotionValidityDays = promotionValidityDays;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public TransactionType getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(TransactionType transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Period getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Period periodId) {
        this.periodId = periodId;
    }

    @XmlTransient
    public Collection<PromotionData> getPromotionDataCollection() {
        return promotionDataCollection;
    }

    public void setPromotionDataCollection(Collection<PromotionData> promotionDataCollection) {
        this.promotionDataCollection = promotionDataCollection;
    }

    @XmlTransient
    public Collection<PromotionItem> getPromotionItemCollection() {
        return promotionItemCollection;
    }

    public void setPromotionItemCollection(Collection<PromotionItem> promotionItemCollection) {
        this.promotionItemCollection = promotionItemCollection;
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
        if (!(object instanceof Promotion)) {
            return false;
        }
        Promotion other = (Promotion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Promotion[ id=" + id + " ]";
    }
    
}
