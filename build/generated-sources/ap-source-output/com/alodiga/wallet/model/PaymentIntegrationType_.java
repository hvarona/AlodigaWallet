package com.alodiga.wallet.model;

import com.alodiga.wallet.model.PaymentPatner;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-26T16:00:51")
@StaticMetamodel(PaymentIntegrationType.class)
public class PaymentIntegrationType_ { 

    public static volatile SingularAttribute<PaymentIntegrationType, Long> id;
    public static volatile SingularAttribute<PaymentIntegrationType, Boolean> enabled;
    public static volatile SingularAttribute<PaymentIntegrationType, String> name;
    public static volatile CollectionAttribute<PaymentIntegrationType, PaymentPatner> paymentPatnerCollection;

}