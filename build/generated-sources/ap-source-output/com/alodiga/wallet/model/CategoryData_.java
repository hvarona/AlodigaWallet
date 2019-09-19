package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Category;
import com.alodiga.wallet.model.Language;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-19T15:54:29")
@StaticMetamodel(CategoryData.class)
public class CategoryData_ { 

    public static volatile SingularAttribute<CategoryData, Long> id;
    public static volatile SingularAttribute<CategoryData, String> alias;
    public static volatile SingularAttribute<CategoryData, String> description;
    public static volatile SingularAttribute<CategoryData, Category> categoryId;
    public static volatile SingularAttribute<CategoryData, Language> languageId;

}