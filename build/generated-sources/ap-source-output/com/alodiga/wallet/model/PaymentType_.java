package com.alodiga.wallet.model;

import com.alodiga.wallet.model.PaymentInfo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-20T14:12:20")
@StaticMetamodel(PaymentType.class)
public class PaymentType_ { 

    public static volatile SingularAttribute<PaymentType, Long> id;
    public static volatile SingularAttribute<PaymentType, Boolean> enabled;
    public static volatile SingularAttribute<PaymentType, String> name;
    public static volatile CollectionAttribute<PaymentType, PaymentInfo> paymentInfoCollection;

}