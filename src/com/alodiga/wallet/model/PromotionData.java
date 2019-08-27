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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "promotion_data")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PromotionData.findAll", query = "SELECT p FROM PromotionData p"),
    @NamedQuery(name = "PromotionData.findById", query = "SELECT p FROM PromotionData p WHERE p.id = :id"),
    @NamedQuery(name = "PromotionData.findBySmsText", query = "SELECT p FROM PromotionData p WHERE p.smsText = :smsText")})
public class PromotionData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "smsText")
    private String smsText;
    @Basic(optional = false)
    @Lob
    @Column(name = "mailText")
    private String mailText;
    @JoinColumn(name = "languageId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Language languageId;
    @JoinColumn(name = "promotionId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Promotion promotionId;

    public PromotionData() {
    }

    public PromotionData(Long id) {
        this.id = id;
    }

    public PromotionData(Long id, String smsText, String mailText) {
        this.id = id;
        this.smsText = smsText;
        this.mailText = mailText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public String getMailText() {
        return mailText;
    }

    public void setMailText(String mailText) {
        this.mailText = mailText;
    }

    public Language getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Language languageId) {
        this.languageId = languageId;
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
        if (!(object instanceof PromotionData)) {
            return false;
        }
        PromotionData other = (PromotionData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PromotionData[ id=" + id + " ]";
    }
    
}
