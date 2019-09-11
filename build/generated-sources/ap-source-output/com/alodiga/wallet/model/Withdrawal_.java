package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Commission;
import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.Transaction;
import com.alodiga.wallet.model.WithdrawalType;
import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T15:19:36")
@StaticMetamodel(Withdrawal.class)
public class Withdrawal_ { 

    public static volatile SingularAttribute<Withdrawal, Long> id;
    public static volatile SingularAttribute<Withdrawal, Commission> commisionId;
    public static volatile SingularAttribute<Withdrawal, WithdrawalType> typeWithdrawalId;
    public static volatile SingularAttribute<Withdrawal, Transaction> transactionId;
    public static volatile SingularAttribute<Withdrawal, BigInteger> userHasBankId;
    public static volatile SingularAttribute<Withdrawal, String> additional;
    public static volatile SingularAttribute<Withdrawal, String> additional2;
    public static volatile SingularAttribute<Withdrawal, BigInteger> userSourceId;
    public static volatile SingularAttribute<Withdrawal, Product> productId;

}