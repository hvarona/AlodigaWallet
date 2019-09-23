package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Country;
import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.UserHasBank;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-23T14:03:36")
@StaticMetamodel(Bank.class)
public class Bank_ { 

    public static volatile SingularAttribute<Bank, Long> id;
    public static volatile SingularAttribute<Bank, String> aba;
    public static volatile CollectionAttribute<Bank, UserHasBank> userHasBankCollection;
    public static volatile SingularAttribute<Bank, Country> countryId;
    public static volatile SingularAttribute<Bank, String> name;
    public static volatile SingularAttribute<Bank, Enterprise> enterpriseId;

}