package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Language;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T13:29:06")
@StaticMetamodel(PromotionNotification.class)
public class PromotionNotification_ { 

    public static volatile SingularAttribute<PromotionNotification, String> promotionType;
    public static volatile SingularAttribute<PromotionNotification, String> ackSmsText;
    public static volatile SingularAttribute<PromotionNotification, Language> languageId;
    public static volatile SingularAttribute<PromotionNotification, String> ackMailFrom;
    public static volatile SingularAttribute<PromotionNotification, String> ackMailSubject;
    public static volatile SingularAttribute<PromotionNotification, String> ackMailHtml;
    public static volatile SingularAttribute<PromotionNotification, Long> id;
    public static volatile SingularAttribute<PromotionNotification, String> ackSmsFrom;
    public static volatile SingularAttribute<PromotionNotification, Boolean> enabled;

}