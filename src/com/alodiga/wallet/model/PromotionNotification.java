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
@Table(name = "promotion_notification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PromotionNotification.findAll", query = "SELECT p FROM PromotionNotification p"),
    @NamedQuery(name = "PromotionNotification.findById", query = "SELECT p FROM PromotionNotification p WHERE p.id = :id"),
    @NamedQuery(name = "PromotionNotification.findByPromotionType", query = "SELECT p FROM PromotionNotification p WHERE p.promotionType = :promotionType"),
    @NamedQuery(name = "PromotionNotification.findByAckMailFrom", query = "SELECT p FROM PromotionNotification p WHERE p.ackMailFrom = :ackMailFrom"),
    @NamedQuery(name = "PromotionNotification.findByAckMailSubject", query = "SELECT p FROM PromotionNotification p WHERE p.ackMailSubject = :ackMailSubject"),
    @NamedQuery(name = "PromotionNotification.findByAckSmsFrom", query = "SELECT p FROM PromotionNotification p WHERE p.ackSmsFrom = :ackSmsFrom"),
    @NamedQuery(name = "PromotionNotification.findByAckSmsText", query = "SELECT p FROM PromotionNotification p WHERE p.ackSmsText = :ackSmsText"),
    @NamedQuery(name = "PromotionNotification.findByEnabled", query = "SELECT p FROM PromotionNotification p WHERE p.enabled = :enabled")})
public class PromotionNotification implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "promotionType")
    private String promotionType;
    @Basic(optional = false)
    @Column(name = "ackMailFrom")
    private String ackMailFrom;
    @Basic(optional = false)
    @Column(name = "ackMailSubject")
    private String ackMailSubject;
    @Basic(optional = false)
    @Lob
    @Column(name = "ackMailHtml")
    private String ackMailHtml;
    @Basic(optional = false)
    @Column(name = "ackSmsFrom")
    private String ackSmsFrom;
    @Basic(optional = false)
    @Column(name = "ackSmsText")
    private String ackSmsText;
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    @JoinColumn(name = "languageId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Language languageId;

    public PromotionNotification() {
    }

    public PromotionNotification(Long id) {
        this.id = id;
    }

    public PromotionNotification(Long id, String promotionType, String ackMailFrom, String ackMailSubject, String ackMailHtml, String ackSmsFrom, String ackSmsText, boolean enabled) {
        this.id = id;
        this.promotionType = promotionType;
        this.ackMailFrom = ackMailFrom;
        this.ackMailSubject = ackMailSubject;
        this.ackMailHtml = ackMailHtml;
        this.ackSmsFrom = ackSmsFrom;
        this.ackSmsText = ackSmsText;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getAckMailFrom() {
        return ackMailFrom;
    }

    public void setAckMailFrom(String ackMailFrom) {
        this.ackMailFrom = ackMailFrom;
    }

    public String getAckMailSubject() {
        return ackMailSubject;
    }

    public void setAckMailSubject(String ackMailSubject) {
        this.ackMailSubject = ackMailSubject;
    }

    public String getAckMailHtml() {
        return ackMailHtml;
    }

    public void setAckMailHtml(String ackMailHtml) {
        this.ackMailHtml = ackMailHtml;
    }

    public String getAckSmsFrom() {
        return ackSmsFrom;
    }

    public void setAckSmsFrom(String ackSmsFrom) {
        this.ackSmsFrom = ackSmsFrom;
    }

    public String getAckSmsText() {
        return ackSmsText;
    }

    public void setAckSmsText(String ackSmsText) {
        this.ackSmsText = ackSmsText;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Language getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Language languageId) {
        this.languageId = languageId;
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
        if (!(object instanceof PromotionNotification)) {
            return false;
        }
        PromotionNotification other = (PromotionNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PromotionNotification[ id=" + id + " ]";
    }
    
}
