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
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author ltoro
 */
@Entity
@Table(name = "card")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Card.findAll", query = "SELECT c FROM Card c")
    , @NamedQuery(name = "Card.findById", query = "SELECT c FROM Card c WHERE c.id = :id")
    , @NamedQuery(name = "Card.findByNumberCard", query = "SELECT c FROM Card c WHERE c.numberCard = :numberCard")})
public class Card implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cardId")
    private Collection<UserHasCard> userHasCardCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numberCard")
    private String numberCard;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cardId")
    private Collection<CardHasProduct> cardHasProductCollection;

    public Card() {
    }

    public Card(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(String numberCard) {
        this.numberCard = numberCard;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<CardHasProduct> getCardHasProductCollection() {
        return cardHasProductCollection;
    }

    public void setCardHasProductCollection(Collection<CardHasProduct> cardHasProductCollection) {
        this.cardHasProductCollection = cardHasProductCollection;
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
        if (!(object instanceof Card)) {
            return false;
        }
        Card other = (Card) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.Card[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UserHasCard> getUserHasCardCollection() {
        return userHasCardCollection;
    }

    public void setUserHasCardCollection(Collection<UserHasCard> userHasCardCollection) {
        this.userHasCardCollection = userHasCardCollection;
    }
    
}
