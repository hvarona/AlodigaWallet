package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Commission;
import com.alodiga.wallet.model.Transaction;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-26T16:00:51")
@StaticMetamodel(CommissionItem.class)
public class CommissionItem_ { 

    public static volatile SingularAttribute<CommissionItem, Long> id;
    public static volatile SingularAttribute<CommissionItem, Float> amount;
    public static volatile SingularAttribute<CommissionItem, Transaction> transactionId;
    public static volatile SingularAttribute<CommissionItem, Commission> commissionId;
    public static volatile SingularAttribute<CommissionItem, Date> processedDate;
    public static volatile SingularAttribute<CommissionItem, Boolean> isResidual;

}