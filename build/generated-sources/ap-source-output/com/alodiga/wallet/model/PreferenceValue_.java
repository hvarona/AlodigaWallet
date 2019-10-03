package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.PreferenceField;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T15:02:10")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T13:29:06")
>>>>>>> JesusMerge
@StaticMetamodel(PreferenceValue.class)
public class PreferenceValue_ { 

    public static volatile SingularAttribute<PreferenceValue, Date> beginningDate;
    public static volatile SingularAttribute<PreferenceValue, Date> endingDate;
    public static volatile SingularAttribute<PreferenceValue, PreferenceField> preferenceFieldId;
    public static volatile SingularAttribute<PreferenceValue, Long> id;
    public static volatile SingularAttribute<PreferenceValue, Enterprise> enterpriseId;
    public static volatile SingularAttribute<PreferenceValue, String> value;

}