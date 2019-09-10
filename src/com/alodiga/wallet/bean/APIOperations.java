package com.alodiga.wallet.bean;

import com.alodiga.wallet.model.Category;
import com.alodiga.wallet.model.Country;
import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.Language;
import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.ProductHasProvider;
import com.alodiga.wallet.model.ProductIntegrationType;
import com.alodiga.wallet.model.UserHasProduct;
import com.alodiga.wallet.respuestas.CountryListResponse;
import com.alodiga.wallet.respuestas.ProductListResponse;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;
import com.alodiga.wallet.respuestas.ResponseCode;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ProductResponse;
import com.alodiga.wallet.respuestas.UserHasProductResponse;
import com.alodiga.wallet.utils.Constante;
import com.alodiga.wallet.utils.Encryptor;
import com.alodiga.wallet.utils.SendCallRegister;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.logging.Level;

@Stateless(name = "FsProcessorWallet", mappedName = "ejb/FsProcessorWallet")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class APIOperations {

    @PersistenceContext(unitName = "AlodigaWalletPU")
    private EntityManager entityManager;

    private static final Logger logger = Logger.getLogger(APIOperations.class);

    public ProductResponse saveProduct(Long enterpriseId, Long categoryId, Long productIntegrationTypeId, String name, boolean taxInclude, boolean status, String referenceCode, String rateUrl, String accesNumberURL, boolean isFree, boolean isAlocashProduct) {
        try {
            Product product = new Product();
            product.setId(null);
            Enterprise enterprise = entityManager.find(Enterprise.class, enterpriseId);
            product.setEnterpriseId(enterprise);
            Category category = entityManager.find(Category.class, categoryId);
            product.setCategoryId(category);
            ProductIntegrationType productIntegrationType = entityManager.find(ProductIntegrationType.class, productIntegrationTypeId);
            product.setProductIntegrationTypeId(productIntegrationType);
            product.setName(name);
            product.setTaxInclude(taxInclude);
            product.setEnabled(status);
            product.setReferenceCode(referenceCode);
            product.setRatesUrl(rateUrl);
            product.setAccessNumberUrl(accesNumberURL);
            product.setIsFree(isFree);
            product.setIsAlocashProduct(isAlocashProduct);
            entityManager.persist(product);
        } catch (Exception e) {
            e.printStackTrace();
            return new ProductResponse(ResponseCode.ERROR_INTERNO, "Error in process saving product");
        }
        return new ProductResponse(ResponseCode.EXITO);
    }

    public UserHasProductResponse saveUserHasProduct(Long userId, Long productId) {
        try {
            UserHasProduct userHasProduct = new UserHasProduct();
            userHasProduct.setProductId(productId);
            userHasProduct.setUserSourceId(userId);
            entityManager.persist(userHasProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return new UserHasProductResponse(ResponseCode.ERROR_INTERNO, "Error in process saving product_has_response");
        }
        return new UserHasProductResponse(ResponseCode.EXITO);
    }

    public UserHasProductResponse saveUserHasProductDefault(Long userId) {
        try {
            UserHasProduct userHasProduct = new UserHasProduct();
            userHasProduct.setProductId(Product.ALOCOIN_PRODUCT);
            userHasProduct.setUserSourceId(userId);
            entityManager.persist(userHasProduct);

            UserHasProduct userHasProduct1 = new UserHasProduct();
            userHasProduct1.setProductId(Product.ALODIGA_BALANCE);
            userHasProduct1.setUserSourceId(userId);
            entityManager.persist(userHasProduct1);

            UserHasProduct userHasProduct2 = new UserHasProduct();
            userHasProduct2.setProductId(Product.PREPAID_CARD);
            userHasProduct2.setUserSourceId(userId);
            entityManager.persist(userHasProduct2);

        } catch (Exception e) {
            e.printStackTrace();
            return new UserHasProductResponse(ResponseCode.ERROR_INTERNO, "Error in process saving product_has_response");
        }
        return new UserHasProductResponse(ResponseCode.EXITO);
    }

    public ProductListResponse getProductsByUserId(Long userId) {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        List<Product> products = new ArrayList<Product>();
        try {
            userHasProducts = (List<UserHasProduct>) entityManager.createNamedQuery("UserHasProduct.findByUserSourceId", UserHasProduct.class).setParameter("userSourceId", userId).getResultList();
            
            if (userHasProducts.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }
            
            for (UserHasProduct uhp : userHasProducts) {
                Product product = new Product();
                product = entityManager.find(Product.class, uhp.getProductId());
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.ERROR_INTERNO, "Error loading products");
        }
       

        return new ProductListResponse(ResponseCode.EXITO, "", products);
    }
    
    public CountryListResponse getCountries() {
        List< Country> countries = null;
        try {
            countries = entityManager.createNamedQuery("Country.findAll", Country.class).getResultList();

        } catch (Exception e) {
            return new CountryListResponse(ResponseCode.ERROR_INTERNO, "Error loading countries");
        }
        return new CountryListResponse(ResponseCode.EXITO, "", countries);
    }

}










