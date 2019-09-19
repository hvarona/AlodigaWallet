package com.alodiga.wallet.model;

import com.alodiga.wallet.model.ParameterType;
import com.alodiga.wallet.model.Report;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-19T15:54:29")
@StaticMetamodel(ReportParameter.class)
public class ReportParameter_ { 

    public static volatile SingularAttribute<ReportParameter, Long> id;
    public static volatile SingularAttribute<ReportParameter, Integer> indexOrder;
    public static volatile SingularAttribute<ReportParameter, ParameterType> parameterTypeId;
    public static volatile SingularAttribute<ReportParameter, String> name;
    public static volatile SingularAttribute<ReportParameter, Boolean> required;
    public static volatile SingularAttribute<ReportParameter, String> defaultValue;
    public static volatile SingularAttribute<ReportParameter, Report> reportId;

}