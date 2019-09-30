package com.alodiga.wallet.model;

import com.alodiga.wallet.model.PreferenceField;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-27T13:34:56")
@StaticMetamodel(Preference.class)
public class Preference_ { 

    public static volatile SingularAttribute<Preference, String> name;
    public static volatile SingularAttribute<Preference, String> description;
    public static volatile CollectionAttribute<Preference, PreferenceField> preferenceFieldCollection;
    public static volatile SingularAttribute<Preference, Long> id;
    public static volatile SingularAttribute<Preference, Boolean> enabled;

}