/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "parameter_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParameterType.findAll", query = "SELECT p FROM ParameterType p"),
    @NamedQuery(name = "ParameterType.findById", query = "SELECT p FROM ParameterType p WHERE p.id = :id"),
    @NamedQuery(name = "ParameterType.findByName", query = "SELECT p FROM ParameterType p WHERE p.name = :name")})
public class ParameterType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parameterTypeId")
    private Collection<ReportParameter> reportParameterCollection;

    public ParameterType() {
    }

    public ParameterType(Long id) {
        this.id = id;
    }

    public ParameterType(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @XmlTransient
    public Collection<ReportParameter> getReportParameterCollection() {
        return reportParameterCollection;
    }

    public void setReportParameterCollection(Collection<ReportParameter> reportParameterCollection) {
        this.reportParameterCollection = reportParameterCollection;
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
        if (!(object instanceof ParameterType)) {
            return false;
        }
        ParameterType other = (ParameterType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ParameterType[ id=" + id + " ]";
    }
    
}
