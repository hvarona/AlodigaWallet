package com.alodiga.wallet.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-26T16:00:51")
@StaticMetamodel(Provider.class)
public class Provider_ { 

    public static volatile SingularAttribute<Provider, Long> id;
    public static volatile SingularAttribute<Provider, Boolean> enabled;
    public static volatile SingularAttribute<Provider, String> name;
    public static volatile SingularAttribute<Provider, String> url;
    public static volatile SingularAttribute<Provider, Boolean> isSMSProvider;
    public static volatile SingularAttribute<Provider, Float> aditionalPercent;

}