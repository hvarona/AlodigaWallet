package com.alodiga.wallet.model;

import com.alodiga.wallet.model.PreferenceField;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T16:52:53")
@StaticMetamodel(PreferenceType.class)
public class PreferenceType_ { 

    public static volatile SingularAttribute<PreferenceType, Long> id;
    public static volatile CollectionAttribute<PreferenceType, PreferenceField> preferenceFieldCollection;
    public static volatile SingularAttribute<PreferenceType, String> type;

}