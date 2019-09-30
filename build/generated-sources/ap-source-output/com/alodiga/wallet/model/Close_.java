package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Transaction;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-27T13:34:56")
@StaticMetamodel(Close.class)
public class Close_ { 

    public static volatile SingularAttribute<Close, Float> totalTax;
    public static volatile CollectionAttribute<Close, Transaction> transactionCollection;
    public static volatile SingularAttribute<Close, Float> totalAmount;
    public static volatile SingularAttribute<Close, Float> totalToUser;
    public static volatile SingularAttribute<Close, Long> id;
    public static volatile SingularAttribute<Close, Date> creationDate;
    public static volatile SingularAttribute<Close, BigInteger> userComerceId;
    public static volatile SingularAttribute<Close, String> status;

}