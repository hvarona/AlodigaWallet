package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.Transaction;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-13T10:48:05")
@StaticMetamodel(BalanceHistory.class)
public class BalanceHistory_ { 

    public static volatile SingularAttribute<BalanceHistory, Long> id;
    public static volatile SingularAttribute<BalanceHistory, Transaction> transactionId;
    public static volatile SingularAttribute<BalanceHistory, Float> oldAmount;
    public static volatile SingularAttribute<BalanceHistory, Long> userId;
    public static volatile SingularAttribute<BalanceHistory, String> adjusmentInfo;
    public static volatile SingularAttribute<BalanceHistory, Float> currentAmount;
    public static volatile SingularAttribute<BalanceHistory, Date> date;
    public static volatile SingularAttribute<BalanceHistory, Long> version;
    public static volatile SingularAttribute<BalanceHistory, Product> productId;

}