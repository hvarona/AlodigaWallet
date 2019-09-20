package com.alodiga.wallet.model;

import com.alodiga.wallet.model.ReportParameter;
import com.alodiga.wallet.model.ReportType;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-20T14:12:20")
@StaticMetamodel(Report.class)
public class Report_ { 

    public static volatile SingularAttribute<Report, Long> id;
    public static volatile SingularAttribute<Report, Boolean> enabled;
    public static volatile SingularAttribute<Report, String> description;
    public static volatile SingularAttribute<Report, String> query;
    public static volatile SingularAttribute<Report, String> name;
    public static volatile SingularAttribute<Report, ReportType> reportTypeId;
    public static volatile CollectionAttribute<Report, ReportParameter> reportParameterCollection;
    public static volatile SingularAttribute<Report, String> webServiceUrl;

}