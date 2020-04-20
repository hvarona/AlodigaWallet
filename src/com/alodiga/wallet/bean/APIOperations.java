package com.alodiga.wallet.bean;

import afinitaspaymentintegration.AfinitasPaymentIntegration;
import cardcredentialserviceclient.CardCredentialServiceClient;
import com.alodiga.account.client.AccountCredentialServiceClient;
import com.alodiga.account.credential.response.StatusAccountResponse;
import com.alodiga.afinitas.json.charge.object.ChargeResponse;
import com.alodiga.autorization.credential.client.AutorizationCredentialServiceClient;
import com.alodiga.autorization.credential.response.CardToCardTransferResponse;

import com.alodiga.card.credential.response.ChangeStatusCardResponse;
import com.alodiga.card.credential.response.StatusCardResponse;
import com.alodiga.transferto.integration.connection.RequestManager;
import com.alodiga.transferto.integration.model.MSIDN_INFOResponse;
import com.alodiga.transferto.integration.model.ReserveResponse;
import com.alodiga.transferto.integration.model.TopUpResponse;
import com.alodiga.wallet.model.Address;
import com.alodiga.wallet.model.Bank;
import com.alodiga.wallet.model.BankOperation;
import com.alodiga.wallet.model.BankOperationMode;
import com.alodiga.wallet.model.BankOperationType;
import com.alodiga.wallet.model.Category;
import com.alodiga.wallet.model.Country;
import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.Language;
import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.ProductIntegrationType;
import com.alodiga.wallet.model.UserHasProduct;
import com.alodiga.wallet.model.Transaction;
import com.alodiga.wallet.model.Preference;
import com.alodiga.wallet.model.PreferenceField;
import com.alodiga.wallet.model.PreferenceValue;
import com.alodiga.wallet.model.Provider;
import com.alodiga.wallet.model.TopUpResponseConstants;
import com.alodiga.wallet.model.TransactionType;
import com.alodiga.wallet.model.TransactionSource;
import com.alodiga.wallet.model.TransactionStatus;
import com.alodiga.wallet.model.Commission;
import com.alodiga.wallet.model.CommissionItem;
import com.alodiga.wallet.model.BalanceHistory;
import com.alodiga.wallet.model.BankHasProduct;
import com.alodiga.wallet.model.Card;
import com.alodiga.wallet.model.Code;
import com.alodiga.wallet.model.CreditcardType;
import com.alodiga.wallet.model.Cumplimient;
import com.alodiga.wallet.model.CumplimientStatus;
import com.alodiga.wallet.model.ExchangeRate;
import com.alodiga.wallet.model.ExchangeDetail;
import com.alodiga.wallet.model.PaymentInfo;
import com.alodiga.wallet.model.PaymentPatner;
import com.alodiga.wallet.model.PaymentType;
import com.alodiga.wallet.model.Sms;
import com.alodiga.wallet.model.State;
import com.alodiga.wallet.model.TopUpCountry;
import com.alodiga.wallet.model.UserHasCard;
import com.alodiga.wallet.model.UserWS;
import com.alodiga.wallet.model.ValidationCollection;
import com.alodiga.wallet.response.generic.BankGeneric;
import com.alodiga.wallet.respuestas.ActivateCardResponses;
import com.alodiga.wallet.respuestas.BalanceHistoryResponse;
import com.alodiga.wallet.respuestas.BankListResponse;
import com.alodiga.wallet.respuestas.CardListResponse;
import com.alodiga.wallet.respuestas.CardResponse;
import com.alodiga.wallet.respuestas.ChangeStatusCredentialCard;
import com.alodiga.wallet.respuestas.CheckStatusAccountResponses;
import com.alodiga.wallet.respuestas.CheckStatusCardResponses;
import com.alodiga.wallet.respuestas.CheckStatusCredentialAccount;
import com.alodiga.wallet.respuestas.CheckStatusCredentialCard;
import com.alodiga.wallet.respuestas.CollectionListResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigInteger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import com.alodiga.wallet.respuestas.ResponseCode;
import com.alodiga.wallet.respuestas.ProductResponse;
import com.alodiga.wallet.respuestas.UserHasProductResponse;
import com.alodiga.wallet.respuestas.CountryListResponse;
import com.alodiga.wallet.respuestas.CountryResponse;
import com.alodiga.wallet.respuestas.CreditCardListResponse;
import com.alodiga.wallet.respuestas.CumplimientResponse;
import com.alodiga.wallet.respuestas.DesactivateCardResponses;
import com.alodiga.wallet.respuestas.LanguageListResponse;
import com.alodiga.wallet.respuestas.PaymentInfoListResponse;
import com.alodiga.wallet.respuestas.PaymentInfoResponse;
import com.alodiga.wallet.respuestas.ProductListResponse;
import com.alodiga.wallet.respuestas.RechargeAfinitasResponses;
import com.alodiga.wallet.respuestas.RemittanceResponse;
import com.alodiga.wallet.respuestas.TopUpCountryListResponse;
import com.alodiga.wallet.respuestas.TopUpInfoListResponse;
import com.alodiga.wallet.respuestas.TransactionListResponse;
import com.alodiga.wallet.respuestas.TransactionResponse;
import com.alodiga.wallet.respuestas.TransferCardToCardCredential;
import com.alodiga.wallet.respuestas.TransferCardToCardResponses;
import com.alodiga.wallet.topup.TopUpInfo;
import com.alodiga.wallet.utils.AmazonSESSendMail;
import com.alodiga.wallet.utils.Constante;
import com.alodiga.wallet.utils.Constants;
import static com.alodiga.wallet.utils.EncriptedRsa.encrypt;
import com.alodiga.wallet.utils.Mail;
import com.alodiga.wallet.utils.S3cur1ty3Cryt3r;
import com.alodiga.wallet.utils.SendMailTherad;
import com.alodiga.wallet.utils.SendSmsThread;
import java.rmi.RemoteException;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.Usuario;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import java.sql.Timestamp;
import com.alodiga.wallet.utils.Utils;
import com.alodiga.wallet.utils.XTrustProvider;
import com.alodiga.ws.cumpliments.services.OFACMethodWSProxy;
import com.alodiga.ws.cumpliments.services.WsExcludeListResponse;
import com.alodiga.ws.cumpliments.services.WsLoginResponse;
import com.alodiga.ws.remittance.services.WSRemittenceMobileProxy;
import com.alodiga.ws.remittance.services.WsAddressListResponse;
import com.alodiga.ws.remittance.services.WsRemittenceResponse;

import com.ericsson.alodiga.ws.Cuenta;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.apache.commons.codec.binary.Base64;

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
            userHasProducts = (List<UserHasProduct>) entityManager.createNamedQuery("UserHasProduct.findByUserSourceIdAllProduct", UserHasProduct.class).setParameter("userSourceId", userId).getResultList();

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
        for (Bank b : banks) {
            BankGeneric bankGeneric = new BankGeneric(b.getId().toString(), b.getName(), b.getAba());
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

            for (Bank b : banks) {
                BankGeneric bankGeneric = new BankGeneric(b.getId().toString(), b.getName(), b.getAba());
                bankGenerics.add(bankGeneric);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new BankListResponse(ResponseCode.ERROR_INTERNO, "Error loading bank");
        }

        return new BankListResponse(ResponseCode.EXITO, "", bankGenerics);
    }

    public TransactionResponse savePaymentShop(String cryptogramShop, String emailUser, Long productId, Float amountPayment,
            String conceptTransaction, String cryptogramUser, Long idUserDestination) {

        Long idTransaction = 0L;
        Long idPreferenceField = 0L;
        Long userId = 0L;
        int totalTransactionsByUser = 0;
        Long totalTransactionsByProduct = 0L;
        Double totalAmountByUser = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction paymentShop = new Transaction();
        try {
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            RespuestaUsuario userDestination = proxy.getUsuarioporId("usuarioWS", "passwordWS", idUserDestination.toString());
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountPayment) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];

            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constants.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constants.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constants.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT, "The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                        break;
                    case Constants.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                }
            }

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

            paymentShop.setConcept(Constante.sTransactionConceptPaymentShop);
            paymentShop.setAmount(amountPayment);
            paymentShop.setTransactionStatus(TransactionStatus.CREATED.name());
            paymentShop.setTotalAmount(amountPayment);
            entityManager.persist(paymentShop);

            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypePS).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountPayment * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;

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

            paymentShop.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(paymentShop);

            balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
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

            BalanceHistory balanceUserDestination = loadLastBalanceHistoryByAccount(idUserDestination, productId);
            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(idUserDestination);
            if (balanceUserDestination == null) {
                balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                balanceHistory.setCurrentAmount(amountPayment - amountCommission);
            } else {
                balanceHistory.setOldAmount(balanceUserDestination.getCurrentAmount());
                Float currentAmountUserDestination = (balanceUserDestination.getCurrentAmount() + amountPayment) - amountCommission;
                balanceHistory.setCurrentAmount(currentAmountUserDestination);
                balanceHistory.setVersion(balanceUserDestination.getId());
            }
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(paymentShop);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);

            entityManager.persist(balanceHistory);

            paymentShop.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(paymentShop);

            products = getProductsListByUserId(userId);
            for (Product p : products) {
                Float amount = 0F;
                try {
                    if (p.getId().equals(Product.PREPAID_CARD)) {
                        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                        CardResponse cardResponse = getCardByUserId(userId);
                        String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
                        StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                        if (statusCardResponse.getCodigo().equals("00")) {
                            StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                            amount = Float.valueOf(accountResponse.getComprasDisponibles());
                        } else {
                            amount = Float.valueOf(0);
                        }

                    } else {

                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    }
                } catch (NoResultException e) {
                    amount = 0F;
                }
                p.setCurrentBalance(amount);
            }

            Usuario usuario = new Usuario();
            usuario.setEmail(emailUser);

            SendMailTherad sendMailTherad = new SendMailTherad("ES", amountPayment, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("3"));
            sendMailTherad.run();

            SendMailTherad sendMailTherad1 = new SendMailTherad("ES", amountPayment, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), userDestination.getDatosRespuesta().getEmail(), Integer.valueOf("7"));
            sendMailTherad1.run();

            System.out.println(responseUser.getDatosRespuesta().getMovil());
            System.out.println(userDestination.getDatosRespuesta().getMovil());

            SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), amountPayment, Integer.valueOf("22"), userId, entityManager);
            sendSmsThread.run();

            SendSmsThread sendSmsThread1 = new SendSmsThread(userDestination.getDatosRespuesta().getMovil(), amountPayment, Integer.valueOf("26"), Long.valueOf(userDestination.getDatosRespuesta().getUsuarioID()), entityManager);
            sendSmsThread1.run();

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
        }
        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.EXITO, "EXITO", products);
        transactionResponse.setIdTransaction(paymentShop.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;
    }

    public TransactionResponse saveTransferBetweenAccount(String cryptograUserSource, String emailUser, Long productId, Float amountTransfer,
            String conceptTransaction, String cryptograUserDestination, Long idUserDestination) {

        Long idTransaction = 0L;
        Long idPreferenceField = 0L;
        Long userId = 0L;
        int totalTransactionsByUser = 0;
        Long totalTransactionsByProduct = 0L;
        Double totalAmountByUser = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionTransfer = new Commission();
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction transfer = new Transaction();
        try {

            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            RespuestaUsuario userDestination = proxy.getUsuarioporId("usuarioWS", "passwordWS", idUserDestination.toString());
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeTA).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionTransfer = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountTransfer * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
            }
            Float amountTransferTotal = amountTransfer + amountCommission;
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountTransferTotal) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];

            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT, "The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                }
            }

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

            transfer.setConcept(Constante.sTransactionConceptTranferAccounts);
            transfer.setAmount(amountTransfer);
            transfer.setTransactionStatus(TransactionStatus.CREATED.name());
            transfer.setTotalAmount(amountTransfer);
            entityManager.persist(transfer);

            CommissionItem commissionItem = new CommissionItem();
            commissionItem.setCommissionId(commissionTransfer);
            commissionItem.setAmount(amountCommission);
            Date commissionDate = new Date();
            Timestamp processedDate = new Timestamp(commissionDate.getTime());
            commissionItem.setProcessedDate(processedDate);
            commissionItem.setTransactionId(transfer);
            entityManager.persist(commissionItem);

            transfer.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(transfer);

            balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
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

            BalanceHistory balanceUserDestination = loadLastBalanceHistoryByAccount(idUserDestination, productId);
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

            transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(transfer);

            products = getProductsListByUserId(userId);
            for (Product p : products) {
                Float amount = 0F;
                try {
                    if (p.getId().equals(Product.PREPAID_CARD)) {
                        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                        CardResponse cardResponse = getCardByUserId(userId);
                        String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
                        StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                        if (statusCardResponse.getCodigo().equals("00")) {
                            StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                            amount = Float.valueOf(accountResponse.getComprasDisponibles());
                        } else {
                            amount = Float.valueOf(0);
                        }

                    } else {

                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    }
                } catch (NoResultException e) {
                    amount = 0F;
                }
                p.setCurrentBalance(amount);
            }

            SendMailTherad sendMailTherad = new SendMailTherad("ES", amountTransfer, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("8"));
            sendMailTherad.run();

            SendMailTherad sendMailTherad1 = new SendMailTherad("ES", amountTransfer, conceptTransaction, userDestination.getDatosRespuesta().getNombre() + " " + userDestination.getDatosRespuesta().getApellido(), userDestination.getDatosRespuesta().getEmail(), Integer.valueOf("9"));
            sendMailTherad1.run();

            SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), amountTransfer, Integer.valueOf("27"), userId, entityManager);
            sendSmsThread.run();

            SendSmsThread sendSmsThread1 = new SendSmsThread(userDestination.getDatosRespuesta().getMovil(), amountTransfer, Integer.valueOf("28"), Long.valueOf(userDestination.getDatosRespuesta().getUsuarioID()), entityManager);
            sendSmsThread1.run();

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
        }
        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.EXITO, "EXITO", products);
        transactionResponse.setIdTransaction(transfer.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;
    }

    public TransactionResponse previewExchangeProduct(String emailUser, Long productSourceId, Long productDestinationId,
            Float amountExchange, int includedAmount) {

        Long userId = 0L;
        Float amountCommission = 0.00F;
        List<Commission> commissions = new ArrayList<Commission>();
        short isPercentCommission = 0;
        Float valueCommission = 0.00F;
        Float totalDebit = 0.00F;
        Float amountConversion = 0.00F;
        Float valueRateByProductSource = 0.00F;
        Float valueRateByProductDestination = 0.00F;

        try {
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, productSourceId);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productSourceId).setParameter("transactionTypeId", Constante.sTransationTypeEP).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productSourceId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    valueCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && valueCommission > 0) {
                        amountCommission = (amountExchange * valueCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
            }
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < totalDebit) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            ExchangeRate RateByProductSource = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productSourceId).getSingleResult();
            valueRateByProductSource = RateByProductSource.getValue();
            ExchangeRate RateByProductDestination = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productDestinationId).getSingleResult();
            valueRateByProductDestination = RateByProductDestination.getValue();
            if (includedAmount == 0) {
                totalDebit = amountExchange + amountCommission;
                amountConversion = (amountExchange * RateByProductSource.getValue()) / RateByProductDestination.getValue();
            } else {
                totalDebit = amountExchange;
                amountExchange = amountExchange - amountCommission;
                amountConversion = (amountExchange * RateByProductSource.getValue()) / RateByProductDestination.getValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
        }
        return new TransactionResponse(ResponseCode.EXITO, "", amountCommission, valueCommission, totalDebit,
                amountConversion, valueRateByProductSource, valueRateByProductDestination, isPercentCommission);
    }

    public TransactionResponse exchangeProduct(String emailUser, Long productSourceId, Long productDestinationId,
            Float amountExchange, String conceptTransaction, int includedAmount) {

        Long idTransaction = 0L;
        Long idPreferenceField = 0L;
        Long userId = 0L;
        Float totalDebit = 0.00F;
        int totalTransactionsByUser = 0;
        Long totalTransactionsByProduct = 0L;
        Double totalAmountByUser = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction exchange = new Transaction();

        try {
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];

            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productSourceId, userId, begginingDateTime, endingDateTime);

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT, "The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                }
            }

            exchange.setId(null);
            exchange.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            exchange.setUserDestinationId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
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

            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productSourceId).setParameter("transactionTypeId", Constante.sTransationTypeEP).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productSourceId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountExchange * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
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

            ExchangeRate RateByProductSource = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productSourceId).getSingleResult();
            ExchangeRate RateByProductDestination = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productDestinationId).getSingleResult();
            if (includedAmount == 0) {
                totalDebit = amountExchange + amountCommission;
            } else {
                totalDebit = amountExchange;
                amountExchange = amountExchange - amountCommission;
            }
            Float amountConversion = (amountExchange * RateByProductSource.getValue()) / RateByProductDestination.getValue();

            exchange.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(exchange);

            ExchangeDetail detailProductDestination = new ExchangeDetail();
            detailProductDestination.setId(null);
            detailProductDestination.setExchangeRateId(RateByProductDestination);
            Product productDestination = entityManager.find(Product.class, productDestinationId);
            detailProductDestination.setProductId(productDestination);
            detailProductDestination.setTransactionId(exchange);
            entityManager.persist(detailProductDestination);

            BalanceHistory balanceProductSource = loadLastBalanceHistoryByAccount(userId, productSourceId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceProductSource.getCurrentAmount());
            Float currentAmountProductSource = balanceProductSource.getCurrentAmount() - totalDebit;
            balanceHistory.setCurrentAmount(currentAmountProductSource);
            balanceHistory.setProductId(productSource);
            balanceHistory.setTransactionId(exchange);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceProductSource.getId());
            entityManager.persist(balanceHistory);

            BalanceHistory balanceProductDestination = loadLastBalanceHistoryByAccount(userId, productDestinationId);
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

            exchange.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(exchange);

            try {
                products = getProductsListByUserId(userId);
                for (Product p : products) {
                    Float amount_1 = 0F;
                    try {
                        if (p.getId().equals(Product.PREPAID_CARD)) {
                            AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                            CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                            CardResponse cardResponse = getCardByUserId(userId);
                            String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                            if (statusCardResponse.getCodigo().equals("00")) {
                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                            } else {
                                amount_1 = Float.valueOf(0);
                            }

                        } else {
                            amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                        }

                    } catch (NoResultException e) {
                        amount_1 = 0F;
                    }
                    p.setCurrentBalance(amount_1);
                }
            } catch (Exception ex) {

                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error loading products");
            }
            SendMailTherad sendMailTherad = new SendMailTherad("ES", productSource.getName(), productDestination.getName(), amountExchange, emailUser, conceptTransaction, emailUser, Integer.valueOf("10"));
            sendMailTherad.run();

            SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), productSource.getName(), productDestination.getName(), amountExchange, Integer.valueOf("29"), userId, entityManager);
            sendSmsThread.run();

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
        }
        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.EXITO, "EXITO", products);
        transactionResponse.setIdTransaction(exchange.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;
    }

    private List<Preference> getPreferences() {
        return entityManager.createNamedQuery("Preference.findAll", Preference.class).getResultList();
    }

    private List<Transaction> getTransactions() {
        return entityManager.createNamedQuery("Transaction.findAll", Transaction.class).getResultList();
    }

    private List<Transaction> getTransactionsByUser(Long userId) {
        return entityManager.createNamedQuery("Transaction.findByUserSourceId", Transaction.class)
                .setParameter("userSourceId", userId).getResultList();
    }

    private List<PreferenceValue> getPreferenceValuePayment(PreferenceField pf) {
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        Long idPreferenceField = pf.getId();
        return preferencesValue = entityManager.createNamedQuery("PreferenceValue.findByPreferenceFieldId", PreferenceValue.class).setParameter("preferenceFieldId", idPreferenceField).getResultList();
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

    public int TransactionsByUserByTransactionByProductCurrentDate(Long userId, Timestamp begginingDateTime, Timestamp endingDateTime, Long productId, Long transactionTypeId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3 AND t.productId = ?4 AND t.transactionTypeId = ?5");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        query.setParameter("4", productId);
        query.setParameter("5", transactionTypeId);
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

    public Double AmountMaxByUserByUserByTransactionByProductCurrentDate(Long userId, Timestamp begginingDateTime, Timestamp endingDateTime, Long productId, Long transactionTypeId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT SUM(t.totalAmount) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3 AND t.productId = ?4 AND t.transactionTypeId = ?5");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        query.setParameter("4", productId);
        query.setParameter("5", transactionTypeId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (double) result.get(0) : 0f;
    }

    public Long TransactionsByProductByUserCurrentDate(Long productId, Long userId, Timestamp begginingDateTime, Timestamp endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(t.productId) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3 AND t.productId = ?4");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        query.setParameter("4", productId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (Long) result.get(0) : 0l;
    }

    public Long TransactionsByProductByUserByTransactionCurrentDate(Long productId, Long userId, Timestamp begginingDateTime, Timestamp endingDateTime, Long transactionTypeId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(t.productId) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3 AND t.productId = ?4 AND t.transactionTypeId = ?5");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        query.setParameter("4", productId);
        query.setParameter("5", transactionTypeId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (Long) result.get(0) : 0l;
    }

    private Provider getProviderById(Long providerId) {
        return entityManager.createNamedQuery("Provider.findById", Provider.class).setParameter("id", providerId)
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
                topUpInfo.setMinimumAmount(Float.parseFloat(inf.getOpen_range_minimum_amount_requested_currency()));
                topUpInfo.setMaximumAmount(Float.parseFloat(inf.getOpen_range_maximum_amount_requested_currency()));
                topUpInfo.setIncrement(Float.parseFloat(inf.getOpen_range_increment_local_currency()));
                Float amount = Float.parseFloat(inf.getOpen_range_minimum_amount_requested_currency()) + Float.parseFloat(inf.getOpen_range_increment_local_currency());

                TopUpResponse topUpResponse = RequestManager.simulationDoTopUp(phoneNumber, receiverNumber, inf.getOpen_range_minimum_amount_requested_currency(), inf.getSkuid());
                Float wholesalePrice = Float.parseFloat(topUpResponse.getWholesalePrice());
                topUpInfo.setWholesalePrice(wholesalePrice);
                Float realRetailPrice;
                Float commissionPercent;
                //solo se adiciona si es cuba
                if (inf.getCountry().equals("Cuba")) {
                    realRetailPrice = Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency()) + (Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency()) * percentAditional / 100);
                } else {
                    realRetailPrice = Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency());
                }
                commissionPercent = ((realRetailPrice - wholesalePrice) / realRetailPrice) * 100;
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
                        realRetailPrice = (retailPrice) + ((retailPrice * percentAditional) / 100);
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

    private TransactionResponse executeTopUp(String skuidIdRequest, Float amount, String destinationNumber, String senderNumber, String emailUser) {
        TransactionResponse transaction = new TransactionResponse();
        String phoneNumber = destinationNumber;
        try {
            MSIDN_INFOResponse response1 = RequestManager.getMsisdn_ingo(phoneNumber);
            String skuidId = response1.getSkuid();
            if (response1.getSkuid() == null) {
                String[] Skuids = response1.getSkuid_list().split(",");
                String[] products = response1.getProduct_list().split(",");
                for (int o = 0; o < products.length; o++) {
                    if (Float.parseFloat(products[o]) == amount) {
                        skuidId = Skuids[o];
                    }
                }
            }
            if (skuidId.equals(skuidIdRequest)) {
                ReserveResponse response2 = RequestManager.getReserve();
                //TopUpResponse topUpResponseExecute = RequestManager.simulationDoTopUp(senderNumber, phoneNumber, amount.toString(), skuidId);
                TopUpResponse topUpResponseExecute = RequestManager.newDoTopUp(senderNumber, phoneNumber, amount.toString(), skuidId, response2.getReserved_id());
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
            } else {
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

        for (Transaction t : transactions) {
            t.setPaymentInfoId(null);
            t.setProductId(t.getProductId());
            t.setTransactionType(t.getTransactionTypeId().getId().toString());
            t.setId(t.getId());
            RespuestaUsuario usuarioRespuesta = new RespuestaUsuario();
            try {
                usuarioRespuesta = api.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, t.getUserDestinationId().toString());
                t.setDestinationUser(usuarioRespuesta.getDatosRespuesta().getEmail() + " / " + usuarioRespuesta.getDatosRespuesta().getMovil() + " / " + usuarioRespuesta.getDatosRespuesta().getNombre());
            } catch (RemoteException ex) {
                return new TransactionListResponse(ResponseCode.ERROR_INTERNO, "No se logro comunicacion entre alodiga wallet y RU");
            }
        }
        return new TransactionListResponse(ResponseCode.EXITO, "", transactions);
    }

    public TransactionResponse manualWithdrawals(Long bankId, String emailUser, String accountBank,
            Float amountWithdrawal, Long productId, String conceptTransaction) {

        Long idTransaction = 0L;
        Long userId = 0L;
        int totalTransactionsByUser = 0;
        Long totalTransactionsByProduct = 0L;
        Double totalAmountByUser = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionWithdrawal = new Commission();
        Transaction withdrawal = new Transaction();
        ArrayList<Product> products = new ArrayList<Product>();
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();

        try {
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];

            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT, "The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                }
            }

            withdrawal.setId(null);
            withdrawal.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            withdrawal.setUserDestinationId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
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

            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeManualWithdrawal).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionWithdrawal = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountWithdrawal * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }

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

            BankOperation manualWithdrawal = new BankOperation();
            manualWithdrawal.setId(null);
            manualWithdrawal.setUserSourceId(BigInteger.valueOf(userId));
            manualWithdrawal.setProductId(product);
            manualWithdrawal.setTransactionId(withdrawal);
            manualWithdrawal.setCommisionId(commissionWithdrawal);
            BankOperationType operationType = entityManager.find(BankOperationType.class, Constante.sBankOperationTypeWithdrawal);
            manualWithdrawal.setBankOperationTypeId(operationType);
            BankOperationMode operationMode = entityManager.find(BankOperationMode.class, Constante.sBankOperationModeManual);
            manualWithdrawal.setBankOperationModeId(operationMode);
            Bank bank = entityManager.find(Bank.class, bankId);
            manualWithdrawal.setBankId(bank);
            manualWithdrawal.setBankOperationNumber(accountBank);
            entityManager.persist(manualWithdrawal);

            withdrawal.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(withdrawal);
            Usuario usuario = new Usuario();
            usuario.setEmail(emailUser);
            try {
                products = getProductsListByUserId(userId);
                for (Product p : products) {
                    Float amount_1 = 0F;
                    try {
                        if (p.getId().equals(Product.PREPAID_CARD)) {
                            CardResponse cardResponse = getCardByUserId(userId);
                            String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                            if (statusCardResponse.getCodigo().equals("00")) {
                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                            } else {
                                amount_1 = Float.valueOf(0);
                            }

                        } else {
                            amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                        }

                    } catch (NoResultException e) {
                        amount_1 = 0F;
                    }
                    p.setCurrentBalance(amount_1);
                }
            } catch (Exception ex) {

                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error loading products");
            }
            SendMailTherad sendMailTherad = new SendMailTherad("ES", accountBank, amountWithdrawal, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("4"));
            sendMailTherad.run();

            SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), Integer.valueOf("23"), amountWithdrawal, userId, entityManager);
            sendSmsThread.run();
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
        }

        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.EXITO, "EXITO", products);
        transactionResponse.setIdTransaction(withdrawal.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;

    }

    public TransactionResponse manualRecharge(Long bankId, String emailUser, String referenceNumberOperation,
            Float amountRecharge, Long productId, String conceptTransaction) {

        Long idTransaction = 0L;
        Long userId = 0L;
        int totalTransactionsByUser = 0;
        Long totalTransactionsByProduct = 0L;
        Double totalAmountByUser = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionRecharge = new Commission();
        Transaction recharge = new Transaction();
        ArrayList<Product> products = new ArrayList<Product>();
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();

        try {
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            //Validar preferencias
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];

            //Obtiene las transacciones del día para el usuario
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT, "The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                }
            }

            recharge.setId(null);
            recharge.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            recharge.setUserDestinationId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
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

            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeManualRecharge).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionRecharge = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountRecharge * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
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
            BankOperation manualRecharge = new BankOperation();
            manualRecharge.setId(null);
            manualRecharge.setUserSourceId(BigInteger.valueOf(userId));
            manualRecharge.setProductId(product);
            manualRecharge.setTransactionId(recharge);
            manualRecharge.setCommisionId(commissionRecharge);
            BankOperationType operationType = entityManager.find(BankOperationType.class, Constante.sBankOperationTypeRecharge);
            manualRecharge.setBankOperationTypeId(operationType);
            BankOperationMode operationMode = entityManager.find(BankOperationMode.class, Constante.sBankOperationModeManual);
            manualRecharge.setBankOperationModeId(operationMode);
            Bank bank = entityManager.find(Bank.class, bankId);
            manualRecharge.setBankId(bank);
            manualRecharge.setBankOperationNumber(referenceNumberOperation);

            entityManager.persist(manualRecharge);

            recharge.setTransactionStatus(TransactionStatus.IN_PROCESS.name());

            entityManager.merge(recharge);

            try {
                products = getProductsListByUserId(userId);
                for (Product p : products) {
                    Float amount_1 = 0F;
                    try {
                        if (p.getId().equals(Product.PREPAID_CARD)) {
                            CardResponse cardResponse = getCardByUserId(userId);
                            String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                            if (statusCardResponse.getCodigo().equals("00")) {
                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                            } else {
                                amount_1 = Float.valueOf(0);
                            }

                        } else {
                            amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                        }

                    } catch (NoResultException e) {
                        amount_1 = 0F;
                    }
                    p.setCurrentBalance(amount_1);
                }
            } catch (Exception ex) {

                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error loading products");
            }
            Usuario usuario = new Usuario();
            usuario.setEmail(emailUser);
            SendMailTherad sendMailTherad = new SendMailTherad("ES", referenceNumberOperation, conceptTransaction, amountRecharge, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("2"));
            sendMailTherad.run();

            SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), amountRecharge, referenceNumberOperation, Integer.valueOf("21"), userId, entityManager);
            sendSmsThread.run();
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
        }
        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.EXITO, "EXITO", products);
        transactionResponse.setIdTransaction(recharge.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;
    }

    public ProductListResponse getProductsByBankId(Long bankId, Long userId) {
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
                    if (product.getId().equals(Constants.PREPAY_CARD_CREDENTIAL)) {
                        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                        CardResponse cardResponse = getCardByUserId(userId);
                        String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
                        StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                        StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                        balanceHistory.setCurrentAmount(Float.valueOf(accountResponse.getComprasDisponibles()));
                    }
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
            StringBuilder sqlBuilder = new StringBuilder("SELECT b.countryId FROM alodigaWallet.bank b WHERE b.id IN (SELECT bhp.bankId FROM alodigaWallet.bank_has_product bhp WHERE bhp.productId IN (SELECT uhp.productId FROM alodigaWallet.user_has_product uhp  WHERE uhp.userSourceId='" + userId + "'))GROUP BY b.countryId;");
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
        } else {

            return new CountryListResponse(ResponseCode.EMPTY_LIST_COUNTRY, "Empty Countries List");
        }
        return new CountryListResponse(ResponseCode.EXITO, "", countrys);
    }

    public BalanceHistoryResponse getBalanceHistoryByUserAndProduct(Long userId, Long productId) {
        BalanceHistory balanceHistory = new BalanceHistory();
        try {
            balanceHistory = loadLastBalanceHistoryByAccount_(userId, productId);
//            if (productId.equals(Constants.PREPAY_CARD_CREDENTIAL)) {
//                AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
//                CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
//                CardResponse cardResponse = getCardByUserId(userId);
//                String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
//                StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
//                if (statusCardResponse.getCodigo().equals("00")) {
//                    StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
//                    balanceHistory.setCurrentAmount(Float.valueOf(accountResponse.getComprasDisponibles()));
//                } else {
//                    balanceHistory.setCurrentAmount(0);
//                }
//
//            }
            
        } catch (NoResultException e) {
            return new BalanceHistoryResponse(ResponseCode.BALANCE_HISTORY_NOT_FOUND_EXCEPTION, "Error loading BalanceHistory");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BalanceHistoryResponse(ResponseCode.ERROR_INTERNO, "Error loading BalanceHistory");
        }
        return new BalanceHistoryResponse(ResponseCode.EXITO, "", balanceHistory);
    }

    public BalanceHistory loadLastBalanceHistoryByAccount_(Long userId, Long productId) throws NoResultException {

        try {
            Query query = entityManager.createQuery("SELECT b FROM BalanceHistory b WHERE b.userId = " + userId + " AND b.productId.id = " + productId + " ORDER BY b.id desc");
            query.setMaxResults(1);
            BalanceHistory result = (BalanceHistory) query.setHint("toplink.refresh", "true").getSingleResult();
            return result;
        } catch (NoResultException e) {
            e.printStackTrace();
            throw new NoResultException();
        }

    }

    public void sendmailTest() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Kerwin");
        usuario.setApellido("Gomez");
        usuario.setCredencial("DAnye");
        usuario.setEmail("moisegrat@hotmail.com");
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
            AmazonSESSendMail.SendMail(mail.getSubject(), mail.getBody(), mail.getTo().get(0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Product> getProductsListByUserId(Long userId) throws NoResultException, Exception {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            userHasProducts = (List<UserHasProduct>) entityManager.createNamedQuery("UserHasProduct.findByUserSourceIdAllProduct", UserHasProduct.class).setParameter("userSourceId", userId).getResultList();

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

    public Boolean hasPrepayCardAsociated(Long userId) throws Exception {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            userHasProducts = (List<UserHasProduct>) entityManager.createNamedQuery("UserHasProduct.findByUserSourceId", UserHasProduct.class).setParameter("userSourceId", userId).getResultList();
            if (userHasProducts.size() >= 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public Boolean hasPrepayCard(Long userId) throws Exception {
        List<UserHasCard> userHasCards = new ArrayList<UserHasCard>();
        try {
            userHasCards = (List<UserHasCard>) entityManager.createNamedQuery("UserHasCard.findByUserId", UserHasCard.class).setParameter("userId", userId).getResultList();
            if (userHasCards.size() >= 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public TransactionResponse saveRechargeTopUp(String emailUser, Long productId, String cryptogramUser,
            String skudId, String destinationNumber, String senderNumber, Float amountRecharge, Float amountPayment, String language) {

        Long idTransaction = 0L;
        Long idPreferenceField = 0L;
        Long userId = 0L;
        int totalTransactionsByUser = 0;
        Long totalTransactionsByProduct = 0L;
        Double totalAmountByUser = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionTopUp = new Commission();

        TransactionResponse response = null;
        try {
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountRecharge) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constants.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constants.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constants.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT, "The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                        break;
                    case Constants.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                }
            }

            Transaction recharge = new Transaction();
            recharge.setId(null);
            recharge.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            recharge.setUserDestinationId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            recharge.setTopUpDescription("Destination:" + destinationNumber + " SkuidID:" + skudId);
            Product product = entityManager.find(Product.class, productId);
            recharge.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeTopUP);
            recharge.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSourceTopUP);
            recharge.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            recharge.setCreationDate(creationDate);
            recharge.setConcept(Constante.sTransactionConceptTopUp);
            recharge.setAmount(amountRecharge);
            recharge.setTransactionStatus(TransactionStatus.CREATED.name());
            recharge.setTotalAmount(amountRecharge);
            recharge.setId(recharge.getId());
            entityManager.persist(recharge);

            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeTopUP).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionTopUp = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountRecharge * amountCommission) / 100;
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

            amountPayment = amountRecharge + amountCommission;
            recharge.setTotalAmount(amountPayment);
            entityManager.merge(recharge);

            balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
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

            recharge.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(recharge);

            response = this.executeTopUp(skudId, amountRecharge, destinationNumber, senderNumber, emailUser);
            if (response.getCodigoRespuesta().equals(ResponseCode.EXITO.getCodigo())) {

                recharge.setTransactionStatus(TransactionStatus.COMPLETED.name());
                entityManager.merge(recharge);
                response.setIdTransaction(recharge.getId().toString());

                SendMailTherad sendMailTherad = new SendMailTherad("ES", destinationNumber, senderNumber, amountRecharge, amountPayment, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("6"));
                sendMailTherad.run();

                SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), destinationNumber, amountRecharge, Integer.valueOf("25"), userId, entityManager);
                sendSmsThread.run();

            } else {
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

        ArrayList<Product> productResponses = new ArrayList<Product>();
        try {
            productResponses = getProductsListByUserId(userId);
            for (Product p : productResponses) {
                Float amount_1 = 0F;
                try {
                    if (p.getId().equals(Product.PREPAID_CARD)) {
                        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                        CardResponse cardResponse = getCardByUserId(userId);
                        String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
                        StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                        if (statusCardResponse.getCodigo().equals("00")) {
                            StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                            amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                        } else {
                            amount_1 = Float.valueOf(0);
                        }

                    } else {
                        amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    }
                } catch (NoResultException e) {
                    amount_1 = 0F;
                }
                p.setCurrentBalance(amount_1);
            }

        } catch (Exception ex) {

            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error loading products");
        }

        response.setProducts(productResponses);
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
            for (Product p : products) {
                if (p.isIsPayTopUp()) {
                    Float amount = 0F;
                    try {
                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
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

    public ProductListResponse getProductsIsExchangeProductUserId(Long userId) {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        List<Product> products = new ArrayList<Product>();
        List<Product> productFinals = new ArrayList<Product>();
        try {
            products = getProductsListByUserId(userId);
            for (Product p : products) {
                if (p.isIsExchangeProduct()) {
                    Float amount = 0F;
                    try {
                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
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

    public String sendSmsSimbox(String text, String phoneNumber, Long userId) {
        try {
            return Utils.sendSmsSimbox(text, phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public Cumplimient LoadCumplimientStatus(Long userId) {
        try {
            Query query = entityManager.createQuery("SELECT c FROM Cumplimient c WHERE c.userSourceId = " + userId + " AND c.endingDate IS NULL ORDER BY c.id desc");
            query.setMaxResults(1);
            Cumplimient result = (Cumplimient) query.setHint("toplink.refresh", "true").getSingleResult();
            return result;
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }

    }

    public CumplimientResponse getCumplimientStatus(Long userId) {
        Cumplimient cumplimients = new Cumplimient();
        try {
            cumplimients = LoadCumplimientStatus(userId);
        } catch (NoResultException e) {
            e.printStackTrace();
            return new CumplimientResponse(ResponseCode.NOT_VALIDATE, "User Not Validate");
        }
        return new CumplimientResponse(ResponseCode.EXITO, "", cumplimients);
    }

    public CollectionListResponse getValidateCollection(Long userId, String language) {
        List<ValidationCollection> validationCollections = new ArrayList<ValidationCollection>();
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        try {
            responseUser = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            Country codeCountry = getCountryCode(responseUser.getDatosRespuesta().getMovil());
            validationCollections = entityManager.createNamedQuery("ValidationCollection.findByStatusByLanguage", ValidationCollection.class).setParameter("languageId", language).setParameter("countryId", codeCountry.getId()).getResultList();

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new CollectionListResponse(ResponseCode.ERROR_INTERNO, "Error validating collections");
        }

        return new CollectionListResponse(ResponseCode.EXITO, "", validationCollections);
    }

    public Country getCountryCode(String strAni) {
        long ani = Long.parseLong(strAni);

        String number = ani + "";

        Country aniCode = null;
        int index = number.length();

        while (aniCode == null && index > 0) {
            try {
                aniCode = (Country) entityManager.createQuery("SELECT c FROM Country c WHERE c.code=" + number.substring(0, index)).getSingleResult();
            } catch (Exception e) {
            }
            index--;
        }

        return aniCode;
    }

    public LanguageListResponse getLanguageByIso(String language) {
        List<Language> languages = null;
        try {
            languages = entityManager.createNamedQuery("Language.findByIso", Language.class).setParameter("iso", language).getResultList();

        } catch (Exception e) {
            return new LanguageListResponse(ResponseCode.ERROR_INTERNO, "Error loading countries");
        }
        return new LanguageListResponse(ResponseCode.EXITO, "", languages);
    }

    public Address saveAddress(Long userId, String estado, String ciudad, String zipCode, String addres1) throws RemoteException, Exception {

        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        try {
            responseUser = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            Country codeCountry = getCountryCode(responseUser.getDatosRespuesta().getMovil());
            Address address = new Address();
            address.setId(null);
            address.setCountryId(codeCountry);
            address.setStateId(null);
            address.setCityId(null);
            address.setCountyId(null);
            address.setAddress(addres1);
            address.setZipCode(zipCode);
            address.setStateName(estado);
            address.setCountyName(null);
            address.setCityName(ciudad);
            entityManager.persist(address);
            return address;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public CollectionListResponse saveCumplimient(Long userId, byte[] imgDocument, byte[] imgProfile, String estado, String ciudad, String zipCode, String addres1) {

        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        Cumplimient cumplimient = new Cumplimient();
        try {
            responseUser = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            Address address = saveAddress(userId, estado, ciudad, zipCode, addres1);
            OFACMethodWSProxy aCMethodWSProxy = new OFACMethodWSProxy();
            WsLoginResponse response;
            WsExcludeListResponse response2;
            response = aCMethodWSProxy.loginWS("alodiga", "d6f80e647631bb4522392aff53370502");
            response2 = aCMethodWSProxy.queryOFACList(response.getToken(), responseUser.getDatosRespuesta().getApellido(), responseUser.getDatosRespuesta().getNombre(), null, null, null, null, 0.5F);
            cumplimient.setUserSourceId(userId);
            cumplimient.setIsKYC(true);
            cumplimient.setIsAML(true);
            cumplimient.setBeginningDate(new Timestamp(new Date().getTime()));
            cumplimient.setEndingDate(null);
            cumplimient.setAprovedDate(null);
            cumplimient.setAMLPercent(Float.valueOf(response2.getPercentMatch()) * 100);
            cumplimient.setImgDocumentDate(imgDocument);
            cumplimient.setImgProfile(imgProfile);
            CumplimientStatus cumplimientStatus = entityManager.find(CumplimientStatus.class, CumplimientStatus.IN_PROCESS);
            cumplimient.setComplientStatusId(cumplimientStatus);
            cumplimient.setAgentComplientId(null);
            cumplimient.setAddressId(address);
            cumplimient.setAdditional(null);
            entityManager.persist(cumplimient);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new CollectionListResponse(ResponseCode.ERROR_INTERNO, "Error validating collections 1");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CollectionListResponse(ResponseCode.ERROR_INTERNO, "Error validating collections 1");
        }

        return new CollectionListResponse(ResponseCode.EXITO);

    }

    public ActivateCardResponses activateCard(Long userId, String card, String timeZone, String status) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            String Card = S3cur1ty3Cryt3r.aloEncrpter(card, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            if (!isCardUnique(Card)) {
                return new ActivateCardResponses(
                        ResponseCode.CARD_NUMBER_EXISTS, "CARD NUMBER EXISTS");
            }
            ignoreSSL();
            String encryptedString = Base64.encodeBase64String(encrypt(Card, Constants.PUBLIC_KEY));
            ChangeStatusCardResponse response = cardCredentialServiceClient.changeStatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, encryptedString, status);

            if (response.getCodigoRespuesta().equals("00") || response.getCodigoRespuesta().equals("-024")) {
                if (!hasPrepayCardAsociated(userId)) {
                    //Si no lo tiene se debe afiliar 
                    UserHasProduct userHasProduct2 = new UserHasProduct();
                    userHasProduct2.setProductId(Product.PREPAID_CARD);
                    userHasProduct2.setUserSourceId(userId);
                    userHasProduct2.setBeginningDate(new Timestamp(new Date().getTime()));
                    entityManager.persist(userHasProduct2);
                }
                if (!hasPrepayCard(userId)) {

                    Card card1 = new Card();
                    card1.setNumberCard(Card);
                    card1.setNameCard(responseUser.getDatosRespuesta().getNombre() + responseUser.getDatosRespuesta().getApellido());
                    entityManager.persist(card1);
                    entityManager.flush();
                    UserHasCard userHasCard = new UserHasCard();
                    userHasCard.setUserId(userId);
                    userHasCard.setCardId(card1);
                    entityManager.persist(userHasCard);
                }

                try {
                    products = getProductsListByUserId(userId);
                    for (Product p : products) {
                        Float amount_1 = 0F;
                        try {
                            if (p.getId().equals(Product.PREPAID_CARD)) {
                                CardResponse cardResponse = getCardByUserId(userId);
                                String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
                                StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                                if (statusCardResponse.getCodigo().equals("00")) {
                                    StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                    amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                                } else {
                                    amount_1 = Float.valueOf(0);
                                }

                            } else {
                                amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                            }

                        } catch (NoResultException e) {
                            amount_1 = 0F;
                        }
                        p.setCurrentBalance(amount_1);
                    }
                } catch (Exception ex) {

                    return new ActivateCardResponses(ResponseCode.ERROR_INTERNO, "Error loading products");
                }

                ChangeStatusCredentialCard changeStatusCredentialcardResponse = new ChangeStatusCredentialCard(response.getInicio(), response.getFin(), response.getTiempo(), response.getCodigoRespuesta(), response.getDescripcion(), response.getTicketWS());
                ActivateCardResponses activateCardResponses = new ActivateCardResponses(changeStatusCredentialcardResponse, ResponseCode.EXITO, "", products);
                activateCardResponses.setProducts(products);

                CardResponse respuestaTarjeta = getCardByUserId(userId);
                activateCardResponses.setNumberCard(respuestaTarjeta.getNumberCard());
                return activateCardResponses;
            } else if (response.getCodigoRespuesta().equals("-024")) {
                return new ActivateCardResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
            } else if (response.getCodigoRespuesta().equals("-011")) {
                return new ActivateCardResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
            } else if (response.getCodigoRespuesta().equals("-13")) {
                return new ActivateCardResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
            } else if (response.getCodigoRespuesta().equals("-14")) {
                return new ActivateCardResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
            } else if (response.getCodigoRespuesta().equals("-060")) {
                return new ActivateCardResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
            } else if (response.getCodigoRespuesta().equals("-120")) {
                return new ActivateCardResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
            } else if (response.getCodigoRespuesta().equals("-140")) {
                return new ActivateCardResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
            } else if (response.getCodigoRespuesta().equals("-160")) {
                return new ActivateCardResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
            }
            return new ActivateCardResponses(ResponseCode.ERROR_INTERNO, "ERROR INTERNO");

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new ActivateCardResponses(ResponseCode.ERROR_INTERNO, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ActivateCardResponses(ResponseCode.ERROR_INTERNO, "");
        }
    }

    public DesactivateCardResponses desactivateCard(Long userId, String card, String timeZone, String status) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        try {
            responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            ignoreSSL();
            //card = S3cur1ty3Cryt3r.aloEncrpter(card, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            String encryptedString = Base64.encodeBase64String(encrypt(card, Constants.PUBLIC_KEY));
            ChangeStatusCardResponse response = cardCredentialServiceClient.changeStatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, encryptedString, status);
            System.out.println(response.getCodigoRespuesta());
            if (response.getCodigoRespuesta().equals("00")) {
                ChangeStatusCredentialCard changeStatusCredentialcardResponse = new ChangeStatusCredentialCard(response.getInicio(), response.getFin(), response.getTiempo(), response.getCodigoRespuesta(), response.getDescripcion(), response.getTicketWS());
                return new DesactivateCardResponses(changeStatusCredentialcardResponse, ResponseCode.EXITO, "");
            } else if (response.getCodigoRespuesta().equals("-024")) {
                return new DesactivateCardResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
            } else if (response.getCodigoRespuesta().equals("-011")) {
                return new DesactivateCardResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
            } else if (response.getCodigoRespuesta().equals("-13")) {
                return new DesactivateCardResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
            } else if (response.getCodigoRespuesta().equals("-14")) {
                return new DesactivateCardResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
            } else if (response.getCodigoRespuesta().equals("-060")) {
                return new DesactivateCardResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
            } else if (response.getCodigoRespuesta().equals("-120")) {
                return new DesactivateCardResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
            } else if (response.getCodigoRespuesta().equals("-140")) {
                return new DesactivateCardResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
            } else if (response.getCodigoRespuesta().equals("-160")) {
                return new DesactivateCardResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
            }
            return new DesactivateCardResponses(ResponseCode.ERROR_INTERNO, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new DesactivateCardResponses(ResponseCode.ERROR_INTERNO, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new DesactivateCardResponses(ResponseCode.ERROR_INTERNO, "");
        }
    }

    public CheckStatusCardResponses checkStatusCard(Long userId, String card, String timeZone) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        try {
            responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            card = S3cur1ty3Cryt3r.aloEncrpter(card, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            String encryptedString = Base64.encodeBase64String(encrypt(card, Constants.PUBLIC_KEY));
            System.out.println("encryptedString " + encryptedString);
            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, encryptedString);
            if (statusCardResponse.getCodigo().equals("00")) {
                CheckStatusCredentialCard checkStatusCredentialCard = new CheckStatusCredentialCard(statusCardResponse.getCodigo(), statusCardResponse.getDescripcion(), statusCardResponse.getTicketWS(), statusCardResponse.getInicio(), statusCardResponse.getFin(), statusCardResponse.getTiempo(), statusCardResponse.getNumero(), statusCardResponse.getCuenta(), statusCardResponse.getCodigoEntidad(), statusCardResponse.getDescripcionEntidad(), statusCardResponse.getSucursal(), statusCardResponse.getCodigoProducto(), statusCardResponse.getDescripcionProducto(), statusCardResponse.getCodigoEstado(), statusCardResponse.getDescripcionEstado(), statusCardResponse.getActual(), statusCardResponse.getAnterior(), statusCardResponse.getDenominacion(), statusCardResponse.getTipo(), statusCardResponse.getIden(), statusCardResponse.getTelefono(), statusCardResponse.getDireccion(), statusCardResponse.getCodigoPostal(), statusCardResponse.getLocalidad(), statusCardResponse.getCodigoPais(), statusCardResponse.getDescripcionPais(), statusCardResponse.getMomentoUltimaActualizacion(), statusCardResponse.getMomentoUltimaOperacionAprobada(), statusCardResponse.getMomentoUltimaOperacionDenegada(), statusCardResponse.getMomentoUltimaBajaBoletin(), statusCardResponse.getContadorPinERR());
                return new CheckStatusCardResponses(checkStatusCredentialCard, ResponseCode.EXITO, "");
            } else if (statusCardResponse.getCodigo().equals("-024")) {
                return new CheckStatusCardResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
            } else if (statusCardResponse.getCodigo().equals("-011")) {
                return new CheckStatusCardResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
            } else if (statusCardResponse.getCodigo().equals("-13")) {
                return new CheckStatusCardResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
            } else if (statusCardResponse.getCodigo().equals("-14")) {
                return new CheckStatusCardResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
            } else if (statusCardResponse.getCodigo().equals("-060")) {
                return new CheckStatusCardResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
            } else if (statusCardResponse.getCodigo().equals("-120")) {
                return new CheckStatusCardResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
            } else if (statusCardResponse.getCodigo().equals("-140")) {
                return new CheckStatusCardResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
            } else if (statusCardResponse.getCodigo().equals("-160")) {
                return new CheckStatusCardResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
            } else if (statusCardResponse.getCodigo().equals("-030")) {
                return new CheckStatusCardResponses(ResponseCode.NON_EXISTENT_ACCOUNT, "Non-existent account");
            }
            return new CheckStatusCardResponses(ResponseCode.ERROR_INTERNO, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new CheckStatusCardResponses(ResponseCode.ERROR_INTERNO, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CheckStatusCardResponses(ResponseCode.ERROR_INTERNO, "");
        }

    }

    public CheckStatusAccountResponses checkStatusAccount(Long userId, String numberAccount, String timeZone) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
        try {
            responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            numberAccount = S3cur1ty3Cryt3r.aloEncrpter(numberAccount, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, numberAccount);
            accountResponse.setCodigo("00");
            if (accountResponse.getCodigo().equals("00")) {
                CheckStatusCredentialAccount checkStatusCredentialAccount = new CheckStatusCredentialAccount(accountResponse.getCodigo(), accountResponse.getDescripcion(), accountResponse.getNumero(), accountResponse.getCodigoEstado(), accountResponse.getDescripcionEstado(), accountResponse.getCodigoEntidad(), accountResponse.getDescripcionEntidad(), accountResponse.getSucursal(), accountResponse.getCodigoProducto(), accountResponse.getDescripcionProducto(), accountResponse.getCodigoPais(), accountResponse.getDescripcionPais(), accountResponse.getCodigoMoneda(), accountResponse.getDescripcionMoneda(), accountResponse.getVIP(), accountResponse.getHCC(), accountResponse.getULC(), accountResponse.getMCC(), accountResponse.getMomentoRenewal(), accountResponse.getMomentoUltimaActualizacion(), accountResponse.getMomentoUltimaOperacionAprobada(), accountResponse.getMomentoUltimaOperacionDenegada(), accountResponse.getMomentoUltimoBloqueo(), accountResponse.getMomentoUltimoDesbloqueo(), accountResponse.getComprasDisponibles(), accountResponse.getCuotasDisponibles(), accountResponse.getAdelantosDisponibles(), accountResponse.getPrestamosDisponibles(), accountResponse.getComprasLimites(), accountResponse.getCuotasLimites(), accountResponse.getAdelantosLimites(), accountResponse.getPrestamosLimites(), accountResponse.getFechaVencimiento(), accountResponse.getSaldo(), accountResponse.getPagoMinimo(), accountResponse.getSaldoDolar());
                return new CheckStatusAccountResponses(checkStatusCredentialAccount, ResponseCode.EXITO, "");
            } else if (accountResponse.getCodigo().equals("-030")) {
                return new CheckStatusAccountResponses(ResponseCode.NON_EXISTENT_ACCOUNT, "Non-existent account");
            } else if (accountResponse.getCodigo().equals("-024")) {
                return new CheckStatusAccountResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
            } else if (accountResponse.getCodigo().equals("-011")) {
                return new CheckStatusAccountResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
            } else if (accountResponse.getCodigo().equals("-13")) {
                return new CheckStatusAccountResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
            } else if (accountResponse.getCodigo().equals("-14")) {
                return new CheckStatusAccountResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
            } else if (accountResponse.getCodigo().equals("-060")) {
                return new CheckStatusAccountResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
            } else if (accountResponse.getCodigo().equals("-120")) {
                return new CheckStatusAccountResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
            } else if (accountResponse.getCodigo().equals("-140")) {
                return new CheckStatusAccountResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
            } else if (accountResponse.getCodigo().equals("-160")) {
                return new CheckStatusAccountResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
            }
            return new CheckStatusAccountResponses(ResponseCode.ERROR_INTERNO, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new CheckStatusAccountResponses(ResponseCode.ERROR_INTERNO, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CheckStatusAccountResponses(ResponseCode.ERROR_INTERNO, "");
        }

    }

    public TransferCardToCardResponses transferCardToCardAutorization(Long userId, String numberCardOrigin, String numberCardDestinate, String balance, Long idUserDestination, String conceptTransaction) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        AutorizationCredentialServiceClient autorizationCredentialServiceClient = new AutorizationCredentialServiceClient();
        ArrayList<Product> products = new ArrayList<Product>();
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Transaction transfer = new Transaction();

        try {

            RespuestaUsuario responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            RespuestaUsuario userDestination = proxy.getUsuarioporId("usuarioWS", "passwordWS", idUserDestination.toString());
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            ignoreSSL();

            numberCardOrigin = S3cur1ty3Cryt3r.aloEncrpter(numberCardOrigin, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            numberCardDestinate = S3cur1ty3Cryt3r.aloEncrpter(numberCardDestinate, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            SimpleDateFormat sdg = new SimpleDateFormat("yyyyMMdd");
            String hour = sdf.format(timestamp);
            System.out.println(hour);
            String date = sdg.format(timestamp);
            System.out.println(date);
            CardToCardTransferResponse cardToCardTransferResponse = autorizationCredentialServiceClient.cardToCardTransfer(date, hour, numberCardOrigin, numberCardDestinate, balance);

            if (cardToCardTransferResponse.getCodigoError().equals("-1")) {
                TransferCardToCardCredential cardCredential = new TransferCardToCardCredential(cardToCardTransferResponse.getCodigoError(), cardToCardTransferResponse.getMensajeError(), cardToCardTransferResponse.getCodigoRespuesta(), cardToCardTransferResponse.getMensajeRespuesta(), cardToCardTransferResponse.getCodigoAutorizacion(), cardToCardTransferResponse.getSaldoPosterior(), cardToCardTransferResponse.getSaldo(), cardToCardTransferResponse.getSaldoPosteriorCuentaDestino(), cardToCardTransferResponse.getSaldoCuentaDestino());

                transfer.setId(null);
                transfer.setUserSourceId(BigInteger.valueOf(userId));
                transfer.setUserDestinationId(BigInteger.valueOf(idUserDestination));
                Product product = entityManager.find(Product.class, 3L);
                transfer.setProductId(product);
                TransactionType transactionType = entityManager.find(TransactionType.class, Constants.TRANSFER_CARD_TO_CARD);
                transfer.setTransactionTypeId(transactionType);
                TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constants.TRANSFER_CARD_TO_CARD_SOURCE);
                transfer.setTransactionSourceId(transactionSource);
                Date date_ = new Date();
                Timestamp creationDate = new Timestamp(date_.getTime());
                transfer.setCreationDate(creationDate);

                transfer.setConcept(Constants.TRANSACTION_CONCEPT_TRANSFER_CARD_TO_CARD);
                transfer.setAmount(Float.valueOf(balance));
                transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
                transfer.setTotalAmount(Float.valueOf(balance));
                entityManager.persist(transfer);

                BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, 3L);
                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                if (balanceUserSource == null) {
                    balanceHistory.setOldAmount(Float.valueOf(cardCredential.getRearBalanceAccountDestination()));
                    balanceHistory.setCurrentAmount(Float.valueOf(cardCredential.getDestinationAccountBalance()));
                } else {
                    balanceHistory.setOldAmount(Float.valueOf(cardCredential.getRearBalanceAccountDestination()));
                    balanceHistory.setCurrentAmount(Float.valueOf(cardCredential.getDestinationAccountBalance()));
                    balanceHistory.setVersion(balanceUserSource.getId());
                }
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transfer);
                Date balanceDate = new Date();
                Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                entityManager.persist(balanceHistory);

                BalanceHistory balanceUserDestination = loadLastBalanceHistoryByAccount(idUserDestination, 3L);
                balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(idUserDestination);
                if (balanceUserDestination == null) {
                    balanceHistory.setOldAmount(Float.valueOf(cardCredential.getRearBalanceAccountDestination()));
                    balanceHistory.setCurrentAmount(Float.valueOf(cardCredential.getDestinationAccountBalance()));
                } else {
                    balanceHistory.setOldAmount(Float.valueOf(cardCredential.getRearBalanceAccountDestination()));
                    balanceHistory.setCurrentAmount(Float.valueOf(cardCredential.getDestinationAccountBalance()));
                    balanceHistory.setVersion(balanceUserDestination.getId());
                }
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transfer);
                balanceDate = new Date();
                balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                entityManager.persist(balanceHistory);

                try {
                    products = getProductsListByUserId(userId);
                    for (Product p : products) {
                        Float amount_1 = 0F;
                        try {
                            if (p.getId().equals(Product.PREPAID_CARD)) {
                                CardResponse cardResponse = getCardByUserId(userId);
                                String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
                                StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                                if (statusCardResponse.getCodigo().equals("00")) {
                                    StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                    amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                                } else {
                                    amount_1 = Float.valueOf(0);
                                }
                            } else {
                                amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                            }

                        } catch (NoResultException e) {
                            amount_1 = 0F;
                        }
                        p.setCurrentBalance(amount_1);
                    }
                } catch (Exception ex) {

                    return new TransferCardToCardResponses(ResponseCode.ERROR_INTERNO, "Error loading products");
                }
                SendMailTherad sendMailTherad = new SendMailTherad("ES", Float.valueOf(balance), conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), responseUser.getDatosRespuesta().getEmail(), Integer.valueOf("11"));
                sendMailTherad.run();

                SendMailTherad sendMailTherad1 = new SendMailTherad("ES", Float.valueOf(balance), conceptTransaction, userDestination.getDatosRespuesta().getNombre() + " " + userDestination.getDatosRespuesta().getApellido(), userDestination.getDatosRespuesta().getEmail(), Integer.valueOf("12"));
                sendMailTherad1.run();

                SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), Float.valueOf(balance), Integer.valueOf("30"), userId, entityManager);
                sendSmsThread.run();

                SendSmsThread sendSmsThread1 = new SendSmsThread(userDestination.getDatosRespuesta().getMovil(), Float.valueOf(balance), Integer.valueOf("31"), Long.valueOf(userDestination.getDatosRespuesta().getUsuarioID()), entityManager);
                sendSmsThread1.run();

                TransferCardToCardResponses cardResponses = new TransferCardToCardResponses(cardCredential, ResponseCode.EXITO, "", products);
                cardResponses.setIdTransaction(transfer.getId().toString());
                cardResponses.setProducts(products);
                return cardResponses;
            } else if (cardToCardTransferResponse.getCodigoError().equals("204")) {
                return new TransferCardToCardResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("913")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_AMOUNT, "INVALID AMOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("203")) {
                return new TransferCardToCardResponses(ResponseCode.EXPIRATION_DATE_DIFFERS, "EXPIRATION DATE DIFFERS");
            } else if (cardToCardTransferResponse.getCodigoError().equals("205")) {
                return new TransferCardToCardResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("202")) {
                return new TransferCardToCardResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("201")) {
                return new TransferCardToCardResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("03")) {
                return new TransferCardToCardResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("28")) {
                return new TransferCardToCardResponses(ResponseCode.LOCKED_CARD, "LOCKED_CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("211")) {
                return new TransferCardToCardResponses(ResponseCode.BLOCKED_ACCOUNT, "BLOCKED ACCOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("210")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_ACCOUNT, "INVALID ACCOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("998")) {
                return new TransferCardToCardResponses(ResponseCode.INSUFFICIENT_BALANCE, "INSUFFICIENT BALANCE");
            } else if (cardToCardTransferResponse.getCodigoError().equals("986")) {
                return new TransferCardToCardResponses(ResponseCode.INSUFFICIENT_LIMIT, "INSUFFICIENT LIMIT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("987")) {
                return new TransferCardToCardResponses(ResponseCode.CREDIT_LIMIT_0, "CREDIT LIMIT 0");
            } else if (cardToCardTransferResponse.getCodigoError().equals("988")) {
                return new TransferCardToCardResponses(ResponseCode.CREDIT_LIMIT_0_OF_THE_DESTINATION_ACCOUNT, "CREDIT LIMIT 0 OF THE DESTINATION ACCOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("999")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_PROCESSING_THE_TRANSACTION, "ERROR PROCESSING THE TRANSACTION");
            } else if (cardToCardTransferResponse.getCodigoError().equals("101")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_TRANSACTION, "INVALID TRANSACTION");
            } else if (cardToCardTransferResponse.getCodigoError().equals("105")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATION_THE_TERMINAL, "ERROR VALIDATION THE TERMINAL");
            } else if (cardToCardTransferResponse.getCodigoError().equals("241")) {
                return new TransferCardToCardResponses(ResponseCode.DESTINATION_ACCOUNT_LOCKED, "DESTINATION ACCOUNT LOCKED");
            } else if (cardToCardTransferResponse.getCodigoError().equals("230")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_DESTINATION_CARD, "INVALID DESTINATION CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("240")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_DESTINATION_ACCOUNT, "INVALID DESTINATION ACCOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("301")) {
                return new TransferCardToCardResponses(ResponseCode.THE_AMOUNT_MUST_BE_POSITIVE_AND_THE_AMOUNT_IS_REPORTED, "THE AMOUNT MUST BE POSITIVE AND THE AMOUNT IS REPORTED");
            } else if (cardToCardTransferResponse.getCodigoError().equals("302")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_TRANSACTION_DATE, "INVALID TRANSACTION DATE");
            } else if (cardToCardTransferResponse.getCodigoError().equals("303")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_TRANSACTION_TIME, "INVALID TRANSACTION TIME");
            } else if (cardToCardTransferResponse.getCodigoError().equals("994")) {
                return new TransferCardToCardResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NN");
            } else if (cardToCardTransferResponse.getCodigoError().equals("991")) {
                return new TransferCardToCardResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_SN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION SN");
            } else if (cardToCardTransferResponse.getCodigoError().equals("992")) {
                return new TransferCardToCardResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (cardToCardTransferResponse.getCodigoError().equals("993")) {
                return new TransferCardToCardResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (cardToCardTransferResponse.getCodigoError().equals("990")) {
                return new TransferCardToCardResponses(ResponseCode.TRASACTION_BETWEEN_ACCOUNTS_NOT_ALLOWED, "TRASACTION BETWEEN ACCOUNTS NOT ALLOWED");
            } else if (cardToCardTransferResponse.getCodigoError().equals("120")) {
                return new TransferCardToCardResponses(ResponseCode.TRADE_VALIDATON_ERROR, "TRADE VALIDATON ERROR");
            } else if (cardToCardTransferResponse.getCodigoError().equals("110")) {
                return new TransferCardToCardResponses(ResponseCode.DESTINATION_CARD_DOES_NOT_SUPPORT_TRANSACTION, "DESTINATION CARD DOES NOT SUPPORT TRANSACTION");
            } else if (cardToCardTransferResponse.getCodigoError().equals("111")) {
                return new TransferCardToCardResponses(ResponseCode.OPERATION_NOT_ENABLED_FOR_THE_DESTINATION_CARD, "OPERATION NOT ENABLED FOR THE DESTINATION CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("206")) {
                return new TransferCardToCardResponses(ResponseCode.BIN_NOT_ALLOWED, "BIN NOT ALLOWED");
            } else if (cardToCardTransferResponse.getCodigoError().equals("207")) {
                return new TransferCardToCardResponses(ResponseCode.STOCK_CARD, "STOCK CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("205")) {
                return new TransferCardToCardResponses(ResponseCode.THE_ACCOUNT_EXCEEDS_THE_MONTHLY_LIMIT, "THE ACCOUNT EXCEEDS THE MONTHLY LIMIT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("101")) {
                return new TransferCardToCardResponses(ResponseCode.THE_PAN_FIELD_IS_MANDATORY, "THE PAN FIELD IS MANDATORY");
            } else if (cardToCardTransferResponse.getCodigoError().equals("102")) {
                return new TransferCardToCardResponses(ResponseCode.THE_AMOUNT_TO_BE_RECHARGE_IS_INCORRECT, "THE AMOUNT TO BE RECHARGE IS INCORRECT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("3")) {
                return new TransferCardToCardResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("8")) {
                return new TransferCardToCardResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("33")) {
                return new TransferCardToCardResponses(ResponseCode.THE_AMOUNT_MUST_BE_GREATER_THAN_0, "THE AMOUNT MUST BE GREATER THAN 0");
            } else if (cardToCardTransferResponse.getCodigoError().equals("1")) {
                return new TransferCardToCardResponses(ResponseCode.SUCCESSFUL_RECHARGE, "SUCCESSFUL RECHARGE");
            } else if (cardToCardTransferResponse.getCodigoError().equals("410")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATING_PIN, "THE PAN FIELD IS MANDATORY");
            } else if (cardToCardTransferResponse.getCodigoError().equals("430")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATING_CVC1, "ERROR VALIDATING CVC1");
            } else if (cardToCardTransferResponse.getCodigoError().equals("400")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATING_CVC2, "ERROR VALIDATING CVC2");
            } else if (cardToCardTransferResponse.getCodigoError().equals("420")) {
                return new TransferCardToCardResponses(ResponseCode.PIN_CHANGE_ERROR, "PIN CHANGE ERROR");
            } else if (cardToCardTransferResponse.getCodigoError().equals("250")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATING_THE_ITEM, " ERROR VALIDATING THE ITEM");
            }
            return new TransferCardToCardResponses(ResponseCode.ERROR_INTERNO, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new TransferCardToCardResponses(ResponseCode.ERROR_INTERNO, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new TransferCardToCardResponses(ResponseCode.ERROR_INTERNO, "");
        }

    }

    private boolean isCardUnique(String card) {
        try {
            entityManager
                    .createNamedQuery("Card.findByNumberCard", Card.class)
                    .setParameter("numberCard", card).getSingleResult();
        } catch (NoResultException e) {
            return true;
        }
        return false;
    }

    private void ignoreSSL() {
        try {
            XTrustProvider.install();
            final String TEST_URL = "https://10.70.10.71:8000/CASA_SRTMX_TarjetaService?wsdl";
            URL url = new URL(TEST_URL);
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            httpsCon.connect();
            InputStream is = httpsCon.getInputStream();
            int nread = 0;
            byte[] buf = new byte[8192];
            while ((nread = is.read(buf)) != -1) {
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    public CardResponse getCardByUserId(Long userId) {
        UserHasCard userHasCard = new UserHasCard();
        try {
            userHasCard = (UserHasCard) entityManager.createNamedQuery("UserHasCard.findByUserId", UserHasCard.class).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.EMPTY_LIST_HAS_CARD, "Error loading cards");
        } catch (Exception e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.ERROR_INTERNO, "Error loading cards");
        }
        return new CardResponse(ResponseCode.EXITO, "", userHasCard.getCardId().getNumberCard());
    }

    public CardListResponse getCardsListByUserId(Long userId) throws NoResultException, Exception {
        List<UserHasCard> userHasCards = new ArrayList<UserHasCard>();
        Card card = new Card();
        List<Card> cards = new ArrayList<Card>();
        try {
            userHasCards = (List<UserHasCard>) entityManager.createNamedQuery("UserHasCard.findByUserIdAndParentId", UserHasCard.class).setParameter("userId", userId).getResultList();

            if (userHasCards.size() <= 0) {
                return new CardListResponse(ResponseCode.USER_NOT_HAS_CARD, "They are not cards asociated");
            }

            for (UserHasCard uhc : userHasCards) {

                cards = (List<Card>) entityManager.createNamedQuery("Card.findByParentId", Card.class).setParameter("parentId", uhc.getCardId().getId()).getResultList();

                if (cards.size() <= 0) {
                    return new CardListResponse(ResponseCode.DOES_NOT_HAVE_AN_ASSOCIATED_COMPANION_CARD, "Does not have an associated companion card");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CardListResponse(ResponseCode.ERROR_INTERNO, "Error loading cards");
        }
        return new CardListResponse(ResponseCode.EXITO, "", cards);
    }

    public ProductListResponse getProductsRemettenceByUserId(Long userId) {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        List<Product> products = new ArrayList<Product>();
        List<Product> productFinals = new ArrayList<Product>();
        try {
            products = getProductsListByUserId(userId);
            for (Product p : products) {
                if (p.isIsRemettence()) {
                    Float amount = 0F;
                    try {
                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
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

    public RemittanceResponse processRemettenceAccount(Long userId,
            Float amountOrigin,
            Float totalAmount,
            Float amountDestiny,
            String exchangeRateId,
            String ratePaymentNetworkId,
            String originCurrentId,
            String destinyCurrentId,
            String paymentNetworkId,
            String deliveryFormId,
            Long addressId,
            String remittentCountryId,
            String remittentStateName,
            String remittentCityName,
            String remittentAddress,
            String remittentZipCode,
            String remittentStateId,
            String remittentCityId,
            String receiverFirstName,
            String receiverMiddleName,
            String receiverLastName,
            String receiverSecondSurname,
            String receiverPhoneNumber,
            String receiverEmail,
            String receiverCountryId,
            String receiverCityId,
            String receiverStateId,
            String receiverStateName,
            String receiverCityName,
            String receiverAddress,
            String receiverZipCode,
            String languageId) {
        try {
            SimpleDateFormat sdg = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String applicationDate = sdg.format(timestamp);
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario userSource;
            List<Commission> commissions = new ArrayList<Commission>();
            List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
            List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
            Commission commissionTransfer = new Commission();
            Float amountCommission = 0.00F;
            short isPercentCommission = 0;
            Timestamp begginingDateTime = new Timestamp(0);
            Timestamp endingDateTime = new Timestamp(0);

            int totalTransactionsByUser = 0;
            Long totalTransactionsByProduct = 0L;
            Double totalAmountByUser = 0.00D;
            Long idTransaction = 0L;
            Transaction transfer = new Transaction();
            String remittentCountryId_ = null;
            String remittentCityId_ = null;
            String remittentStateId_ = null;
            String remittentStateName_ = null;
            String remittentCityName_ = null;
            String remittentAddress_ = null;
            String remittentZipCode_ = null;

            userSource = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
            String middleName = userSource.getDatosRespuesta().getNombre().split(" ")[0].trim();
            String secondSurname = userSource.getDatosRespuesta().getApellido().split(" ")[0].trim();

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, Constants.PRODUCT_REMITTANCE);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", Constants.PRODUCT_REMITTANCE).setParameter("transactionTypeId", Constante.sTransationTypeTA).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + Constants.PRODUCT_REMITTANCE + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionTransfer = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (totalAmount * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new RemittanceResponse(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
            }
            Float amountTransferTotal = totalAmount + amountCommission;
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountTransferTotal) {
                return new RemittanceResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            //Validar preferencias
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];

            //Obtiene las transacciones del dÃ­a para el usuario
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            //Obtiene la sumatoria de los montos de las transacciones del usuario
            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            //Obtiene las transacciones del dÃ­a para el producto que se estÃ¡ comprando
            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(Constants.PRODUCT_REMITTANCE, userId, begginingDateTime, endingDateTime);

            //Cotejar las preferencias vs las transacciones del usuario
            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT, "The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                }
            }

            //Crear el objeto Transaction para registrar la transferencia del cliente
            transfer.setId(null);
            transfer.setUserSourceId(BigInteger.valueOf(userSource.getDatosRespuesta().getUsuarioID()));
            transfer.setUserDestinationId(null);
            Product product = entityManager.find(Product.class, Constants.PRODUCT_REMITTANCE);
            transfer.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeTR);
            transfer.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            transfer.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            transfer.setCreationDate(creationDate);
            //cambiar por valor de parÃ¡metro
            transfer.setConcept(Constante.sTransactionConceptTranferRemmittance);
            transfer.setAmount(totalAmount);
            transfer.setTransactionStatus(TransactionStatus.CREATED.name());
            transfer.setTotalAmount(totalAmount);
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
            balanceUserSource = loadLastBalanceHistoryByAccount(userId, Constants.PRODUCT_REMITTANCE);
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

            //Se actualiza el estado de la transaccion a COMPLETED
            transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(transfer);

            WSRemittenceMobileProxy wSRemittenceMobileProxy = new WSRemittenceMobileProxy();
            WsAddressListResponse addressListResponse = new WsAddressListResponse();
            WsRemittenceResponse response = new WsRemittenceResponse();
            if (addressId != 0) {
                addressListResponse = wSRemittenceMobileProxy.getAddressById(addressId);
                remittentCountryId_ = String.valueOf(addressListResponse.getAddresses(0).getCountry().getId());
                remittentCityId_ = String.valueOf(addressListResponse.getAddresses(0).getCity().getId());
                remittentStateId_ = String.valueOf(addressListResponse.getAddresses(0).getState().getId());
                remittentStateName_ = addressListResponse.getAddresses(0).getStateName();
                remittentCityName_ = addressListResponse.getAddresses(0).getCityName();
                remittentAddress_ = addressListResponse.getAddresses(0).getAddress();
                remittentZipCode_ = addressListResponse.getAddresses(0).getZipCode();
            } else {
                remittentCountryId_ = remittentCountryId;
                if (remittentCityId != null) {
                    remittentCityId_ = remittentCityId;
                } else {
                    remittentCityId_ = null;
                }

                if (remittentStateId_ != null) {
                    remittentStateId_ = remittentStateId;
                } else {
                    remittentStateId_ = null;
                }

                if (remittentStateName_ != null) {
                    remittentStateName_ = remittentStateName;
                } else {
                    remittentStateName_ = null;
                }

                if (remittentCityName_ != null) {
                    remittentCityName_ = remittentCityName;
                } else {
                    remittentCityName_ = null;
                }

                remittentAddress_ = addressListResponse.getAddresses(0).getAddress();
                remittentZipCode_ = addressListResponse.getAddresses(0).getZipCode();
            }
            response = wSRemittenceMobileProxy.saverRemittence(applicationDate,
                    Constants.COMMENTARY_REMETTENCE,
                    amountOrigin,
                    totalAmount,
                    Constants.SENDING_OPTION_SMS_REMETTENCE,
                    amountDestiny,
                    Constants.BANK_REMETTENCE,
                    Constants.PAYMENT_SERVICE_REMETTENCE,
                    Constants.ADDITIONAL_CHANGES_REMITTANCE,
                    Constants.CORRESPONDENT_REMITTANCE,
                    Constants.SALES_TYPE_REMETTENCE,
                    exchangeRateId,
                    ratePaymentNetworkId,
                    Constants.SALES_PRICE_REMITTANCE,
                    languageId,
                    originCurrentId,
                    destinyCurrentId,
                    Constants.STORE_REMETTENCE,
                    Constants.PAYMENT_METHOD_REMITTANCE,
                    Constants.SERVICE_TYPE_REMITTANCE,
                    paymentNetworkId,
                    Constants.POINT_REMITTANCE,
                    Constants.USER_REMITTANCE,
                    Constants.CASH_BOX_REMITTANCE,
                    deliveryFormId,
                    userSource.getDatosRespuesta().getNombre(),
                    middleName,
                    userSource.getDatosRespuesta().getApellido(),
                    secondSurname,
                    userSource.getDatosRespuesta().getMovil(),
                    userSource.getDatosRespuesta().getEmail(),
                    remittentCountryId_,
                    remittentCityId_,
                    remittentStateId_,
                    remittentStateName_,
                    remittentCityName_,
                    remittentAddress_,
                    remittentZipCode_,
                    receiverFirstName,
                    receiverMiddleName,
                    receiverLastName,
                    receiverSecondSurname,
                    receiverPhoneNumber,
                    receiverEmail,
                    receiverCountryId,
                    receiverCityId,
                    receiverStateId,
                    receiverStateName,
                    receiverCityName,
                    receiverAddress,
                    receiverZipCode);
            if (addressId == 0) {
                proxy.actualizarUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId), String.valueOf(response.getRemittanceSingleResponse().getAddressId()));
            }
            RemittanceResponse remittanceResponse = new RemittanceResponse(response.getRemittanceSingleResponse().getId(), response.getRemittanceSingleResponse().getApplicationDate(), response.getRemittanceSingleResponse().getCommentary(), response.getRemittanceSingleResponse().getAmountOrigin(), response.getRemittanceSingleResponse().getTotalAmount(), response.getRemittanceSingleResponse().getSendingOptionSMS(), response.getRemittanceSingleResponse().getAmountDestiny(), response.getRemittanceSingleResponse().getBank(), response.getRemittanceSingleResponse().getPaymentServiceId(), response.getRemittanceSingleResponse().getSecondaryKey(), response.getRemittanceSingleResponse().getAdditionalChanges(), response.getRemittanceSingleResponse().getCreationDate(), response.getRemittanceSingleResponse().getCreationHour(), response.getRemittanceSingleResponse().getLocalSales(), response.getRemittanceSingleResponse().getReserveField1(), response.getRemittanceSingleResponse().getRemittent(), response.getRemittanceSingleResponse().getReceiver(), response.getRemittanceSingleResponse().getCorrespondent(), response.getRemittanceSingleResponse().getAddressReciever(), response.getRemittanceSingleResponse().getSalesType(), response.getRemittanceSingleResponse().getAddressRemittent(), response.getRemittanceSingleResponse().getExchangeRate(), response.getRemittanceSingleResponse().getRatePaymentNetwork(), response.getRemittanceSingleResponse().getLanguage(), response.getRemittanceSingleResponse().getOriginCurrent(), response.getRemittanceSingleResponse().getDestinyCurrent(), response.getRemittanceSingleResponse().getPaymentMethod(), response.getRemittanceSingleResponse().getServiceType(), response.getRemittanceSingleResponse().getPaymentNetwork(), response.getRemittanceSingleResponse().getPaymentNetworkPoint(), response.getRemittanceSingleResponse().getCashBox(), response.getRemittanceSingleResponse().getCashier(), response.getRemittanceSingleResponse().getStatus(), response.getRemittanceSingleResponse().getRemittanceNumber(), response.getRemittanceSingleResponse().getPaymentKey(), response.getRemittanceSingleResponse().getCorrelative(), response.getRemittanceSingleResponse().getDeliveryForm(), ResponseCode.EXITO, "");
            remittanceResponse.setAmountTransferTotal(String.valueOf(amountTransferTotal));
            return remittanceResponse;

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new RemittanceResponse(ResponseCode.ERROR_INTERNO, "");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return new RemittanceResponse(ResponseCode.ERROR_INTERNO, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new RemittanceResponse(ResponseCode.ERROR_INTERNO, "");
        }

    }

    public RechargeAfinitasResponses saveRechargeAfinitas(String emailUser, Float amountRecharge, String currency, String cardNumber, String expirationYear, String expirationMonth, String cvv, String cardHolderName) {

        Long idTransaction = 0L;
        Long idPreferenceField = 0L;
        int totalTransactionsByUser = 0;
        Long totalTransactionsByProduct = 0L;
        Double totalAmountByUser = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionRecharge = new Commission();
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction transfer = new Transaction();
        AfinitasPaymentIntegration afinitasPaymentIntegration = new AfinitasPaymentIntegration();
        ChargeResponse chargeResponse = new ChargeResponse();
        try {

            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario userSource_ = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            Long userId = Long.valueOf(userSource_.getDatosRespuesta().getUsuarioID());

            //Validar preferencias
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];

            //Obtiene la cantidad de transacciones del dÃ­a para el usuario pasarle el 
            totalTransactionsByUser = TransactionsByUserByTransactionByProductCurrentDate(userId, begginingDateTime, endingDateTime, Constants.PRODUCT_AFINITAS, Constante.sTransationTypeAF);

            //Obtiene la sumatoria de los montos de las transacciones del usuario
            totalAmountByUser = AmountMaxByUserByUserByTransactionByProductCurrentDate(userId, begginingDateTime, endingDateTime, Constants.PRODUCT_AFINITAS, Constante.sTransationTypeAF);

            //Obtiene las transacciones del dÃ­a para el producto que se estÃ¡ comprando
            totalTransactionsByProduct = TransactionsByProductByUserByTransactionCurrentDate(Constants.PRODUCT_AFINITAS, userId, begginingDateTime, endingDateTime, Constante.sTransationTypeAF);

            //Cotejar las preferencias vs las transacciones del usuario
            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT, "The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                }
            }

            //Crear el objeto Transaction para registrar la transferencia del cliente
            transfer.setId(null);
            transfer.setUserSourceId(BigInteger.valueOf(userSource_.getDatosRespuesta().getUsuarioID()));
            transfer.setUserDestinationId(BigInteger.valueOf(userSource_.getDatosRespuesta().getUsuarioID()));
            Product product = entityManager.find(Product.class, Constants.PRODUCT_AFINITAS);
            transfer.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeAF);
            transfer.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            transfer.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            transfer.setCreationDate(creationDate);
            //cambiar por valor de parÃ¡metro
            transfer.setConcept(Constante.sTransactionConceptPurchaseBalance);
            transfer.setAmount(amountRecharge);
            transfer.setTransactionStatus(TransactionStatus.CREATED.name());
            transfer.setTotalAmount(amountRecharge);
            System.out.println("transferrrrrrrrrr" + transfer);
            entityManager.persist(transfer);

            //Revisar si la transaccion esta sujeta a comisiones
            try {
                    commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", Constants.PRODUCT_AFINITAS).setParameter("transactionTypeId", Constante.sTransationTypeAF).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + Constants.PRODUCT_AFINITAS + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionRecharge = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountRecharge * amountCommission) / 100;
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
                commissionItem.setTransactionId(transfer);
                entityManager.persist(commissionItem);
            } catch (NoResultException e) {
                e.printStackTrace();
                return new RechargeAfinitasResponses(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
            }
            //Se actualiza el estatus de la transaccion a IN_PROCESS
            transfer.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(transfer);

            chargeResponse = afinitasPaymentIntegration.afinitasCharge(String.valueOf(amountRecharge), currency, cardNumber, expirationYear, expirationMonth, cvv, cardHolderName);
            chargeResponse.setStatus("true");
            if (chargeResponse.getStatus().equals("true")) {
                //Se actualizan los saldos de los usuarios involucrados en la transferencia
                //Balance History del usuario que transfiere el saldo
                BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, Constants.PRODUCT_AFINITAS);
                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
                Float currentAmountUserSource = balanceUserSource.getCurrentAmount() + amountRecharge;
                balanceHistory.setCurrentAmount(currentAmountUserSource);
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transfer);
                Date balanceDate = new Date();
                Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                balanceHistory.setVersion(balanceUserSource.getId());
                entityManager.persist(balanceHistory);
                System.out.println("BALANCE HISTORY"+ balanceHistory );
                //Se actualiza el estado de la transaccion a COMPLETED
                transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
                entityManager.merge(transfer);

                //Envias notificaciones
                //envias sms
                ////////////////////////////////////////////////////////////
                /// se incorpora para delvolver el saldo actual del cliente///
                /////////////////////////////////////////////////////////////
                products = getProductsListByUserId(userId);
                for (Product p : products) {
                    Float amount = 0F;
                    try {
//                        if (p.getId().equals(Product.PREPAID_CARD)) {
//                            AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
//                            CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
//                            CardResponse cardResponse = getCardByUserId(userId);
//                            String cardEncripter = Base64.encodeBase64String(encrypt(cardResponse.getNumberCard(), Constants.PUBLIC_KEY));
//                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
//                            statusCardResponse.setCodigo("00");
//                            if (statusCardResponse.getCodigo().equals("00")) {
//                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
//                                amount = Float.valueOf(accountResponse.getComprasDisponibles());
//                            } else {
//                                amount = Float.valueOf(0);
//                            }
//
//                        } else {

                            amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                        //}
                    } catch (NoResultException e) {
                        amount = 0F;
                    }
                    p.setCurrentBalance(amount);
                }

                ////////////////////////////////////////////////////////////
                /// se incorpora para delvolver el saldo actual del cliente///
                /////////////////////////////////////////////////////////////
                //Envias notificaciones
                //envias sms Y coreo
                //correo a quien envia
//            SendMailTherad sendMailTherad = new SendMailTherad("ES", amountTransfer, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("8"));
//            sendMailTherad.run();
//            
//
//            //Envia quien envia 
//            SendSmsThread sendSmsThread = new SendSmsThread(userSource_.getDatosRespuesta().getMovil(), amountTransfer, Integer.valueOf("27"), userId, entityManager);
//            sendSmsThread.run();
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("000000")) {
                    return new RechargeAfinitasResponses(ResponseCode.NOT_AUTHORIZED, "NOT AUTHORIZED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("1")) {
                    return new RechargeAfinitasResponses(ResponseCode.CALL_ISSUER, "CALL ISSUER");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("2")) {
                    return new RechargeAfinitasResponses(ResponseCode.CALL_ISSUER, "CALL ISSUER");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("3")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_TRADE, "INVALID TRADE");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("4")) {
                    return new RechargeAfinitasResponses(ResponseCode.RETAIN_CARD, "RETAIN CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("5")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_TRANSACTION_AFINITAS, "INVALID TRANSACTION");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("6")) {
                    return new RechargeAfinitasResponses(ResponseCode.RETRY_AFINITAS, "RETRY");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("12")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("13")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("14")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("30")) {
                    return new RechargeAfinitasResponses(ResponseCode.FORMAT_ERROR, "FORMAT ERROR");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("31")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("36")) {
                    return new RechargeAfinitasResponses(ResponseCode.RETAIN_CARD, "RETAIN CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("41")) {
                    return new RechargeAfinitasResponses(ResponseCode.RETAIN_CARD, "RETAIN CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("43")) {
                    return new RechargeAfinitasResponses(ResponseCode.RETAIN_CARD, "RETAIN CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("51")) {
                    return new RechargeAfinitasResponses(ResponseCode.INSUFFICIENT_FUNDS, "INSUFFICIENT FUNDS");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("54")) {
                    return new RechargeAfinitasResponses(ResponseCode.EXPIRED_CARD_AFINITAS, "EXPIRED CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("55")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_PIN, "INVALID PIN");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("56")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("57")) {
                    return new RechargeAfinitasResponses(ResponseCode.DEFERRED_PAYMENT_NOT_PERMITTED, "DEFERRED PAYMENT NOT PERMITTED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("61")) {
                    return new RechargeAfinitasResponses(ResponseCode.LIMIT_EXCEEDED, "LIMIT EXCEEDED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("62")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("65")) {
                    return new RechargeAfinitasResponses(ResponseCode.LIMIT_EXCEEDED, "LIMIT EXCEEDED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("68")) {
                    return new RechargeAfinitasResponses(ResponseCode.RETRY_AFINITAS, "RETRY");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("75")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_PIN, "INVALID PIN");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("82")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("83")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("87")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("89")) {
                    return new RechargeAfinitasResponses(ResponseCode.TYPE_OF_PLAN_TERM_INVALID, "TYPE OF PLAN / TERM INVALID");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("94")) {
                    return new RechargeAfinitasResponses(ResponseCode.DUPLICATED_TRANSACTION, "DUPLICATED TRANSACTION");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N0")) {
                    return new RechargeAfinitasResponses(ResponseCode.RETRY_AFINITAS, "RETRY");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N2")) {
                    return new RechargeAfinitasResponses(ResponseCode.EXCESSED_AUTHORIZATIONS, "RETRY");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N5")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N6")) {
                    return new RechargeAfinitasResponses(ResponseCode.CP_NOT_PERMITTED_BY_TH, "CP NOT PERMITTED BY TH");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N7")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N7")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("O4")) {
                    return new RechargeAfinitasResponses(ResponseCode.LIMIT_EXCEEDED, "LIMIT EXCEEDED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("O6")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("O8")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("P1")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("P9")) {
                    return new RechargeAfinitasResponses(ResponseCode.LIMIT_EXCEEDED, "LIMIT EXCEEDED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("Q1")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("T2")) {
                    return new RechargeAfinitasResponses(ResponseCode.TERMINAL_ERROR, "TERMINAL ERROR");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("T3")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("T5")) {
                    return new RechargeAfinitasResponses(ResponseCode.UNACTIVATED_CARD, "UNACTIVATED CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("T9")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_COIN, "INVALID COIN");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("1001")) {
                    return new RechargeAfinitasResponses(ResponseCode.CHIP_READING_ERROR, "CHIP READING ERROR");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("1002")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CHIP, "INVALID CHIP");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("1003")) {
                    return new RechargeAfinitasResponses(ResponseCode.CHIP_NOT_SUPPORTED, "CHIP NOT SUPPORTED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("11")) {
                    return new RechargeAfinitasResponses(ResponseCode.UNKNOWN, "UNKNOWN");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("DV_002")) {
                    return new RechargeAfinitasResponses(ResponseCode.THE_DEVICE_IS_NOT_ACTIVE, "THE DEVICE IS NOT ACTIVE");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("BR_002")) {
                    return new RechargeAfinitasResponses(ResponseCode.THE_BRANCH_IS_NOT_ACTIVE, "THE BRANCH IS NOT ACTIVE");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("BS_002")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRADE_IS_NOT_ACTIVE, "TRADE IS NOT ACTIVE");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("RQ_001")) {
                    return new RechargeAfinitasResponses(ResponseCode.THE_REQUEST_IS_EMPTY, "THE REQUEST IS EMPTY");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("RQ_002")) {
                    return new RechargeAfinitasResponses(ResponseCode.MISSING_PARAMETER_ON_REQUEST, "MISSING PARAMETER ON REQUEST");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("RQ_003")) {
                    return new RechargeAfinitasResponses(ResponseCode.RESOURCE_NOT_FOUND, "RESOURCE NOT FOUND");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("RQ_004")) {
                    return new RechargeAfinitasResponses(ResponseCode.ANSWER_EMPTY, "ANSWER EMPTY");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_001")) {
                    return new RechargeAfinitasResponses(ResponseCode.THE_TRANSACTION_EXCEEDS_THE_PERMITTED_AMOUNT, "THE TRANSACTION EXCEEDS THE PERMITTED AMOUNT");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_002")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_EXCEEDS_THE_ALLOWED_DAILY_AMOUNT, "TRANSACTION EXCEEDS THE ALLOWED DAILY AMOUNT");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_003")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_EXCEEDS_THE_MONTHLY_AMOUNT_ALLOWED, "TRANSACTION EXCEEDS THE MONTHLY AMOUNT ALLOWED");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_005")) {
                    return new RechargeAfinitasResponses(ResponseCode.NON_ACTIVE_PROMOTIONS, "NON ACTIVE PROMOTIONS");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_006")) {
                    return new RechargeAfinitasResponses(ResponseCode.PROMOTION_NOT_ACTIVE, "PROMOTION NOT ACTIVE");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_007")) {
                    return new RechargeAfinitasResponses(ResponseCode.THE_TRANSACTION_IS_NOT_WITHIN_THE_PERMITTED_SCHEDULE, "THE TRANSACTION IS NOT WITHIN THE PERMITTED SCHEDULE");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_008")) {
                    return new RechargeAfinitasResponses(ResponseCode.THE_TRANSACTION_DOES_NOT_EXIST, "THE TRANSACTION DOES NOT EXIST");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_009")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_WITH_NOT_APPROVED_SOURCE, "TRANSACTION WITH NOT APPROVED SOURCE");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_010")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_011")) {
                    return new RechargeAfinitasResponses(ResponseCode.INVALID_MEMBERSHIP, "INVALID MEMBERSHIP");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_013")) {
                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_CANCELED_PREVIOUSLY, "TRANSACTION CANCELED PREVIOUSLY");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_014")) {
                    return new RechargeAfinitasResponses(ResponseCode.PREVIOUSLY_REVERSED_TRANSACTION, "PREVIOUSLY REVERSED TRANSACTION");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_015")) {
                    return new RechargeAfinitasResponses(ResponseCode.EXCEED_PERMITTED_DAILY_TRANSACTIONS, "EXCEED PERMITTED DAILY TRANSACTIONS");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("CRP_002")) {
                    return new RechargeAfinitasResponses(ResponseCode.THE_CORPORATE_IS_NOT_ACTIVE, "THE CORPORATE IS NOT ACTIVE");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("NF_001")) {
                    return new RechargeAfinitasResponses(ResponseCode.ANSWER_NOT_FOUND, "ANSWER NOT FOUND");
                } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_024")) {
                    return new RechargeAfinitasResponses(ResponseCode.TIME_EXCEEDED_TO_PERFORM_CANCELLATION, "TIME EXCEEDED TO PERFORM CANCELLATION");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return new RechargeAfinitasResponses(ResponseCode.ERROR_INTERNO, "Error in process saving transaction");
            }
        RechargeAfinitasResponses rechargeAfinitasResponses = new RechargeAfinitasResponses(chargeResponse, ResponseCode.EXITO, "EXITO", products);
        rechargeAfinitasResponses.setProducts(products);
        return rechargeAfinitasResponses;
//        return new TransactionResponse(ResponseCode.EXITO, "EXITO", products);
    }

    public PaymentInfoListResponse getPaymentInfo(String userApi, String passwordApi, Long userId) {
        List<PaymentInfo> paymentInfos = null;
        try {
            if (validateUser(userApi, passwordApi)) {
                paymentInfos = entityManager.createNamedQuery("PaymentInfo.findByUserId", PaymentInfo.class).setParameter("userId", userId).getResultList();
                if (paymentInfos.size() <= 0) {
                    return new PaymentInfoListResponse(ResponseCode.NOT_ASSOCIATED_PAYMENT_INFO, "Not associated payment info");
                }
            } else {
                return new PaymentInfoListResponse(ResponseCode.ERROR_INTERNO, "Error loading Payment Info");
            }

        } catch (Exception e) {
            return new PaymentInfoListResponse(ResponseCode.ERROR_INTERNO, "Error loading Payment Info");
        }
        return new PaymentInfoListResponse(ResponseCode.EXITO, "", paymentInfos);
    }

    public CreditCardListResponse getCreditCardType(String userApi, String passwordApi) {
        List<CreditcardType> creditcardTypes = null;
        try {
            if (validateUser(userApi, passwordApi)) {
                creditcardTypes = entityManager.createNamedQuery("CreditcardType.findByEnabledTrue", CreditcardType.class).getResultList();
            } else {
                return new CreditCardListResponse(ResponseCode.ERROR_INTERNO, "Error loading Credit Card");
            }

        } catch (Exception e) {
            return new CreditCardListResponse(ResponseCode.ERROR_INTERNO, "Error loading Payment Info");
        }
        return new CreditCardListResponse(ResponseCode.EXITO, "", creditcardTypes);
    }

    public PaymentInfoResponse savePaymentInfo(String userApi, String passwordApi, Long userId, String estado, String ciudad, String zipCode, String addres1, Long paymentPatnerId, Long paymentTypeId, Long creditCardTypeId, String creditCardName, String creditCardNumber, String creditCardCVV, String creditCardDate) throws RemoteException, Exception {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;

        try {
            if (validateUser(userApi, passwordApi)) {
                responseUser = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
                userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
                Address address = saveAddress(userId, estado, ciudad, zipCode, addres1);
                PaymentInfo paymentInfo = new PaymentInfo();
                paymentInfo.setBillingAddressId(address);
                PaymentPatner paymentPatner = entityManager.find(PaymentPatner.class, paymentPatnerId);
                paymentInfo.setPaymentPatnerId(paymentPatner);
                PaymentType paymentType = entityManager.find(PaymentType.class, paymentTypeId);
                paymentInfo.setPaymentTypeId(paymentType);
                paymentInfo.setUserId(BigInteger.valueOf(userId));
                CreditcardType creditcardType = entityManager.find(CreditcardType.class, creditCardTypeId);
                paymentInfo.setCreditCardTypeId(creditcardType);
                paymentInfo.setCreditCardName(creditCardName);
                String example = creditCardNumber;
                byte[] bytes = example.getBytes();
                paymentInfo.setCreditCardNumber(bytes);
                paymentInfo.setCreditCardCVV(creditCardCVV);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                Date ccdate = format.parse(creditCardDate);
                paymentInfo.setCreditCardDate(ccdate);
                paymentInfo.setBeginningDate(new Timestamp(new Date().getTime()));
                paymentInfo.setEnabled(true);
                entityManager.persist(paymentInfo);

            } else {
                return new PaymentInfoResponse(ResponseCode.ERROR_INTERNO, "Error in process saving payment info");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return new PaymentInfoResponse(ResponseCode.EXITO);

    }

    private boolean validateUser(String user, String password) {
        UserWS usuarioWS = entityManager
                .createNamedQuery("UserWS.findByUserANDPassword",
                        UserWS.class).setMaxResults(1).getSingleResult();
        return (usuarioWS.getUsuario().equals(user) && usuarioWS
                .getPassword().equals(password));
    }

  public PaymentInfoResponse ChangeStatusPaymentInfo(String userApi, String passwordApi, Long userId, Long paymentInfoId, boolean  status) {
       PaymentInfo paymentInfo = null;
       
        try {
            if (validateUser(userApi, passwordApi)) {
                paymentInfo = entityManager.createNamedQuery("PaymentInfo.findByUserIdById", PaymentInfo.class).setParameter("userId", userId).setParameter("id", paymentInfoId).getSingleResult();
                paymentInfo.setEnabled(status);
                entityManager.merge(paymentInfo);
            } else {
                return new PaymentInfoResponse(ResponseCode.ERROR_INTERNO, "Error loading Payment Info");
            }

        } catch (Exception e) {
            return new PaymentInfoResponse(ResponseCode.ERROR_INTERNO, "Error loading Payment Info");
        }
        return new PaymentInfoResponse(ResponseCode.EXITO, "", paymentInfo);
    }
  
  public ProductListResponse getProductsRechargePaymentByUserId(Long userId) {
        List<Product> products = new ArrayList<Product>();
        List<Product> productFinals = new ArrayList<Product>();
        try {
            products = getProductsListByUserId(userId);
            for (Product p : products) {
                if (p.isIsPaymentInfo()) {
                    Float amount = 0F;
                    try {
                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
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
