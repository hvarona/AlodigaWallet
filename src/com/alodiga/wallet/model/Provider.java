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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "provider")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Provider.findAll", query = "SELECT p FROM Provider p"),
    @NamedQuery(name = "Provider.findById", query = "SELECT p FROM Provider p WHERE p.id = :id"),
    @NamedQuery(name = "Provider.findByName", query = "SELECT p FROM Provider p WHERE p.name = :name"),
    @NamedQuery(name = "Provider.findByUrl", query = "SELECT p FROM Provider p WHERE p.url = :url"),
    @NamedQuery(name = "Provider.findByIsSMSProvider", query = "SELECT p FROM Provider p WHERE p.isSMSProvider = :isSMSProvider"),
    @NamedQuery(name = "Provider.findByEnabled", query = "SELECT p FROM Provider p WHERE p.enabled = :enabled"),
    @NamedQuery(name = "Provider.findByAditionalPercent", query = "SELECT p FROM Provider p WHERE p.aditionalPercent = :aditionalPercent")})
public class Provider implements Serializable {
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
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @Column(name = "isSMSProvider")
    private boolean isSMSProvider;
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "aditionalPercent")
    private Float aditionalPercent;

    public Provider() {
    }

    public Provider(Long id) {
        this.id = id;
    }

    public Provider(Long id, String name, String url, boolean isSMSProvider, boolean enabled) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.isSMSProvider = isSMSProvider;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getIsSMSProvider() {
        return isSMSProvider;
    }

    public void setIsSMSProvider(boolean isSMSProvider) {
        this.isSMSProvider = isSMSProvider;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Float getAditionalPercent() {
        return aditionalPercent;
    }

    public void setAditionalPercent(Float aditionalPercent) {
        this.aditionalPercent = aditionalPercent;
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
        if (!(object instanceof Provider)) {
            return false;
        }
        Provider other = (Provider) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Provider[ id=" + id + " ]";
    }
    
}
