package com.alodiga.wallet.model;

import com.alodiga.wallet.model.City;
import com.alodiga.wallet.model.Country;
import com.alodiga.wallet.model.County;
import com.alodiga.wallet.model.PaymentInfo;
import com.alodiga.wallet.model.State;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-19T15:54:29")
@StaticMetamodel(Address.class)
public class Address_ { 

    public static volatile SingularAttribute<Address, Long> id;
    public static volatile SingularAttribute<Address, Country> countryId;
    public static volatile SingularAttribute<Address, String> countyName;
    public static volatile SingularAttribute<Address, String> address;
    public static volatile SingularAttribute<Address, City> cityId;
    public static volatile SingularAttribute<Address, State> stateId;
    public static volatile SingularAttribute<Address, String> zipCode;
    public static volatile SingularAttribute<Address, String> stateName;
    public static volatile SingularAttribute<Address, String> cityName;
    public static volatile CollectionAttribute<Address, PaymentInfo> paymentInfoCollection;
    public static volatile SingularAttribute<Address, County> countyId;

}