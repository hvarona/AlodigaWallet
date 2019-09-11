package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Address;
import com.alodiga.wallet.model.State;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T15:19:36")
@StaticMetamodel(City.class)
public class City_ { 

    public static volatile SingularAttribute<City, Long> id;
    public static volatile SingularAttribute<City, State> stateId;
    public static volatile SingularAttribute<City, String> name;
    public static volatile CollectionAttribute<City, Address> addressCollection;

}