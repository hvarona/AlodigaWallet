package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Address;
import com.alodiga.wallet.model.State;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T15:02:10")
@StaticMetamodel(County.class)
public class County_ { 

    public static volatile CollectionAttribute<County, Address> addressCollection;
    public static volatile SingularAttribute<County, State> stateId;
    public static volatile SingularAttribute<County, String> name;
    public static volatile SingularAttribute<County, Long> id;
    public static volatile SingularAttribute<County, String> shortName;

}