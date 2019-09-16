package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Withdrawal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-13T10:48:05")
@StaticMetamodel(WithdrawalType.class)
public class WithdrawalType_ { 

    public static volatile SingularAttribute<WithdrawalType, Long> id;
    public static volatile SingularAttribute<WithdrawalType, String> name;
    public static volatile CollectionAttribute<WithdrawalType, Withdrawal> withdrawalCollection;

}