package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Period;
import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.PromotionData;
import com.alodiga.wallet.model.PromotionItem;
import com.alodiga.wallet.model.TransactionType;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T16:52:53")
@StaticMetamodel(Promotion.class)
public class Promotion_ { 

    public static volatile SingularAttribute<Promotion, Boolean> enabled;
    public static volatile SingularAttribute<Promotion, Period> periodId;
    public static volatile SingularAttribute<Promotion, String> promotionType;
    public static volatile CollectionAttribute<Promotion, PromotionItem> promotionItemCollection;
    public static volatile SingularAttribute<Promotion, String> promotionalAction;
    public static volatile SingularAttribute<Promotion, Integer> promotionValidityDays;
    public static volatile SingularAttribute<Promotion, Product> productId;
    public static volatile SingularAttribute<Promotion, Long> id;
    public static volatile SingularAttribute<Promotion, Date> beginningDate;
    public static volatile SingularAttribute<Promotion, Boolean> isPercentage;
    public static volatile CollectionAttribute<Promotion, PromotionData> promotionDataCollection;
    public static volatile SingularAttribute<Promotion, String> name;
    public static volatile SingularAttribute<Promotion, TransactionType> transactionTypeId;
    public static volatile SingularAttribute<Promotion, Float> promotionalAmount;
    public static volatile SingularAttribute<Promotion, Date> endingDate;
    public static volatile SingularAttribute<Promotion, Float> goalAmount;

}