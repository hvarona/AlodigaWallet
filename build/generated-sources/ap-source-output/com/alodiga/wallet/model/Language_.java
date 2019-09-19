package com.alodiga.wallet.model;

import com.alodiga.wallet.model.CategoryData;
import com.alodiga.wallet.model.ProductData;
import com.alodiga.wallet.model.PromotionData;
import com.alodiga.wallet.model.PromotionNotification;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-19T15:54:29")
@StaticMetamodel(Language.class)
public class Language_ { 

    public static volatile SingularAttribute<Language, Long> id;
    public static volatile SingularAttribute<Language, Boolean> enabled;
    public static volatile CollectionAttribute<Language, PromotionData> promotionDataCollection;
    public static volatile CollectionAttribute<Language, PromotionNotification> promotionNotificationCollection;
    public static volatile SingularAttribute<Language, String> iso;
    public static volatile CollectionAttribute<Language, CategoryData> categoryDataCollection;
    public static volatile SingularAttribute<Language, String> description;
    public static volatile CollectionAttribute<Language, ProductData> productDataCollection;

}