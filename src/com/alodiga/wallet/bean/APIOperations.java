package com.alodiga.wallet.bean;

import com.alodiga.transferto.integration.connection.RequestManager;
import com.alodiga.transferto.integration.model.MSIDN_INFOResponse;
import com.alodiga.transferto.integration.model.ReserveResponse;
import com.alodiga.transferto.integration.model.TopUpResponse;
import com.alodiga.wallet.model.Bank;
import com.alodiga.wallet.model.BankOperation;
import com.alodiga.wallet.model.BankOperationMode;
import com.alodiga.wallet.model.BankOperationType;
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
import com.alodiga.wallet.model.BankHasProduct;
import com.alodiga.wallet.model.ExchangeRate;
import com.alodiga.wallet.model.ExchangeDetail;
import com.alodiga.wallet.model.TopUpCountry;
import com.alodiga.wallet.response.generic.BankGeneric;
import com.alodiga.wallet.respuestas.BalanceHistoryResponse;
import com.alodiga.wallet.respuestas.BankListResponse;
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
import com.alodiga.wallet.respuestas.TransactionListResponse;
import com.alodiga.wallet.respuestas.UserHasProductResponse;
import com.alodiga.wallet.respuestas.CountryListResponse;
import com.alodiga.wallet.respuestas.LanguageListResponse;
import com.alodiga.wallet.respuestas.ProductListResponse;
import com.alodiga.wallet.respuestas.PreferenceListResponse;
import com.alodiga.wallet.respuestas.TopUpCountryListResponse;
import com.alodiga.wallet.respuestas.TopUpInfoListResponse;
import com.alodiga.wallet.respuestas.TransactionListResponse;
import com.alodiga.wallet.respuestas.TransactionResponse;
import com.alodiga.wallet.topup.TopUpInfo;
import com.alodiga.wallet.utils.Constante;
import com.alodiga.wallet.utils.Constants;
import com.alodiga.wallet.utils.Encryptor;
import com.alodiga.wallet.utils.EnvioCorreo;
import com.alodiga.wallet.utils.Mail;
import com.alodiga.wallet.utils.SendCallRegister;
import com.alodiga.wallet.utils.SendMailTherad;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.Usuario;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import java.sql.Timestamp;
import com.alodiga.wallet.utils.Utils;
import com.ericsson.alodiga.ws.Cuenta;
import java.util.HashMap;
import java.util.Map;

@Stateless(name = "FsProcessorWallet", mappedName = "ejb/FsProcessorWallet")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class APIOperations {

    @PersistenceContext(unitName = "AlodigaWalletPU")
    private EntityManager entityManager;
    private static final Logger logger = Logger.getLogger(APIOperations.class);

    
    
    public ProductResponse saveProduct(Long enterpriseId, Long categoryId, Long productIntegrationTypeId, String name, boolean taxInclude, boolean status, String referenceCode, String rateUrl, String accesNumberURL, boolean isFree, boolean isAlocashProduct, String symbol) {
        try {
            //t
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
            product.setSymbol(symbol);
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
            userHasProduct.setBeginningDate(new Timestamp(new Date().getTime()));
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
            userHasProduct.setBeginningDate(new Timestamp(new Date().getTime()));
            entityManager.persist(userHasProduct);

            UserHasProduct userHasProduct1 = new UserHasProduct();
            userHasProduct1.setProductId(Product.ALODIGA_BALANCE);
            userHasProduct1.setUserSourceId(userId);
            userHasProduct1.setBeginningDate(new Timestamp(new Date().getTime()));
            entityManager.persist(userHasProduct1);

            UserHasProduct userHasProduct2 = new UserHasProduct();
            userHasProduct2.setProductId(Product.PREPAID_CARD);
            userHasProduct2.setUserSourceId(userId);
            userHasProduct2.setBeginningDate(new Timestamp(new Date().getTime()));
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
    
    
    
    public BankListResponse getBankApp() {
        List<Bank> banks = null;
        try {
            banks = entityManager.createNamedQuery("Bank.findAll", Bank.class).getResultList();            
        } catch (Exception e) {
            return new BankListResponse(ResponseCode.ERROR_INTERNO, "Error loading bank");
        }
        ArrayList<BankGeneric> bankGenerics = new ArrayList<BankGeneric>();
        for(Bank b : banks){
            BankGeneric bankGeneric = new BankGeneric(b.getId().toString(),b.getName(),b.getAba());
            bankGenerics.add(bankGeneric);
        }

        return new BankListResponse(ResponseCode.EXITO, "", bankGenerics);
       
    }
    
    
        public BankListResponse getBankByCountryApp(Long countryId) {
        List<Bank> banks = new ArrayList<Bank>();
        List<Country> countrys = new ArrayList<Country>();
        List<BankGeneric> bankGenerics = new ArrayList<BankGeneric>();
        try {
            banks = (List<Bank>) entityManager.createNamedQuery("Bank.findByCountryIdBank", Bank.class).setParameter("countryId", countryId).getResultList();
            
            if (banks.size() <= 0) {
                return new BankListResponse(ResponseCode.ERROR_INTERNO, "Lista de banco vacia");
            }
            
            for(Bank b: banks){
                BankGeneric bankGeneric = new BankGeneric(b.getId().toString(),b.getName(), b.getAba());
                bankGenerics.add(bankGeneric);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new BankListResponse(ResponseCode.ERROR_INTERNO, "Error loading bank");
        }
       

        return new BankListResponse(ResponseCode.EXITO, "",bankGenerics);
    }
    
    
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
         ArrayList<Product> products = new ArrayList<Product>(); 
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
            
            //Obtiene las transacciones del dÃ­a para el usuario
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene la sumatoria de los montos de las transacciones del usuario
            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene las transacciones del dÃ­a para el producto que se estÃ¡ comprando
            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);
            
            //Cotejar las preferencias vs las transacciones del usuario
            List<Preference> preferences = getPreferences();
            for(Preference p: preferences){
                if (p.getName().equals(Constants.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for(PreferenceField pf: preferencesField){
                switch(pf.getName()) {
                    case Constants.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf); 
                            for(PreferenceValue pv: preferencesValue){
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT,"The user exceeded the maximum amount per day");
                                }
                            }
                        }
                    break;
                    case Constants.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);                           
                            for(PreferenceValue pv: preferencesValue){
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT,"The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                    break;
                    case Constants.sValidatePreferenceTransaction3:
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
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constants.sTransactionSource);
            paymentShop.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            paymentShop.setCreationDate(creationDate);
            //cambiar por valor de parÃ¡metro
            paymentShop.setConcept(Constante.sTransactionConceptPaymentShop);
            paymentShop.setAmount(amountPayment);
            paymentShop.setTransactionStatus(TransactionStatus.CREATED.name());
            paymentShop.setTotalAmount(amountPayment);
            entityManager.persist(paymentShop);
            
            //Revisar si la transacciÃ³n estÃ¡ sujeta a comisiones
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId",Constante.sTransationTypePS).getResultList();
                if(commissions.size() < 1){
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: "+ userId);
                }  
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
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
            }
            
            //Se actualiza el estatus de la transacciÃ³n a IN_PROCESS
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
            if (balanceUserDestination == null) {
                balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                balanceHistory.setCurrentAmount(amountPayment - amountCommission);
            } else {
                balanceHistory.setOldAmount(balanceUserDestination.getCurrentAmount());
                Float currentAmountUserDestination = (balanceUserDestination.getCurrentAmount()+amountPayment) - amountCommission;
                balanceHistory.setCurrentAmount(currentAmountUserDestination);
                balanceHistory.setVersion(balanceUserDestination.getId());
            }
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(paymentShop);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            
            entityManager.persist(balanceHistory);            
            
            //Se actualiza el estado de la transacciÃ³n a COMPLETED
            paymentShop.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(paymentShop);
            //Envias notificaciones
            //envias sms

            ////////////////////////////////////////////////////////////
           /// se incorpora para delvolver el saldo actual del cliente///
            /////////////////////////////////////////////////////////////
            products = getProductsListByUserId(userId);
            for(Product p: products){
                Float amount = 0F;
                try {
                    amount = loadLastBalanceHistoryByAccount_(userId,  p.getId()).getCurrentAmount();
                } catch (NoResultException e) {
                    amount = 0F;        
                }
                 p.setCurrentBalance(amount);   
            }
            
            ////////////////////////////////////////////////////////////
           /// se incorpora para delvolver el saldo actual del cliente///
            /////////////////////////////////////////////////////////////
            
            
        
        Usuario usuario = new Usuario();
        usuario.setEmail(emailUser);
        SendMailTherad sendMailTherad = new SendMailTherad("ES", amountPayment, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido() , emailUser, Integer.valueOf("3"));
        sendMailTherad.run();

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        } 
        
        
        
        
     
        return new TransactionResponse(ResponseCode.EXITO,"",products);
    }
    
/*
     *
     */
    public TransactionResponse saveTransferBetweenAccount(String cryptograUserSource, String emailUser, Long productId, Float amountTransfer,
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
        ArrayList<Product> products = new ArrayList<Product>(); 
        
        try {
            
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS","passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            
            // Validar que el balance history del cliente disponga de saldo para hacer la operacion
            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId,productId);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId",Constante.sTransationTypeTA).getResultList();
                if(commissions.size() < 1){
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: "+ userId);
                }  
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
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
            }
            Float amountTransferTotal = amountTransfer + amountCommission;
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountTransferTotal) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE,"The user has no balance available to complete the transaction");
            }
            
            //Validar preferencias
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];
            
            //Obtiene las transacciones del dÃ­a para el usuario
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene la sumatoria de los montos de las transacciones del usuario
            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene las transacciones del dÃ­a para el producto que se estÃ¡ comprando
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
            //cambiar por valor de parÃ¡metro
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
            
            //Se actualiza el estatus de la transaccion a IN_PROCESS
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
            if (balanceUserDestination == null) {
                balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                balanceHistory.setCurrentAmount(amountTransfer);
            } else {
                balanceHistory.setOldAmount(balanceUserDestination.getCurrentAmount());
                Float currentAmountUserDestination = balanceUserDestination.getCurrentAmount() + amountTransfer;
                balanceHistory.setCurrentAmount(currentAmountUserDestination);
                balanceHistory.setVersion(balanceUserDestination.getId());
            }
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            entityManager.persist(balanceHistory);  
            
            //Se actualiza el estado de la transaccion a COMPLETED
            transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(transfer);
            //Envias notificaciones
            //envias sms
            
            
                        ////////////////////////////////////////////////////////////
           /// se incorpora para delvolver el saldo actual del cliente///
            /////////////////////////////////////////////////////////////
            products = getProductsListByUserId(userId);
            for(Product p: products){
                Float amount = 0F;
                try {
                    amount = loadLastBalanceHistoryByAccount_(userId,  p.getId()).getCurrentAmount();
                } catch (NoResultException e) {
                    amount = 0F;        
                }
                 p.setCurrentBalance(amount);   
            }
            
           ////////////////////////////////////////////////////////////
           /// se incorpora para delvolver el saldo actual del cliente///
           /////////////////////////////////////////////////////////////

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        } 
        return new TransactionResponse(ResponseCode.EXITO,"",products); 
    }
    
    
    public TransactionResponse previewExchangeProduct(String emailUser, Long productSourceId, Long productDestinationId, 
                                                      Float amountExchange, int includedAmount) {
        
        Long userId                             = 0L;
        Float amountCommission                  = 0.00F;
        List<Commission> commissions            = new ArrayList<Commission>();
        short isPercentCommission               = 0;
        Float valueCommission                   = 0.00F;
        Float totalDebit                        = 0.00F;
        Float amountConversion                  = 0.00F;
        Float valueRateByProductSource          = 0.00F;
        Float valueRateByProductDestination     = 0.00F;;
        
        try {
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS","passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            
            // Validar que el cliente disponga de saldo para hacer la operacion
            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId,productSourceId);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productSourceId).setParameter("transactionTypeId",Constante.sTransationTypeEP).getResultList();
                if(commissions.size() < 1){
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productSourceId + " and userId: "+ userId);
                }  
                for (Commission c: commissions) {
                    valueCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && valueCommission > 0) {
                        amountCommission = (amountExchange * valueCommission)/100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
            }
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < totalDebit) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE,"The user has no balance available to complete the transaction");
            }
            
            //Calcular el total a debitar dependiendo si se incluye o no la comision
            if (includedAmount == 0) {
                totalDebit = amountExchange + amountCommission;
            } else {
                totalDebit = amountExchange - amountCommission;
            }
            
            //Se calcula el monto de la conversion entre los productos
            ExchangeRate RateByProductSource = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productSourceId).getSingleResult();
            valueRateByProductSource = RateByProductSource.getValue();
            ExchangeRate RateByProductDestination = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productDestinationId).getSingleResult();
            valueRateByProductDestination = RateByProductDestination.getValue();
            amountConversion = (amountExchange * RateByProductSource.getValue()) / RateByProductDestination.getValue();
            
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        } 
        return new TransactionResponse(ResponseCode.EXITO,"",amountCommission,valueCommission,totalDebit,
                                       amountConversion,valueRateByProductSource,valueRateByProductDestination); 
    }
    
    /*
     *
     */
    public TransactionResponse exchangeProduct(String emailUser, Long productSourceId, Long productDestinationId,
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

            //Obtiene las transacciones del dÃ­a para el usuario
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene la sumatoria de los montos de las transacciones del usuario
            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            //Obtiene las transacciones del dÃ­a para el producto que se estÃ¡ comprando
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

            //Se calcula el monto de la conversiÃ³n entre los productos
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
            exchange.setConcept(conceptTransaction);
            exchange.setAmount(amountExchange);
            exchange.setTransactionStatus(TransactionStatus.CREATED.name());
            exchange.setTotalAmount(amountExchange);
            entityManager.persist(exchange);
            
            //Se calcula la comision asociada al producto de origen
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productSourceId).setParameter("transactionTypeId",Constante.sTransationTypeEP).getResultList();
                if(commissions.size() < 1){
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productSourceId + " and userId: "+ userId);
                }
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
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
            }
            
            //Se actualiza el estatus de la transaccion a IN_PROCESS
            exchange.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(exchange);
                
            //Guardar los detalles del intercambio entre los productos (para producto destino)
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
            if (balanceProductDestination == null) {
                balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                balanceHistory.setCurrentAmount(amountConversion);
            } else {
                balanceHistory.setOldAmount(balanceProductDestination.getCurrentAmount());
                Float currentAmountUserDestination = balanceProductDestination.getCurrentAmount() + amountConversion;
                balanceHistory.setCurrentAmount(currentAmountUserDestination);
                balanceHistory.setVersion(balanceProductDestination.getId());
            }
            balanceHistory.setProductId(productDestination);
            balanceHistory.setTransactionId(exchange);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            entityManager.persist(balanceHistory);
            
            //Se actualiza el estado de la transaccion a COMPLETED
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
    
     public BalanceHistory loadLastBalanceHistoryByAccount(Long userId, Long productId) {
        try {
            Query query = entityManager.createQuery("SELECT b FROM BalanceHistory b WHERE b.userId = " + userId + " AND b.productId.id = " + productId + " ORDER BY b.id desc");
            query.setMaxResults(1);
            BalanceHistory result = (BalanceHistory) query.setHint("toplink.refresh", "true").getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
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
    
  
    private Provider getProviderById(Long providerId) {
        return entityManager.createNamedQuery("Provider.findById",Provider.class).setParameter("id", providerId)
                .getSingleResult();
    }
    
    public TopUpInfoListResponse getTopUpInfo(String receiverNumber, String phoneNumber) {

        Provider provider = null;
        try {
            provider = getProviderById(1L);
        } catch (Exception ex) {
            return new TopUpInfoListResponse(ResponseCode.ERROR_INTERNO, "Error al buscar el proveedor");
        }
        Float percentAditional = provider.getAditionalPercent();
        
      
        MSIDN_INFOResponse inf = null;
       
        List<TopUpInfo> topUpInfos = new ArrayList<TopUpInfo>();
        if (receiverNumber == null) {
             return new TopUpInfoListResponse(ResponseCode.ERROR_INTERNO, "Error parametro receiver null");
        }
        try {
            inf = RequestManager.getMsisdn_ingo(receiverNumber);
            String[] productIds = null;
            String skuid = null;        
            String[] skuids = null;
            String[] productRetailsPrices = null;
            String[] denominationsReceiver = null;
            String[] productWholesalePrices = null;


            skuid = inf.getSkuid();
            if (skuid != null) {
                TopUpInfo topUpInfo = new TopUpInfo();
                topUpInfo.setCountry(inf.getCountry());
                topUpInfo.setCoutryId(inf.getCountryId());
                topUpInfo.setIsOpenRange(true);
                topUpInfo.setOpertador(inf.getOperator());
                topUpInfo.setOperatorid(inf.getOperatorId());
                topUpInfo.setDestinationCurrency(inf.getDestinationCurrency());
                
                topUpInfo.setSkuid(inf.getSkuid());
                topUpInfo.setMinimumAmount(Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency()));
                topUpInfo.setMaximumAmount(Float.parseFloat(inf.getOpen_range_maximum_amount_local_currency()));
                topUpInfo.setIncrement(Float.parseFloat(inf.getOpen_range_increment_local_currency()));
                Float amount = Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency()) +Float.parseFloat(inf.getOpen_range_increment_local_currency());            
                TopUpResponse topUpResponse = RequestManager.simulationDoTopUp(phoneNumber,receiverNumber , inf.getOpen_range_minimum_amount_requested_currency(), inf.getSkuid());
                Float wholesalePrice = Float.parseFloat(topUpResponse.getWholesalePrice());
                topUpInfo.setWholesalePrice(wholesalePrice);
                Float realRetailPrice;
                Float commissionPercent;
                //solo se adiciona si es cuba
                if (inf.getCountry().equals("Cuba")){
                     realRetailPrice = Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency()) + (Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency())*percentAditional/100);
                }else{
                      realRetailPrice =  Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency());
                }
                commissionPercent = ((realRetailPrice- wholesalePrice)/realRetailPrice)*100;
                topUpInfo.setCommissionPercent(commissionPercent);
                topUpInfos.add(topUpInfo);
            } else {
                productIds = inf.getProduct_list().split(",");
                skuids = inf.getSkuid_list().split(",");
                productRetailsPrices = inf.getRetail_price_list().split(",");
                productWholesalePrices = inf.getWholesale_price_list().split(",");
                denominationsReceiver = inf.getLocal_info_amount_list().split(",");
               
                List<Float> denominationsReceivers = new ArrayList<Float>();
                for (int i = 0; i < productIds.length; i++) {
                    TopUpInfo topUpInfo = new TopUpInfo();
                    topUpInfo.setCountry(inf.getCountry());
                    topUpInfo.setCoutryId(inf.getCountryId());
                    topUpInfo.setIsOpenRange(false);
                    topUpInfo.setOpertador(inf.getOperator());
                    topUpInfo.setOperatorid(inf.getOperatorId());
                    topUpInfo.setDestinationCurrency(inf.getLocal_info_currency());
                    
                    topUpInfo.setSkuid(skuids[i]);
                    Float retailPrice = Float.parseFloat(productRetailsPrices[i]);
                    Float wholesalePrice = Float.parseFloat(productWholesalePrices[i]);
                    topUpInfo.setWholesalePrice(wholesalePrice);
                    Float realRetailPrice;
                    Float commissionPercent;
                    //solo se adiciona si es cuba
                    if (inf.getCountry().equals("Cuba")) {
                       realRetailPrice = (retailPrice) +  ((retailPrice * percentAditional)/100);
                    } else {
                        realRetailPrice = retailPrice;
                    }
                    commissionPercent = ((realRetailPrice - wholesalePrice) / realRetailPrice) * 100;
                    topUpInfo.setCommissionPercent(commissionPercent);
                    topUpInfo.setDenominationSale(realRetailPrice);
                    topUpInfo.setDenomination(retailPrice);
                    topUpInfo.setDenominationReceiver(Float.parseFloat(denominationsReceiver[i]));
                    topUpInfos.add(topUpInfo);
                }
               
            }
        

        } catch (Exception ex) {
            return new TopUpInfoListResponse(ResponseCode.ERROR_INTERNO, "Error en el metodo getTopUpInfs");
        }
        return new TopUpInfoListResponse(ResponseCode.EXITO, "", topUpInfos);
    }
    
    
    private TransactionResponse executeTopUp(String skuidIdRequest, Float amount, String destinationNumber, String senderNumber) {
        TransactionResponse transaction = new TransactionResponse();
        String phoneNumber = destinationNumber;
         try {
             MSIDN_INFOResponse response1 = RequestManager.getMsisdn_ingo(phoneNumber);
             String skuidId = response1.getSkuid();
             if (response1.getSkuid() == null) {
                String[] Skuids = response1.getSkuid_list().split(",");
                String[] products = response1.getProduct_list().split(",");
                for (int o = 0; o < products.length; o++) {
                  if (Float.parseFloat(products[o])==amount ) {
                    skuidId = Skuids[o];
                  }
                }
             }
             if (skuidId.equals(skuidIdRequest)) {
                 ReserveResponse response2 = RequestManager.getReserve();
                 TopUpResponse topUpResponseExecute = RequestManager.simulationDoTopUp(senderNumber, phoneNumber, amount.toString(), skuidId);
//             TopUpResponse topUpResponseExecute = RequestManager.newDoTopUp(senderNumber, phoneNumber, amount.toString(), skuidId, response2.getReserved_id());
                 String code = topUpResponseExecute.getErrorCode();
                 if (!code.equals("0")) {//Cuando es 0 esta bien...
                     StringBuilder errorBuilder = new StringBuilder(TopUpResponseConstants.TRANSFER_TO_CODES.get(code));
                     errorBuilder.append("Integrator = ").append("TransferTo").append("ProductId = ").append(response1.getOperatorId()).append("phoneNumber = ").append(destinationNumber);
                     if (code.equals("301") || topUpResponseExecute.getErrorText().equals("Denomination not available")) {
                         transaction.setCodigoRespuesta(ResponseCode.DENOMINATION_NOT_AVAILABLE.getCodigo());
                         transaction.setMensajeRespuesta("DENOMINATION NOT AVAILABLE");
                     } else if (code.equals("101") || topUpResponseExecute.getErrorText().equals("Destination MSISDN out of range")) {
                         transaction.setCodigoRespuesta(ResponseCode.DESTINATION_MSISDN_OUT_OF_RANGE.getCodigo());
                         transaction.setMensajeRespuesta("DESTINATION MSISDN OUT OF RANGE");
                     } else if (code.equals("204")) {
                         transaction.setCodigoRespuesta(ResponseCode.DESTINATION_NOT_PREPAID.getCodigo());
                         transaction.setMensajeRespuesta("DESTINATION NOT PREPAID");
                     } else {
                         transaction.setCodigoRespuesta(ResponseCode.ERROR_TRANSACTION_TOP_UP.getCodigo());
                         transaction.setMensajeRespuesta("ERROR TRANSACTION TOP UP");
                     }
                 } else {
                     transaction.setCodigoRespuesta(ResponseCode.EXITO.getCodigo());
                     transaction.setMensajeRespuesta("TOPUP TRANSACTION SUCCESSFUL");
                 }
             }else{
                transaction.setCodigoRespuesta(ResponseCode.DENOMINATION_NOT_AVAILABLE.getCodigo());
                transaction.setMensajeRespuesta("DENOMINATION NOT AVAILABLE");
             }
        } catch (Exception ex) {
            transaction.setCodigoRespuesta(ResponseCode.ERROR_TRANSACTION_TOP_UP.getCodigo());
            transaction.setMensajeRespuesta("TOPUP TRANSACTION FAILED");
        }
   
        return transaction;
    }
    
    
    
    public TransactionListResponse getTransactionsByUserIdApp(Long userId, Integer maxResult) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        try {
            entityManager.flush();
            
            transactions = (List<Transaction>) entityManager.createNamedQuery("Transaction.findByUserSourceId", Transaction.class).setParameter("userSourceId", userId).setMaxResults(maxResult).setParameter("userDestinationId", userId).getResultList();
            if (transactions.size() < 1) {
                throw new NoResultException(ResponseCode.TRANSACTION_LIST_NOT_FOUND_EXCEPTION.toString());
            }
        } catch (NoResultException e) {
            e.printStackTrace();
            return new TransactionListResponse(ResponseCode.TRANSACTION_LIST_NOT_FOUND_EXCEPTION, "El usuario no tiene transacciones asociadas");
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionListResponse(ResponseCode.ERROR_INTERNO, "error interno");
        }
        
        APIRegistroUnificadoProxy api = new APIRegistroUnificadoProxy();
        
        for(Transaction t :transactions) {
            t.setPaymentInfoId(null);
            t.setProductId(t.getProductId());
            t.setTransactionType(t.getTransactionTypeId().getId().toString());
            t.setId(t.getId());
            RespuestaUsuario usuarioRespuesta = new RespuestaUsuario();
            try {
                usuarioRespuesta = api.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, t.getUserDestinationId().toString());
                t.setDestinationUser(usuarioRespuesta.getDatosRespuesta().getEmail() +" / "+ usuarioRespuesta.getDatosRespuesta().getMovil() + " / "+ usuarioRespuesta.getDatosRespuesta().getNombre());
            } catch (RemoteException ex) {
                return new TransactionListResponse(ResponseCode.ERROR_INTERNO, "No se logro comunicacion entre alodiga wallet y RU");
            }
        }
        return new TransactionListResponse(ResponseCode.EXITO, "", transactions);
    }
	
   public TransactionResponse manualWithdrawals(Long bankId, String emailUser, String accountBank, 
                                                Float amountWithdrawal, Long productId, String conceptTransaction) {
        
        Long idTransaction                      = 0L;
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
        Commission commissionWithdrawal         = new Commission();
        
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
            
            //Registrar la transacción
            Transaction withdrawal = new Transaction();
            withdrawal.setId(null);
            withdrawal.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            withdrawal.setUserDestinationId(withdrawal.getUserSourceId());
            Product product = entityManager.find(Product.class, productId);
            withdrawal.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeManualWithdrawal);
            withdrawal.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            withdrawal.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            withdrawal.setCreationDate(creationDate);
            withdrawal.setConcept(conceptTransaction);
            withdrawal.setAmount(amountWithdrawal);
            withdrawal.setTransactionStatus(TransactionStatus.CREATED.name());
            withdrawal.setTotalAmount(amountWithdrawal);
            entityManager.persist(withdrawal);
            
            //Revisar si la transaccion esta sujeta a comisiones
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId",Constante.sTransationTypeManualWithdrawal).getResultList();
                if(commissions.size() < 1){
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: "+ userId);
                }                
                for (Commission c: commissions) {
                    commissionWithdrawal = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountWithdrawal * amountCommission)/100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }    
                //Se crea el objeto commissionItem y se persiste en BD
                CommissionItem commissionItem = new CommissionItem();
                commissionItem.setCommissionId(commissionWithdrawal);           
                commissionItem.setAmount(amountCommission);
                Date commissionDate = new Date();
                Timestamp processedDate = new Timestamp(commissionDate.getTime());
                commissionItem.setProcessedDate(processedDate);
                commissionItem.setTransactionId(withdrawal);
                entityManager.persist(commissionItem);
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
            }
            
            //Guardar los datos del retiro          
            BankOperation manualWithdrawal = new BankOperation();
            manualWithdrawal.setId(null);
            manualWithdrawal.setUserSourceId(BigInteger.valueOf(userId));
            manualWithdrawal.setProductId(product);
            manualWithdrawal.setTransactionId(withdrawal);
            manualWithdrawal.setCommisionId(commissionWithdrawal);
            BankOperationType operationType = entityManager.find(BankOperationType.class,Constante.sBankOperationTypeWithdrawal);
            manualWithdrawal.setBankOperationTypeId(operationType);
            BankOperationMode operationMode = entityManager.find(BankOperationMode.class, Constante.sBankOperationModeManual);
            manualWithdrawal.setBankOperationModeId(operationMode);
            Bank bank = entityManager.find(Bank.class, bankId);
            manualWithdrawal.setBankId(bank);
            manualWithdrawal.setBankOperationNumber(accountBank);
            entityManager.persist(manualWithdrawal);
            
            
            
            
            //Se actualiza el estatus de la transacción a IN_PROCESS
            withdrawal.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(withdrawal);
//            Usuario usuario = new Usuario();
        Usuario usuario = new Usuario();
        usuario.setEmail(emailUser);
        SendMailTherad sendMailTherad = new SendMailTherad("ES", accountBank, amountWithdrawal, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido() ,emailUser , Integer.valueOf("4"));
        sendMailTherad.run();
                        
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        }
        
        return new TransactionResponse(ResponseCode.EXITO);
    }
	 
    /*
     *
     */
    public TransactionResponse manualRecharge(Long bankId, String emailUser, String referenceNumberOperation, 
                                              Float amountRecharge, Long productId, String conceptTransaction) {
    
    Long idTransaction                      = 0L;
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
    Commission commissionRecharge           = new Commission();
        
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
            
            //Registrar la transacción en la BD
            Transaction recharge = new Transaction();
            recharge.setId(null);
            recharge.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            recharge.setUserDestinationId(recharge.getUserSourceId());
            Product product = entityManager.find(Product.class, productId);
            recharge.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeManualRecharge);
            recharge.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            recharge.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            recharge.setCreationDate(creationDate);
            recharge.setConcept(conceptTransaction);
            recharge.setAmount(amountRecharge);
            recharge.setTransactionStatus(TransactionStatus.CREATED.name());
            recharge.setTotalAmount(amountRecharge);
            entityManager.persist(recharge);
            
            //Revisar si la transaccion esta sujeta a comisiones
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId",Constante.sTransationTypeManualRecharge).getResultList();
                if(commissions.size() < 1){
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: "+ userId);
                }  
                for (Commission c: commissions) {
                    commissionRecharge = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountRecharge * amountCommission)/100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                } 
                //Se crea el objeto commissionItem y se persiste en BD
                CommissionItem commissionItem = new CommissionItem();
                commissionItem.setCommissionId(commissionRecharge);           
                commissionItem.setAmount(amountCommission);
                Date commissionDate = new Date();
                Timestamp processedDate = new Timestamp(commissionDate.getTime());
                commissionItem.setProcessedDate(processedDate);
                commissionItem.setTransactionId(recharge);
                entityManager.persist(commissionItem);
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
            }
            
            //Guardar los datos de la recarga          
            BankOperation manualRecharge = new BankOperation();
            manualRecharge.setId(null);
            manualRecharge.setUserSourceId(BigInteger.valueOf(userId));
            manualRecharge.setProductId(product);
            manualRecharge.setTransactionId(recharge);
            manualRecharge.setCommisionId(commissionRecharge);
            BankOperationType operationType = entityManager.find(BankOperationType.class,Constante.sBankOperationTypeRecharge);
            manualRecharge.setBankOperationTypeId(operationType);
            BankOperationMode operationMode = entityManager.find(BankOperationMode.class, Constante.sBankOperationModeManual);
            manualRecharge.setBankOperationModeId(operationMode);
            Bank bank = entityManager.find(Bank.class, bankId);
            manualRecharge.setBankId(bank);
            manualRecharge.setBankOperationNumber(referenceNumberOperation);
      
            entityManager.persist(manualRecharge);
        
            //Se actualiza el estatus de la transacción a IN_PROCESS
            recharge.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            
            entityManager.merge(recharge);
            
            
          Usuario usuario = new Usuario();
        usuario.setEmail(emailUser);
        SendMailTherad sendMailTherad = new SendMailTherad("ES", referenceNumberOperation, conceptTransaction, amountRecharge, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido() ,emailUser, Integer.valueOf("2"));
        sendMailTherad.run();
    } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
        } 
    
        
        return new TransactionResponse(ResponseCode.EXITO);
        
    }
    
     public ProductListResponse getProductsByBankId(Long bankId,Long userId) {
        List<BankHasProduct> bankHasProducts = new ArrayList<BankHasProduct>();
        List<Product> products = new ArrayList<Product>();
        try {
            bankHasProducts = (List<BankHasProduct>) entityManager.createNamedQuery("BankHasProduct.findByBankId", BankHasProduct.class).setParameter("bankId", bankId).getResultList();
            if (bankHasProducts.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }
            for (BankHasProduct bhp : bankHasProducts) {
                Product product = new Product();
                product = entityManager.find(Product.class, bhp.getProductId());
                BalanceHistory balanceHistory = new BalanceHistory();
                try {
                    balanceHistory = loadLastBalanceHistoryByAccount_(userId, product.getId());
                    product.setCurrentBalance(balanceHistory.getCurrentAmount());
                } catch (NoResultException e) {
                    product.setCurrentBalance(0F);
                }
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.ERROR_INTERNO, "Error loading products");
        }
       

        return new ProductListResponse(ResponseCode.EXITO, "", products);
    }
	
	
   public CountryListResponse getCountriesHasBank(Long userId) {
        List countries = null;
        ArrayList<Country> countrys = new ArrayList<Country>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT b.countryId FROM alodigaWallet.bank b WHERE b.id IN (SELECT bhp.bankId FROM alodigaWallet.bank_has_product bhp WHERE bhp.productId IN (SELECT uhp.productId FROM alodigaWallet.user_has_product uhp  WHERE uhp.userSourceId='"+userId+"'))GROUP BY b.countryId;");
            Query query = entityManager.createNativeQuery(sqlBuilder.toString());
            countries = query.setHint("toplink.refresh", "true").getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CountryListResponse(ResponseCode.ERROR_INTERNO, "Error loading Countries");
            
        }
        if (countries != null && countries.size() > 0) {
            for (int i = 0; i < countries.size(); i++) {
               Long countryId = (Long) countries.get(i);
               
               
               Country country = entityManager.find(Country.class, countryId);
               countrys.add(country);
            }
        }else{
              
            return new CountryListResponse(ResponseCode.EMPTY_LIST_COUNTRY, "Empty Countries List");
        }
        return new CountryListResponse(ResponseCode.EXITO, "", countrys);
    }
   
   //Desarrollador por kerwin 2-10-2019 modificaciòn solicitada por adira
    public BalanceHistoryResponse getBalanceHistoryByUserAndProduct(Long userId, Long productId) {
        BalanceHistory balanceHistory = new BalanceHistory();
        try {
            balanceHistory = loadLastBalanceHistoryByAccount_(userId, productId);
        } catch (NoResultException e) {
            e.printStackTrace();
            return new BalanceHistoryResponse(ResponseCode.BALANCE_HISTORY_NOT_FOUND_EXCEPTION, "Error loading BalanceHistory");
        }
        return new BalanceHistoryResponse(ResponseCode.EXITO, "", balanceHistory);
    }
    
    public BalanceHistory loadLastBalanceHistoryByAccount_(Long userId, Long productId) throws NoResultException {

        try {
            Query query = entityManager.createQuery("SELECT b FROM BalanceHistory b WHERE b.userId = " +userId+ " AND b.productId.id = " + productId + " ORDER BY b.id desc");
            query.setMaxResults(1);
            BalanceHistory result = (BalanceHistory) query.setHint("toplink.refresh", "true").getSingleResult();
            return result;
        } catch (NoResultException e) {
            e.printStackTrace();
            throw  new NoResultException();
        } 

    }
  
    
    public void sendmailTest() {
        
        Usuario usuario = new Usuario();
        usuario.setNombre("Kerwin");
        usuario.setApellido("Gomez");
        usuario.setCredencial("DAnye");
        usuario.setEmail("moisegrat12@gmail.com");
        usuario.setMovil("584241934005");
        Cuenta cunCuenta = new Cuenta();
        cunCuenta.setNumeroCuenta("01050614154515461528");
        usuario.setCuenta(cunCuenta);
        Transaction transaction = new Transaction();
        transaction.setId(1412L);
        transaction.getId();
        transaction.getTotalAmount();
        transaction.setTotalAmount(Float.valueOf("2"));
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setCurrentAmount(20);
        balanceHistory.setOldAmount(25);
        
        
        
                Mail mail = Utils.SendMailUserChangePassword("ES", usuario);
        System.out.println("body: " + mail.getBody());
                try {
        EnvioCorreo.enviarCorreoHtml(new String[]{mail.getTo().get(0)},
                mail.getSubject(),  mail.getBody(), Utils.obtienePropiedad("mail.user"), null);     
            //AmazonSESSendMail.SendMail(mail.getSubject(), mail.getBody(), mail.getTo().get(0));
            //Envio de Correo Electronico
        } catch (Exception ex) {
            ex.printStackTrace();
        }
                }
    
    
    
    public ArrayList<Product> getProductsListByUserId(Long userId) throws NoResultException,Exception{
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            userHasProducts = (List<UserHasProduct>) entityManager.createNamedQuery("UserHasProduct.findByUserSourceId", UserHasProduct.class).setParameter("userSourceId", userId).getResultList();
            
            if (userHasProducts.size() <= 0) {
                throw new NoResultException();
            }
            
            for (UserHasProduct uhp : userHasProducts) {
                Product product = new Product();
                product = entityManager.find(Product.class, uhp.getProductId());
                products.add(product);
            }
        } catch (Exception e) {   
            throw new Exception();   
        }
        return products;
    }
    
    public TransactionResponse saveRechargeTopUp(String emailUser, Long productId, String cryptogramUser,
            String skudId, String destinationNumber, String senderNumber, Float amountRecharge, Float amountPayment, String language ) {
        
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
        Commission commissionTopUp              = new Commission();
        
        TransactionResponse response = null;
        try {    
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS","passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
//            userId = Long.valueOf("402");//para test
            
            // Validar que el balance history del cliente disponga de saldo para hacer la operacion
            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId,productId);
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountRecharge) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE,"The user has no balance available to complete the transaction");
            }
            
            //Validar preferencias
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];
            
            //Obtiene las transacciones del dÃ­a para el usuario
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene la sumatoria de los montos de las transacciones del usuario
            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);
            
            //Obtiene las transacciones del dÃ­a para el producto que se estÃ¡ comprando
            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);
            
            //Cotejar las preferencias vs las transacciones del usuario
            List<Preference> preferences = getPreferences();
            for(Preference p: preferences){
                if (p.getName().equals(Constants.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for(PreferenceField pf: preferencesField){
                switch(pf.getName()) {
                    case Constants.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf); 
                            for(PreferenceValue pv: preferencesValue){
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT,"The user exceeded the maximum amount per day");
                                }
                            }
                        }
                    break;
                    case Constants.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);                           
                            for(PreferenceValue pv: preferencesValue){
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT,"The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                    break;
                    case Constants.sValidatePreferenceTransaction3:
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
            
            //Crear el objeto Transaction para registrar el pago del topUp
            Transaction recharge = new Transaction();
            recharge.setId(null);
            recharge.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
//            recharge.setUserSourceId(BigInteger.valueOf(402));//para test
            recharge.setTopUpDescription("Destination:"+destinationNumber+" SkuidID:"+skudId);
            Product product = entityManager.find(Product.class, productId);
            recharge.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeTopUP);
            recharge.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSourceTopUP);
            recharge.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            recharge.setCreationDate(creationDate);
            //cambiar por valor de parametro
            recharge.setConcept(Constante.sTransactionConceptTopUp);
            recharge.setAmount(amountRecharge);
            recharge.setTransactionStatus(TransactionStatus.CREATED.name());
            recharge.setTotalAmount(amountRecharge);
            entityManager.persist(recharge);
            
                      
            //Revisar si la transaccion esta sujeta a comisiones
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId",Constante.sTransationTypeTopUP).getResultList();
                if(commissions.size() < 1){
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: "+ userId);
                }                
                for (Commission c: commissions) {
                    commissionTopUp = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountRecharge * amountCommission)/100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }    
                //Se crea el objeto commissionItem y se persiste en BD
                CommissionItem commissionItem = new CommissionItem();
                commissionItem.setCommissionId(commissionTopUp);           
                commissionItem.setAmount(amountCommission);
                Date commissionDate = new Date();
                Timestamp processedDate = new Timestamp(commissionDate.getTime());
                commissionItem.setProcessedDate(processedDate);
                commissionItem.setTransactionId(recharge);
                entityManager.persist(commissionItem);
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");  
            }
            
            //Se actualiza el monto total monto del topUp + comision
            amountPayment = amountRecharge + amountCommission;
            recharge.setTotalAmount(amountPayment);
            entityManager.merge(recharge);
               
            //Se actualizan los saldos del cliente en la BD
            //Balance History del cliente
            balanceUserSource = loadLastBalanceHistoryByAccount(userId,productId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
            Float currentAmountUserSource = balanceUserSource.getCurrentAmount() - amountPayment;
            balanceHistory.setCurrentAmount(currentAmountUserSource);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(recharge);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserSource.getId());
            entityManager.persist(balanceHistory);
            
            //Se actualiza el estatus de la transaccion a IN_PROCESS
            recharge.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(recharge);
            
           //ejecuta la recarga de TopUp
            response = this.executeTopUp(skudId,amountRecharge, destinationNumber,  senderNumber);
            if (response.getCodigoRespuesta().equals(ResponseCode.EXITO.getCodigo())){
            
                //Se actualiza el estado de la transaccion a COMPLETED
                recharge.setTransactionStatus(TransactionStatus.COMPLETED.name());
                entityManager.merge(recharge);
                //Envias notificaciones
                //envias sms
            }else {
                //Fallo el topUp devolver el saldo
                //Se actualizan los saldos del cliente en la BD
                //Balance History del cliente FAILED
                balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
                balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
                currentAmountUserSource = balanceUserSource.getCurrentAmount() + amountPayment;
                balanceHistory.setCurrentAmount(currentAmountUserSource);
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(recharge);
                balanceDate = new Date();
                balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                balanceHistory.setVersion(balanceUserSource.getId());
                balanceHistory.setAdjusmentInfo("TopUp Failed. Balance Refund");
                entityManager.persist(balanceHistory);
                
                 //Se actualiza el estatus de la transaccion a FAILED
                recharge.setTransactionStatus(TransactionStatus.FAILED.name());
                entityManager.merge(recharge);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction saveRechargeTopUp");  
        } 
        return response;
    }
    
    
    public TopUpCountryListResponse getTopUpCountries() {
        List<TopUpCountry> topUpCountrys = null;
        try {
            topUpCountrys = entityManager.createNamedQuery("TopUpCountry.findAll", TopUpCountry.class).getResultList();

        } catch (Exception e) {
            return new TopUpCountryListResponse(ResponseCode.ERROR_INTERNO, "Error loading countries");
        }
        return new TopUpCountryListResponse(ResponseCode.EXITO, "", topUpCountrys);
    }
    
    
    
    public LanguageListResponse getLanguage() {
        List<Language> languages = null;
        try {
            languages = entityManager.createNamedQuery("Language.findAll", Language.class).getResultList();

        } catch (Exception e) {
            return new LanguageListResponse(ResponseCode.ERROR_INTERNO, "Error loading countries");
        }
        return new LanguageListResponse(ResponseCode.EXITO, "", languages);
    }
    
    
    public ProductListResponse getProductsPayTopUpByUserId(Long userId) {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        List<Product> products = new ArrayList<Product>();
        List<Product> productFinals = new ArrayList<Product>();
        try {        
            products = getProductsListByUserId(userId);
            for(Product p: products){
                if(p.isIsPayTopUp()){
                    Float amount = 0F;
                try {
                    amount = loadLastBalanceHistoryByAccount_(userId,  p.getId()).getCurrentAmount();
                } catch (NoResultException e) {
                    amount = 0F;        
                }
                 p.setCurrentBalance(amount);  
                 productFinals.add(p);       
                }
            }
             if (productFinals.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
             }
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.ERROR_INTERNO, "Error loading products");
        }
       

        return new ProductListResponse(ResponseCode.EXITO, "", productFinals);
    }
    
    
    
    
}




