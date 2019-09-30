package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Bank;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-27T13:34:56")
@StaticMetamodel(UserHasBank.class)
public class UserHasBank_ { 

    public static volatile SingularAttribute<UserHasBank, Long> userSourceId;
    public static volatile SingularAttribute<UserHasBank, Bank> bankId;
    public static volatile SingularAttribute<UserHasBank, Long> id;
    public static volatile SingularAttribute<UserHasBank, String> accountNumber;

}