package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Report;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-23T14:03:36")
@StaticMetamodel(ReportType.class)
public class ReportType_ { 

    public static volatile SingularAttribute<ReportType, Long> id;
    public static volatile SingularAttribute<ReportType, String> description;
    public static volatile SingularAttribute<ReportType, String> name;
    public static volatile CollectionAttribute<ReportType, Report> reportCollection;

}