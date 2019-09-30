package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Commission;
import com.alodiga.wallet.model.Transaction;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T15:02:10")
@StaticMetamodel(CommissionItem.class)
public class CommissionItem_ { 

    public static volatile SingularAttribute<CommissionItem, Date> processedDate;
    public static volatile SingularAttribute<CommissionItem, Float> amount;
    public static volatile SingularAttribute<CommissionItem, Commission> commissionId;
    public static volatile SingularAttribute<CommissionItem, Boolean> isResidual;
    public static volatile SingularAttribute<CommissionItem, Long> id;
    public static volatile SingularAttribute<CommissionItem, Transaction> transactionId;

}