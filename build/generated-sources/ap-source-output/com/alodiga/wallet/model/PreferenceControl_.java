package com.alodiga.wallet.model;

import com.alodiga.wallet.model.PreferenceField;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-23T14:03:36")
@StaticMetamodel(PreferenceControl.class)
public class PreferenceControl_ { 

    public static volatile SingularAttribute<PreferenceControl, Long> id;
    public static volatile SingularAttribute<PreferenceControl, Date> creationDate;
    public static volatile SingularAttribute<PreferenceControl, BigInteger> customerId;
    public static volatile SingularAttribute<PreferenceControl, PreferenceField> preferenceFieldId;
    public static volatile SingularAttribute<PreferenceControl, BigInteger> userId;
    public static volatile SingularAttribute<PreferenceControl, BigInteger> preferenceId;
    public static volatile SingularAttribute<PreferenceControl, Long> accessCounter;
    public static volatile SingularAttribute<PreferenceControl, String> paramValue;

}