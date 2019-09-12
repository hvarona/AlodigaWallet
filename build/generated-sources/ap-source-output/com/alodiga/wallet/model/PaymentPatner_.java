package com.alodiga.wallet.model;

import com.alodiga.wallet.model.PaymentInfo;
import com.alodiga.wallet.model.PaymentIntegrationType;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T16:52:53")
@StaticMetamodel(PaymentPatner.class)
public class PaymentPatner_ { 

    public static volatile SingularAttribute<PaymentPatner, Long> id;
    public static volatile SingularAttribute<PaymentPatner, String> urlSubmit;
    public static volatile SingularAttribute<PaymentPatner, Boolean> enabled;
    public static volatile SingularAttribute<PaymentPatner, PaymentIntegrationType> integrationTypeId;
    public static volatile SingularAttribute<PaymentPatner, String> name;
    public static volatile SingularAttribute<PaymentPatner, String> paymentUser;
    public static volatile SingularAttribute<PaymentPatner, String> encriptionKey;
    public static volatile SingularAttribute<PaymentPatner, Boolean> testMode;
    public static volatile SingularAttribute<PaymentPatner, String> urlResponse;
    public static volatile SingularAttribute<PaymentPatner, String> urlConfirmation;
    public static volatile CollectionAttribute<PaymentPatner, PaymentInfo> paymentInfoCollection;

}