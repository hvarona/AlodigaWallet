package com.alodiga.wallet.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-11T16:25:57")
@StaticMetamodel(Sms.class)
public class Sms_ { 

    public static volatile SingularAttribute<Sms, Integer> id;
    public static volatile SingularAttribute<Sms, String> sender;
    public static volatile SingularAttribute<Sms, String> content;
    public static volatile SingularAttribute<Sms, String> integratorName;
    public static volatile SingularAttribute<Sms, Date> creationDate;
    public static volatile SingularAttribute<Sms, String> additional;
    public static volatile SingularAttribute<Sms, String> status;
    public static volatile SingularAttribute<Sms, BigInteger> userId;
    public static volatile SingularAttribute<Sms, String> destination;

}