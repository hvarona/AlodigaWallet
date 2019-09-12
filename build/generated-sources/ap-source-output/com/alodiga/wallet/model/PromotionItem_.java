package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Promotion;
import com.alodiga.wallet.model.Transaction;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T16:52:53")
@StaticMetamodel(PromotionItem.class)
public class PromotionItem_ { 

    public static volatile SingularAttribute<PromotionItem, Long> id;
    public static volatile SingularAttribute<PromotionItem, Promotion> promotionId;
    public static volatile SingularAttribute<PromotionItem, Date> promotionApplicationDate;
    public static volatile SingularAttribute<PromotionItem, Transaction> transactionId;
    public static volatile SingularAttribute<PromotionItem, Float> promotionalAmount;
    public static volatile SingularAttribute<PromotionItem, String> comments;

}