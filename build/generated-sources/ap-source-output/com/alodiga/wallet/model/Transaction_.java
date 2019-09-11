package com.alodiga.wallet.model;

import com.alodiga.wallet.model.BalanceHistory;
import com.alodiga.wallet.model.Close;
import com.alodiga.wallet.model.CommissionItem;
import com.alodiga.wallet.model.PaymentInfo;
import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.PromotionItem;
import com.alodiga.wallet.model.TransactionSource;
import com.alodiga.wallet.model.TransactionType;
import com.alodiga.wallet.model.Withdrawal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T15:19:36")
@StaticMetamodel(Transaction.class)
public class Transaction_ { 

    public static volatile SingularAttribute<Transaction, PaymentInfo> paymentInfoId;
    public static volatile CollectionAttribute<Transaction, PromotionItem> promotionItemCollection;
    public static volatile SingularAttribute<Transaction, Close> closeId;
    public static volatile SingularAttribute<Transaction, String> billPaymentDescription;
    public static volatile SingularAttribute<Transaction, String> externalId;
    public static volatile CollectionAttribute<Transaction, Withdrawal> withdrawalCollection;
    public static volatile CollectionAttribute<Transaction, BalanceHistory> balanceHistoryCollection;
    public static volatile SingularAttribute<Transaction, Product> productId;
    public static volatile SingularAttribute<Transaction, TransactionSource> transactionSourceId;
    public static volatile SingularAttribute<Transaction, Long> id;
    public static volatile SingularAttribute<Transaction, Float> amount;
    public static volatile SingularAttribute<Transaction, Date> creationDate;
    public static volatile SingularAttribute<Transaction, Float> totalAlopointsUsed;
    public static volatile SingularAttribute<Transaction, String> topUpDescription;
    public static volatile SingularAttribute<Transaction, String> additional;
    public static volatile SingularAttribute<Transaction, Float> totalAmount;
    public static volatile SingularAttribute<Transaction, String> additional2;
    public static volatile SingularAttribute<Transaction, TransactionType> transactionTypeId;
    public static volatile SingularAttribute<Transaction, BigInteger> userDestinationId;
    public static volatile SingularAttribute<Transaction, Float> totalTax;
    public static volatile CollectionAttribute<Transaction, CommissionItem> commissionItemCollection;
    public static volatile SingularAttribute<Transaction, Float> promotionAmount;
    public static volatile SingularAttribute<Transaction, String> transactionStatus;
    public static volatile SingularAttribute<Transaction, BigInteger> userSourceId;

}