package com.alodiga.wallet.model;

import com.alodiga.wallet.model.PaymentInfo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-26T16:00:51")
@StaticMetamodel(CreditcardType.class)
public class CreditcardType_ { 

    public static volatile SingularAttribute<CreditcardType, Long> id;
    public static volatile SingularAttribute<CreditcardType, Boolean> enabled;
    public static volatile SingularAttribute<CreditcardType, String> name;
    public static volatile CollectionAttribute<CreditcardType, PaymentInfo> paymentInfoCollection;

}