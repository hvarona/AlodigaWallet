package com.alodiga.wallet.bean;

import com.alodiga.wallet.model.Category;
import com.alodiga.wallet.model.Country;
import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.Language;
import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.ProductHasProvider;
import com.alodiga.wallet.model.ProductIntegrationType;
import com.alodiga.wallet.model.UserHasProduct;
import com.alodiga.wallet.model.Transaction;
import com.alodiga.wallet.model.Preference;
import com.alodiga.wallet.model.PreferenceField;
import com.alodiga.wallet.model.PreferenceValue;
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
import java.util.Iterator;
import java.math.BigInteger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
import com.alodiga.wallet.respuestas.TransactionResponse;
import com.alodiga.wallet.respuestas.UserHasProductResponse;
import com.alodiga.wallet.respuestas.CountryListResponse;
import com.alodiga.wallet.respuestas.ProductListResponse;
import com.alodiga.wallet.respuestas.PreferenceListResponse;
import com.alodiga.wallet.utils.Constante;
import com.alodiga.wallet.utils.Encryptor;
import com.alodiga.wallet.utils.SendCallRegister;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.Usuario;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import java.sql.Timestamp;
import com.alodiga.wallet.utils.Utils;

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
    
    public TransactionResponse savePaymentShop(String cryptogramShop, String emailUser, Long productId, Float amountPayment,
                                               String conceptTransaction, String cryptogramUser) {
        
        Long idTransaction                      = 12345678910L;
        Long idPreferenceField                  = 12345678910L;
        Long userId                             = 12345678910L;
        int totalTransactionsByUser             = 0;
        Double totalAmountByUser                = 0.00D;
        List<Transaction> transactionsByUser    = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField  = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue  = new ArrayList<PreferenceValue>();
        Timestamp begginingDateTime             = new Timestamp(0);   
        Timestamp endingDateTime                = new Timestamp(0);   
        try {
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS","passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            //Validar preferencias
            //Obtiene las transacciones del día para el usuario
            begginingDateTime = Utils.DateBeggining()[0];
            endingDateTime = Utils.DateBeggining()[1];
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3");
            Query query = entityManager.createNativeQuery(sqlBuilder.toString());
            query.setParameter("1", begginingDateTime);
            query.setParameter("2", endingDateTime);
            query.setParameter("3", userId);
            List result = (List) query.setHint("toplink.refresh", "true").getResultList();
            totalTransactionsByUser = result.size();
            //Obtiene la sumatoria de los montos de las transacciones del usuario
            sqlBuilder = new StringBuilder("SELECT SUM(t.totalAmount) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3");
            query = entityManager.createNativeQuery(sqlBuilder.toString());
            query.setParameter("1", begginingDateTime);
            query.setParameter("2", endingDateTime);
            query.setParameter("3", userId);
            result = (List) query.setHint("toplink.refresh", "true").getResultList();
            totalAmountByUser = result.get(0) != null ? (double) result.get(0) : 0f;
            List<Preference> preferences = getPreferences();
            for(Preference p: preferences){
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for(PreferenceField pf: preferencesField){
                switch(pf.getName()) {
                    case Constante.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf); 
                            for(PreferenceValue pv: preferencesValue){
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT,"The user exceeded the maximum amount per day");
                                }
                            }
                        }
                    break;
                    case Constante.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);                           
                            for(PreferenceValue pv: preferencesValue){
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT,"The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                    break;
                    case Constante.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf); 
                            for(PreferenceValue pv: preferencesValue){
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER,"The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                    break;
                }
            }
            
            //Crear el objeto Transaction para registrar el pago al comercio
            Transaction paymentShop = new Transaction();
            paymentShop.setId(null);
            paymentShop.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            Product product = entityManager.find(Product.class, productId);
            paymentShop.setProductId(product);
            paymentShop.setAmount(amountPayment); 
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        } 
        return new TransactionResponse(ResponseCode.EXITO);
    }
    
    private List<Preference> getPreferences() {
        return entityManager.createNamedQuery("Preference.findAll",Preference.class).getResultList();
    }
    
    private List<Transaction> getTransactions() {
        return entityManager.createNamedQuery("Transaction.findAll",Transaction.class).getResultList();
    }
    
    private List<Transaction> getTransactionsByUser(Long userId) {
	return entityManager.createNamedQuery("Transaction.findByUserSourceId",Transaction.class)
			    .setParameter("userSourceId", userId).getResultList();
    }
    
    private List<PreferenceValue> getPreferenceValuePayment(PreferenceField pf) {
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        Long idPreferenceField = pf.getId();                            
        return preferencesValue = entityManager.createNamedQuery("PreferenceValue.findByPreferenceFieldId", PreferenceValue.class).setParameter("preferenceFieldId",idPreferenceField).getResultList();
    }
}