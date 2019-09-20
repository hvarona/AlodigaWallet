package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Bank;
import com.alodiga.wallet.model.Country;
import com.alodiga.wallet.model.Currency;
import com.alodiga.wallet.model.PreferenceValue;
import com.alodiga.wallet.model.Product;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-20T14:12:20")
@StaticMetamodel(Enterprise.class)
public class Enterprise_ { 

    public static volatile SingularAttribute<Enterprise, Long> id;
    public static volatile SingularAttribute<Enterprise, Country> countryId;
    public static volatile SingularAttribute<Enterprise, Boolean> enabled;
    public static volatile SingularAttribute<Enterprise, String> infoEmail;
    public static volatile CollectionAttribute<Enterprise, Bank> bankCollection;
    public static volatile SingularAttribute<Enterprise, String> address;
    public static volatile SingularAttribute<Enterprise, String> atcNumber;
    public static volatile SingularAttribute<Enterprise, String> email;
    public static volatile CollectionAttribute<Enterprise, PreferenceValue> preferenceValueCollection;
    public static volatile SingularAttribute<Enterprise, Currency> currencyId;
    public static volatile SingularAttribute<Enterprise, String> name;
    public static volatile SingularAttribute<Enterprise, String> invoiceAddress;
    public static volatile CollectionAttribute<Enterprise, Product> productCollection;
    public static volatile SingularAttribute<Enterprise, String> url;

}