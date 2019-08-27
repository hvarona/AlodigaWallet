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
@Table(name = "report_parameter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportParameter.findAll", query = "SELECT r FROM ReportParameter r"),
    @NamedQuery(name = "ReportParameter.findById", query = "SELECT r FROM ReportParameter r WHERE r.id = :id"),
    @NamedQuery(name = "ReportParameter.findByName", query = "SELECT r FROM ReportParameter r WHERE r.name = :name"),
    @NamedQuery(name = "ReportParameter.findByRequired", query = "SELECT r FROM ReportParameter r WHERE r.required = :required"),
    @NamedQuery(name = "ReportParameter.findByIndexOrder", query = "SELECT r FROM ReportParameter r WHERE r.indexOrder = :indexOrder"),
    @NamedQuery(name = "ReportParameter.findByDefaultValue", query = "SELECT r FROM ReportParameter r WHERE r.defaultValue = :defaultValue")})
public class ReportParameter implements Serializable {
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
    @Column(name = "required")
    private boolean required;
    @Basic(optional = false)
    @Column(name = "indexOrder")
    private int indexOrder;
    @Column(name = "defaultValue")
    private String defaultValue;
    @JoinColumn(name = "reportId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Report reportId;
    @JoinColumn(name = "parameterTypeId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ParameterType parameterTypeId;

    public ReportParameter() {
    }

    public ReportParameter(Long id) {
        this.id = id;
    }

    public ReportParameter(Long id, String name, boolean required, int indexOrder) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.indexOrder = indexOrder;
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

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getIndexOrder() {
        return indexOrder;
    }

    public void setIndexOrder(int indexOrder) {
        this.indexOrder = indexOrder;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Report getReportId() {
        return reportId;
    }

    public void setReportId(Report reportId) {
        this.reportId = reportId;
    }

    public ParameterType getParameterTypeId() {
        return parameterTypeId;
    }

    public void setParameterTypeId(ParameterType parameterTypeId) {
        this.parameterTypeId = parameterTypeId;
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
        if (!(object instanceof ReportParameter)) {
            return false;
        }
        ReportParameter other = (ReportParameter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ReportParameter[ id=" + id + " ]";
    }
    
}
