package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Promotion;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-23T14:03:36")
@StaticMetamodel(Period.class)
public class Period_ { 

    public static volatile SingularAttribute<Period, Long> id;
    public static volatile SingularAttribute<Period, Integer> days;
    public static volatile SingularAttribute<Period, String> description;
    public static volatile SingularAttribute<Period, String> name;
    public static volatile CollectionAttribute<Period, Promotion> promotionCollection;

}