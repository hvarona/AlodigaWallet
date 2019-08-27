package com.alodiga.wallet.model;

import com.alodiga.wallet.model.CategoryData;
import com.alodiga.wallet.model.Product;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-26T16:00:51")
@StaticMetamodel(Category.class)
public class Category_ { 

    public static volatile SingularAttribute<Category, Long> id;
    public static volatile SingularAttribute<Category, Boolean> enabled;
    public static volatile CollectionAttribute<Category, CategoryData> categoryDataCollection;
    public static volatile SingularAttribute<Category, String> name;
    public static volatile CollectionAttribute<Category, Product> productCollection;

}