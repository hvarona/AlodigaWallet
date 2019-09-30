package com.alodiga.wallet.model;

import com.alodiga.wallet.model.CommissionItem;
import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.TransactionType;
import com.alodiga.wallet.model.Withdrawal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T15:02:10")
@StaticMetamodel(Commission.class)
public class Commission_ { 

    public static volatile SingularAttribute<Commission, Date> beginningDate;
    public static volatile CollectionAttribute<Commission, CommissionItem> commissionItemCollection;
    public static volatile SingularAttribute<Commission, Date> endingDate;
    public static volatile SingularAttribute<Commission, TransactionType> transactionTypeId;
    public static volatile SingularAttribute<Commission, Product> productId;
    public static volatile SingularAttribute<Commission, Long> id;
    public static volatile SingularAttribute<Commission, Short> isPercentCommision;
    public static volatile SingularAttribute<Commission, Float> value;
    public static volatile CollectionAttribute<Commission, Withdrawal> withdrawalCollection;

}