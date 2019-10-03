package com.alodiga.wallet.model;

import com.alodiga.wallet.model.BalanceHistory;
import com.alodiga.wallet.model.Category;
import com.alodiga.wallet.model.Commission;
import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.ExchangeDetail;
import com.alodiga.wallet.model.ExchangeRate;
import com.alodiga.wallet.model.ProductData;
import com.alodiga.wallet.model.ProductIntegrationType;
import com.alodiga.wallet.model.Promotion;
import com.alodiga.wallet.model.Transaction;
import com.alodiga.wallet.model.Withdrawal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T15:02:10")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T13:29:06")
>>>>>>> JesusMerge
@StaticMetamodel(Product.class)
public class Product_ { 

    public static volatile CollectionAttribute<Product, Transaction> transactionCollection;
    public static volatile SingularAttribute<Product, ProductIntegrationType> productIntegrationTypeId;
    public static volatile CollectionAttribute<Product, BalanceHistory> balanceHistoryCollection;
    public static volatile SingularAttribute<Product, String> ratesUrl;
    public static volatile SingularAttribute<Product, Boolean> isAlocashProduct;
    public static volatile SingularAttribute<Product, Boolean> enabled;
    public static volatile SingularAttribute<Product, String> accessNumberUrl;
    public static volatile SingularAttribute<Product, Boolean> isFree;
    public static volatile CollectionAttribute<Product, Promotion> promotionCollection;
    public static volatile CollectionAttribute<Product, Commission> commissionCollection;
    public static volatile CollectionAttribute<Product, ExchangeRate> exchangeRateCollection;
    public static volatile SingularAttribute<Product, String> name;
    public static volatile CollectionAttribute<Product, ExchangeDetail> exchangeDetailCollection;
    public static volatile SingularAttribute<Product, Boolean> taxInclude;
    public static volatile CollectionAttribute<Product, ProductData> productDataCollection;
    public static volatile SingularAttribute<Product, Long> id;
    public static volatile SingularAttribute<Product, Enterprise> enterpriseId;
    public static volatile SingularAttribute<Product, String> referenceCode;
    public static volatile CollectionAttribute<Product, Withdrawal> withdrawalCollection;
    public static volatile SingularAttribute<Product, Category> categoryId;

}