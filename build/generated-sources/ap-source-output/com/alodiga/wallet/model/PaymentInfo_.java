package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Address;
import com.alodiga.wallet.model.CreditcardType;
import com.alodiga.wallet.model.PaymentPatner;
import com.alodiga.wallet.model.PaymentType;
import com.alodiga.wallet.model.Transaction;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T13:29:06")
@StaticMetamodel(PaymentInfo.class)
public class PaymentInfo_ { 

    public static volatile CollectionAttribute<PaymentInfo, Transaction> transactionCollection;
    public static volatile SingularAttribute<PaymentInfo, CreditcardType> creditCardTypeId;
    public static volatile SingularAttribute<PaymentInfo, BigInteger> userId;
    public static volatile SingularAttribute<PaymentInfo, String> creditCardName;
    public static volatile SingularAttribute<PaymentInfo, Date> beginningDate;
    public static volatile SingularAttribute<PaymentInfo, String> creditCardCVV;
    public static volatile SingularAttribute<PaymentInfo, Date> endingDate;
    public static volatile SingularAttribute<PaymentInfo, PaymentType> paymentTypeId;
    public static volatile SingularAttribute<PaymentInfo, byte[]> creditCardNumber;
    public static volatile SingularAttribute<PaymentInfo, PaymentPatner> paymentPatnerId;
    public static volatile SingularAttribute<PaymentInfo, Date> creditCardDate;
    public static volatile SingularAttribute<PaymentInfo, Address> billingAddressId;
    public static volatile SingularAttribute<PaymentInfo, Long> id;

}