package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.PreferenceField;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-26T16:00:51")
@StaticMetamodel(PreferenceValue.class)
public class PreferenceValue_ { 

    public static volatile SingularAttribute<PreferenceValue, Long> id;
    public static volatile SingularAttribute<PreferenceValue, Date> beginningDate;
    public static volatile SingularAttribute<PreferenceValue, PreferenceField> preferenceFieldId;
    public static volatile SingularAttribute<PreferenceValue, String> value;
    public static volatile SingularAttribute<PreferenceValue, Enterprise> enterpriseId;
    public static volatile SingularAttribute<PreferenceValue, Date> endingDate;

}