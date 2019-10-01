package com.alodiga.wallet.bean;

import com.alodiga.transferto.integration.connection.RequestManager;
import com.alodiga.transferto.integration.model.MSIDN_INFOResponse;
import com.alodiga.transferto.integration.model.ReserveResponse;
import com.alodiga.transferto.integration.model.TopUpResponse;
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
import com.alodiga.wallet.model.PaymentInfo;
import com.alodiga.wallet.model.Provider;
import com.alodiga.wallet.model.TopUpResponseConstants;
import com.alodiga.wallet.model.TransactionType;
import com.alodiga.wallet.model.TransactionSource;
import com.alodiga.wallet.model.TransactionStatus;
import com.alodiga.wallet.model.Commission;
import com.alodiga.wallet.model.CommissionItem;
import com.alodiga.wallet.model.BalanceHistory;
import com.alodiga.wallet.model.ExchangeRate;
import com.alodiga.wallet.model.ExchangeDetail;
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
import com.alodiga.wallet.respuestas.TopUpInfoListResponse;
import com.alodiga.wallet.topup.TopUpInfo;
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
import java.util.HashMap;
import java.util.Map;

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
    
    /*
     *
     */
    public TransactionResponse savePaymentShop(String cryptogramShop, String emailUser, Long productId, Float amountPayment,
                                               String conceptTransaction, String cryptogramUser, Long idUserDestination) {
        
        Long idTransaction                      = 0L;
        Long idPreferenceField                  = 0L;
        Long userId                             = 0L;
        int totalTransactionsByUser             = 0;
        Long totalTransactionsByProduct         = 0L;
        Double totalAmountByUser                = 0.00D;
        List<Transaction> transactionsByUser    = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField  = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue  = new ArrayList<PreferenceValue>();
        List<Commission> commissions            = new ArrayList<Commission>();
        Timestamp begginingDateTime             = new Timestamp(0);   
        Timestamp endingDateTime                = new Timestamp(0); 
        Float amountCommission                  = 0.00F;
        short isPercentCommission               = 0;
        
        try {    
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS","passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            
            // Validar que el balance history del cliente disponga de saldo para hacer la operacion
            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId,productId);
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountPayment) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE,"The user has no balance available to complete the transaction");
            }
            
            //Validar preferencias
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];
            
            //Obtiene las transacciones del día para el usuario
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene la sumatoria de los montos de las transacciones del usuario
            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene las transacciones del día para el producto que se está comprando
            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);
            
            //Cotejar las preferencias vs las transacciones del usuario
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
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
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
            paymentShop.setUserDestinationId(BigInteger.valueOf(idUserDestination));
            Product product = entityManager.find(Product.class, productId);
            paymentShop.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypePS);
            paymentShop.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            paymentShop.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            paymentShop.setCreationDate(creationDate);
            //cambiar por valor de parámetro
            paymentShop.setConcept(Constante.sTransactionConceptPaymentShop);
            paymentShop.setAmount(amountPayment);
            paymentShop.setTransactionStatus(TransactionStatus.CREATED.name());
            paymentShop.setTotalAmount(amountPayment);
            entityManager.persist(paymentShop);
            
            //Revisar si la transacción está sujeta a comisiones
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId",Constante.sTransationTypePS).getResultList();
                for (Commission c: commissions) {
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountPayment * amountCommission)/100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                    //Se crea el objeto commissionItem y se persiste en BD
                    CommissionItem commissionItem = new CommissionItem();
                    commissionItem.setCommissionId(c);           
                    commissionItem.setAmount(amountCommission);
                    Date commissionDate = new Date();
                    Timestamp processedDate = new Timestamp(commissionDate.getTime());
                    commissionItem.setProcessedDate(processedDate);
                    commissionItem.setTransactionId(paymentShop);
                    entityManager.persist(commissionItem);
                }
            } catch (NoResultException e) {
                
            }
            
            //Se actualiza el estatus de la transacción a IN_PROCESS
            paymentShop.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(paymentShop);
            
            //Se actualizan los saldos del cliente y de la tienda en la BD
            //Balance History del cliente
            balanceUserSource = loadLastBalanceHistoryByAccount(userId,productId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
            Float currentAmountUserSource = balanceUserSource.getCurrentAmount() - amountPayment;
            balanceHistory.setCurrentAmount(currentAmountUserSource);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(paymentShop);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserSource.getId());
            entityManager.persist(balanceHistory);
            
            //Balance History de la Tienda
            BalanceHistory balanceUserDestination = loadLastBalanceHistoryByAccount(idUserDestination,productId);
            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(idUserDestination);
            balanceHistory.setOldAmount(balanceUserDestination.getCurrentAmount());
            Float currentAmountUserDestination = balanceUserDestination.getCurrentAmount() - amountCommission;
            balanceHistory.setCurrentAmount(currentAmountUserDestination);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(paymentShop);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserDestination.getId());
            entityManager.persist(balanceHistory);            
            
            //Se actualiza el estado de la transacción a COMPLETED
            paymentShop.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(paymentShop);
            //Envias notificaciones
            //envias sms
            
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        } 
        return new TransactionResponse(ResponseCode.EXITO);
    }
    
    /*
     *
     */
    public TransactionResponse SaveTransferBetweenAccount(String cryptograUserSource, String emailUser, Long productId, Float amountTransfer,
                                                          String conceptTransaction, String cryptograUserDestination, Long idUserDestination) {
        
        Long idTransaction                      = 0L;
        Long idPreferenceField                  = 0L;
        Long userId                             = 0L;
        int totalTransactionsByUser             = 0;
        Long totalTransactionsByProduct         = 0L;
        Double totalAmountByUser                = 0.00D;
        List<Transaction> transactionsByUser    = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField  = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue  = new ArrayList<PreferenceValue>();
        List<Commission> commissions            = new ArrayList<Commission>();
        Timestamp begginingDateTime             = new Timestamp(0);   
        Timestamp endingDateTime                = new Timestamp(0); 
        Float amountCommission                  = 0.00F;
        short isPercentCommission               = 0;
        Commission commissionTransfer           = new Commission();
        
        try {
            
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS","passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            
            // Validar que el balance history del cliente disponga de saldo para hacer la operacion
            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId,productId);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId",Constante.sTransationTypeTA).getResultList();
                for (Commission c: commissions) {
                    commissionTransfer = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountTransfer * amountCommission)/100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }    
            } catch (NoResultException e) {
                
            }
            Float amountTransferTotal = amountTransfer + amountCommission;
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountTransferTotal) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE,"The user has no balance available to complete the transaction");
            }
            
            //Validar preferencias
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];
            
            //Obtiene las transacciones del día para el usuario
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene la sumatoria de los montos de las transacciones del usuario
            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene las transacciones del día para el producto que se está comprando
            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);
            
            //Cotejar las preferencias vs las transacciones del usuario
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
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
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
            
            //Crear el objeto Transaction para registrar la transferencia del cliente
            Transaction transfer = new Transaction();
            transfer.setId(null);
            transfer.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            transfer.setUserDestinationId(BigInteger.valueOf(idUserDestination));
            Product product = entityManager.find(Product.class, productId);
            transfer.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeTA);
            transfer.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            transfer.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            transfer.setCreationDate(creationDate);
            //cambiar por valor de parámetro
            transfer.setConcept(Constante.sTransactionConceptTranferAccounts);
            transfer.setAmount(amountTransfer);
            transfer.setTransactionStatus(TransactionStatus.CREATED.name());
            transfer.setTotalAmount(amountTransfer);
            entityManager.persist(transfer);
            
            //Se crea el objeto commissionItem y se persiste en BD
            CommissionItem commissionItem = new CommissionItem();
            commissionItem.setCommissionId(commissionTransfer);           
            commissionItem.setAmount(amountCommission);
            Date commissionDate = new Date();
            Timestamp processedDate = new Timestamp(commissionDate.getTime());
            commissionItem.setProcessedDate(processedDate);
            commissionItem.setTransactionId(transfer);
            entityManager.persist(commissionItem);
            
            //Se actualiza el estatus de la transacción a IN_PROCESS
            transfer.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(transfer);
            
            //Se actualizan los saldos de los usuarios involucrados en la transferencia
            //Balance History del usuario que transfiere el saldo
            balanceUserSource = loadLastBalanceHistoryByAccount(userId,productId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
            Float currentAmountUserSource = balanceUserSource.getCurrentAmount() - amountTransferTotal;
            balanceHistory.setCurrentAmount(currentAmountUserSource);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserSource.getId());
            entityManager.persist(balanceHistory);
            
            //Balance History del usuario que recibe la transferencia
            BalanceHistory balanceUserDestination = loadLastBalanceHistoryByAccount(idUserDestination,productId);
            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(idUserDestination);
            balanceHistory.setOldAmount(balanceUserDestination.getCurrentAmount());
            Float currentAmountUserDestination = balanceUserDestination.getCurrentAmount() + amountTransfer;
            balanceHistory.setCurrentAmount(currentAmountUserDestination);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserDestination.getId());
            entityManager.persist(balanceHistory);  
            
            //Se actualiza el estado de la transacción a COMPLETED
            transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(transfer);
            //Envias notificaciones
            //envias sms
            
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        } 
        return new TransactionResponse(ResponseCode.EXITO); 
    }
    
    
    /*
     *
     */
    public TransactionResponse ExchangeProduct(String emailUser, Long productSourceId, Long productDestinationId,
                                                Float amountExchange, String conceptTransaction) {
        
        Long idTransaction                      = 0L;
        Long idPreferenceField                  = 0L;
        Long userId                             = 0L;
        int totalTransactionsByUser             = 0;
        Long totalTransactionsByProduct         = 0L;
        Double totalAmountByUser                = 0.00D;
        List<Transaction> transactionsByUser    = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField  = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue  = new ArrayList<PreferenceValue>();
        List<Commission> commissions            = new ArrayList<Commission>();
        Timestamp begginingDateTime             = new Timestamp(0);   
        Timestamp endingDateTime                = new Timestamp(0); 
        Float amountCommission                  = 0.00F;
        short isPercentCommission               = 0;
                
        try {
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS","passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            //Validar preferencias
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];

            //Obtiene las transacciones del día para el usuario
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene la sumatoria de los montos de las transacciones del usuario
            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            //Obtiene las transacciones del día para el producto que se está comprando
            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productSourceId, userId, begginingDateTime, endingDateTime);

            //Cotejar las preferencias vs las transacciones del usuario
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
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
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

            //Se calcula el monto de la conversión entre los productos
            ExchangeRate RateByProductSource = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productSourceId).getSingleResult();
            ExchangeRate RateByProductDestination = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productDestinationId).getSingleResult();
            Float amountConversion = (amountExchange * RateByProductSource.getValue()) / RateByProductDestination.getValue();
            
            //Registrar el intercambio de los productos (producto de origen)
            Transaction exchange = new Transaction();
            exchange.setId(null);
            exchange.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            Product productSource = entityManager.find(Product.class, productSourceId);
            exchange.setProductId(productSource);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeEP);
            exchange.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            exchange.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            exchange.setCreationDate(creationDate);
            //cambiar por valor de parámetro
            exchange.setConcept(Constante.sTransactionConceptExchangeProducts);
            exchange.setAmount(amountExchange);
            exchange.setTransactionStatus(TransactionStatus.CREATED.name());
            exchange.setTotalAmount(amountExchange);
            entityManager.persist(exchange);
            
            //Se calcula la comisión asociada al producto de origen
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productSourceId).setParameter("transactionTypeId",Constante.sTransationTypeEP).getResultList();
                for (Commission c: commissions) {
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountExchange * amountCommission)/100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                    //Se crea el objeto commissionItem y se persiste en BD
                    CommissionItem commissionItem = new CommissionItem();
                    commissionItem.setCommissionId(c);           
                    commissionItem.setAmount(amountCommission);
                    Date commissionDate = new Date();
                    Timestamp processedDate = new Timestamp(commissionDate.getTime());
                    commissionItem.setProcessedDate(processedDate);
                    commissionItem.setTransactionId(exchange);
                    entityManager.persist(commissionItem);
                }    
            } catch (NoResultException e) {
                
            }
            
            //Se actualiza el estatus de la transacción a IN_PROCESS
            exchange.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(exchange);
                
            //Guardar los detalles del intercambio (producto destino)
            ExchangeDetail detailProductDestination = new ExchangeDetail();
            detailProductDestination.setId(null);
            detailProductDestination.setExchangeRateId(RateByProductDestination);
            Product productDestination = entityManager.find(Product.class, productDestinationId);
            detailProductDestination.setProductId(productDestination);
            detailProductDestination.setTransactionId(exchange);
            entityManager.persist(detailProductDestination);
            
            //Se actualiza el saldo del usuario para el producto de origen
            BalanceHistory balanceProductSource = loadLastBalanceHistoryByAccount(userId,productSourceId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceProductSource.getCurrentAmount());
            Float currentAmountProductSource = balanceProductSource.getCurrentAmount() - amountExchange;
            balanceHistory.setCurrentAmount(currentAmountProductSource);
            balanceHistory.setProductId(productSource);
            balanceHistory.setTransactionId(exchange);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceProductSource.getId());
            entityManager.persist(balanceHistory);
            
            //Se actualiza el saldo del usuario para el producto de destino
            BalanceHistory balanceProductDestination = loadLastBalanceHistoryByAccount(userId,productDestinationId);
            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceProductDestination.getCurrentAmount());
            Float currentAmountProductDestination = balanceProductDestination.getCurrentAmount() + amountConversion;
            balanceHistory.setCurrentAmount(currentAmountProductDestination);
            balanceHistory.setProductId(productDestination);
            balanceHistory.setTransactionId(exchange);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceProductDestination.getId());
            entityManager.persist(balanceHistory);
            
            //Se actualiza el estado de la transacción a COMPLETED
            exchange.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(exchange);
            //Envias notificaciones
            //envias sms
            
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        } 
        
        return new TransactionResponse(ResponseCode.EXITO); 
    }
    
    /*
     *
     */
    public TransactionResponse ManualWithdrawals(Long bankId, String emailUser, String accountBank, 
                                                 Float amountWithdrawal, Long productId) {
        
        int totalTransactionsByUser             = 0;
        Long totalTransactionsByProduct         = 0L;
        Double totalAmountByUser                = 0.00D;
        List<Transaction> transactionsByUser    = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField  = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue  = new ArrayList<PreferenceValue>();
        List<Commission> commissions            = new ArrayList<Commission>();
        Timestamp begginingDateTime             = new Timestamp(0);   
        Timestamp endingDateTime                = new Timestamp(0); 
        Float amountCommission                  = 0.00F;
        short isPercentCommission               = 0;
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        } 
        return new TransactionResponse(ResponseCode.EXITO);
    }
    
    /*
     *
     */
    private List<Preference> getPreferences() {
        return entityManager.createNamedQuery("Preference.findAll",Preference.class).getResultList();
    }
    
    private List<PreferenceValue> getPreferenceValuePayment(PreferenceField pf) {
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        Long idPreferenceField = pf.getId();                            
        return preferencesValue = entityManager.createNamedQuery("PreferenceValue.findByPreferenceFieldId", PreferenceValue.class).setParameter("preferenceFieldId",idPreferenceField).getResultList();
    }  
    

    public BalanceHistory loadLastBalanceHistoryByAccount(Long userId, Long productId) {
        Query query = entityManager.createQuery("SELECT b FROM BalanceHistory b WHERE b.userId = " + userId + " AND b.productId.id = " + productId + " ORDER BY b.id desc");
        query.setMaxResults(1);
        BalanceHistory result = (BalanceHistory) query.setHint("toplink.refresh", "true").getSingleResult();
        return result;
    }
    
    public int TransactionsByUserCurrentDate(Long userId, Timestamp begginingDateTime, Timestamp endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.size();
    }
    
    public Double AmountMaxByUserCurrentDate(Long userId, Timestamp begginingDateTime, Timestamp endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT SUM(t.totalAmount) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (double) result.get(0) : 0f;
    }
    
    public Long TransactionsByProductByUserCurrentDate(Long productId, Long userId, Timestamp begginingDateTime, Timestamp endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(t.productId) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3 AND t.productId = ?4");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        query.setParameter("4",productId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (Long) result.get(0) : 0l;
    }
    
}

