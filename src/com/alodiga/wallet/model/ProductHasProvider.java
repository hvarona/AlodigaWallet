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
@Table(name = "product_has_provider")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductHasProvider.findAll", query = "SELECT p FROM ProductHasProvider p"),
    @NamedQuery(name = "ProductHasProvider.findById", query = "SELECT p FROM ProductHasProvider p WHERE p.id = :id"),
    @NamedQuery(name = "ProductHasProvider.findByProductId", query = "SELECT p FROM ProductHasProvider p WHERE p.productId = :productId"),
    @NamedQuery(name = "ProductHasProvider.findByProviderId", query = "SELECT p FROM ProductHasProvider p WHERE p.providerId = :providerId")})
public class ProductHasProvider implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "productId")
    private long productId;
    @Basic(optional = false)
    @Column(name = "providerId")
    private long providerId;

    public ProductHasProvider() {
    }

    public ProductHasProvider(Long id) {
        this.id = id;
    }

    public ProductHasProvider(Long id, long productId, long providerId) {
        this.id = id;
        this.productId = productId;
        this.providerId = providerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
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
        if (!(object instanceof ProductHasProvider)) {
            return false;
        }
        ProductHasProvider other = (ProductHasProvider) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ProductHasProvider[ id=" + id + " ]";
    }
    
}
