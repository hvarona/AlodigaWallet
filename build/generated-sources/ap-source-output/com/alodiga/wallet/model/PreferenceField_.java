package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Preference;
import com.alodiga.wallet.model.PreferenceControl;
import com.alodiga.wallet.model.PreferenceType;
import com.alodiga.wallet.model.PreferenceValue;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T15:02:10")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T13:29:06")
>>>>>>> JesusMerge
@StaticMetamodel(PreferenceField.class)
public class PreferenceField_ { 

    public static volatile CollectionAttribute<PreferenceField, PreferenceValue> preferenceValueCollection;
    public static volatile SingularAttribute<PreferenceField, Preference> preferenceId;
    public static volatile SingularAttribute<PreferenceField, String> name;
    public static volatile SingularAttribute<PreferenceField, PreferenceType> preferenceTypeId;
    public static volatile SingularAttribute<PreferenceField, Long> id;
    public static volatile CollectionAttribute<PreferenceField, PreferenceControl> preferenceControlCollection;
    public static volatile SingularAttribute<PreferenceField, Short> enabled;

}