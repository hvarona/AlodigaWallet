package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Commission;
import com.alodiga.wallet.model.Promotion;
import com.alodiga.wallet.model.Transaction;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T15:02:10")
@StaticMetamodel(TransactionType.class)
public class TransactionType_ { 

    public static volatile CollectionAttribute<TransactionType, Transaction> transactionCollection;
    public static volatile CollectionAttribute<TransactionType, Promotion> promotionCollection;
    public static volatile CollectionAttribute<TransactionType, Commission> commissionCollection;
    public static volatile SingularAttribute<TransactionType, Long> id;
    public static volatile SingularAttribute<TransactionType, String> value;

}