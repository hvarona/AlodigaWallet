package com.alodiga.wallet.model;

import com.alodiga.wallet.model.BalanceHistory;
import com.alodiga.wallet.model.Category;
import com.alodiga.wallet.model.Commission;
import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.ProductData;
import com.alodiga.wallet.model.ProductIntegrationType;
import com.alodiga.wallet.model.Promotion;
import com.alodiga.wallet.model.Transaction;
import com.alodiga.wallet.model.Withdrawal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-26T16:00:51")
@StaticMetamodel(Product.class)
public class Product_ { 

    public static volatile SingularAttribute<Product, Boolean> isAlocashProduct;
    public static volatile SingularAttribute<Product, Boolean> enabled;
    public static volatile SingularAttribute<Product, String> referenceCode;
    public static volatile SingularAttribute<Product, Category> categoryId;
    public static volatile SingularAttribute<Product, Enterprise> enterpriseId;
    public static volatile SingularAttribute<Product, ProductIntegrationType> productIntegrationTypeId;
    public static volatile SingularAttribute<Product, String> accessNumberUrl;
    public static volatile CollectionAttribute<Product, Withdrawal> withdrawalCollection;
    public static volatile CollectionAttribute<Product, BalanceHistory> balanceHistoryCollection;
    public static volatile SingularAttribute<Product, Long> id;
    public static volatile SingularAttribute<Product, String> ratesUrl;
    public static volatile SingularAttribute<Product, Boolean> isFree;
    public static volatile SingularAttribute<Product, String> name;
    public static volatile SingularAttribute<Product, Boolean> taxInclude;
    public static volatile CollectionAttribute<Product, Transaction> transactionCollection;
    public static volatile CollectionAttribute<Product, ProductData> productDataCollection;
    public static volatile CollectionAttribute<Product, Promotion> promotionCollection;
    public static volatile CollectionAttribute<Product, Commission> commissionCollection;

}