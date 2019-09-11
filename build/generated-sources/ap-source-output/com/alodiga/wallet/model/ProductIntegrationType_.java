package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Product;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T15:19:36")
@StaticMetamodel(ProductIntegrationType.class)
public class ProductIntegrationType_ { 

    public static volatile SingularAttribute<ProductIntegrationType, Long> id;
    public static volatile SingularAttribute<ProductIntegrationType, String> name;
    public static volatile CollectionAttribute<ProductIntegrationType, Product> productCollection;

}