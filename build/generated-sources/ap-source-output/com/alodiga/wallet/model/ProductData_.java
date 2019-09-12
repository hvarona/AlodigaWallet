package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Language;
import com.alodiga.wallet.model.Product;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T16:52:53")
@StaticMetamodel(ProductData.class)
public class ProductData_ { 

    public static volatile SingularAttribute<ProductData, Long> id;
    public static volatile SingularAttribute<ProductData, String> alias;
    public static volatile SingularAttribute<ProductData, String> description;
    public static volatile SingularAttribute<ProductData, Language> languageId;
    public static volatile SingularAttribute<ProductData, Product> productId;

}