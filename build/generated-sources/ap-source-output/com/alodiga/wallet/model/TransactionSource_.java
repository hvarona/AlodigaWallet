package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Transaction;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-27T13:34:56")
@StaticMetamodel(TransactionSource.class)
public class TransactionSource_ { 

    public static volatile CollectionAttribute<TransactionSource, Transaction> transactionCollection;
    public static volatile SingularAttribute<TransactionSource, String> name;
    public static volatile SingularAttribute<TransactionSource, Long> id;

}