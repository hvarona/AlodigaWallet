package com.alodiga.wallet.ws;

import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Logger;
import com.alodiga.wallet.bean.APIOperations;
import com.alodiga.wallet.respuestas.BalanceHistoryResponse;
import com.alodiga.wallet.respuestas.BankListResponse;
import com.alodiga.wallet.respuestas.CountryListResponse;
import com.alodiga.wallet.respuestas.ProductListResponse;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ProductResponse;
import com.alodiga.wallet.respuestas.ResponseCode;
import com.alodiga.wallet.respuestas.TopUpInfoListResponse;
import com.alodiga.wallet.respuestas.UserHasProductResponse;
import com.alodiga.wallet.respuestas.TransactionListResponse;
import com.alodiga.wallet.respuestas.TransactionResponse;
import java.sql.Timestamp;
import java.util.ArrayList;

@WebService
public class APIAlodigaWallet {

    private static final Logger logger = Logger
            .getLogger(APIAlodigaWallet.class);

    @EJB
    private APIOperations operations;

   //coment 6
   @WebMethod
    public ProductResponse saveProduct(
        @WebParam(name = "enterprise") String enterpriseId,
        @WebParam(name = "category") String categoryId,
        @WebParam(name = "productIntegrationTypeId") String productIntegrationTypeId,
        @WebParam(name = "name") String name,
        @WebParam(name = "taxInclude") boolean taxInclude,
        @WebParam(name = "status") boolean status,
        @WebParam(name = "referenceCode") String referenceCode,
        @WebParam(name = "rateUrl") String rateUrl,
        @WebParam(name = "accesNumberUrl") String accesNumberUrl,
        @WebParam(name = "isFree") boolean isFree,
        @WebParam(name = "isAlocashproduct") boolean isAlocashproduct){
       return operations.saveProduct(Long.valueOf(enterpriseId), Long.valueOf(categoryId), Long.valueOf(productIntegrationTypeId), name, taxInclude, status, referenceCode, rateUrl, accesNumberUrl, isFree, isAlocashproduct);         
    }
    
    //coment21
    //cambio267810
    @WebMethod
    public UserHasProductResponse saveUserHasProduct(
        @WebParam(name = "userId") String userId,
        @WebParam(name = "productId") String producId){
       return operations.saveUserHasProduct(Long.valueOf(userId), Long.valueOf(producId));         
    }
    
    @WebMethod
    public UserHasProductResponse saveUserHasProductDefault(
        @WebParam(name = "userId") String userId){
       return operations.saveUserHasProductDefault(Long.valueOf(userId));         
    }
    
    @WebMethod
    public ProductListResponse getProductsByUserId(
        @WebParam(name = "userId") String userId){
       return operations.getProductsByUserId(Long.valueOf(userId));         
    }
    
    @WebMethod
    public CountryListResponse getCountries(){
        return operations.getCountries();        
    }
    
    @WebMethod
    public BankListResponse getBankApp(){
        return operations.getBankApp();        
    }
    
    @WebMethod
    public BankListResponse getBankByCountryApp(
        @WebParam(name = "countryId") String countryId){
       return operations.getBankByCountryApp(Long.valueOf(countryId));         
    }
    
    @WebMethod
    public TransactionResponse savePaymentShop(
        @WebParam(name = "cryptogramaShop") String cryptogramShop,
        @WebParam(name = "emailUser") String emailUser,
        @WebParam(name = "productId") Long productId,
        @WebParam(name = "amountPayment") Float amountPayment,
        @WebParam(name = "conceptTransaction") String conceptTransaction ,
        @WebParam(name = "cryptogramaUser") String cryptogramUser,
        @WebParam(name = "idUserDestination") Long idUserDestination) {
        
        return operations.savePaymentShop(cryptogramShop, emailUser, productId, amountPayment, 
                                          conceptTransaction, cryptogramUser, idUserDestination);        
    }

    @WebMethod
    public TransactionResponse SaveTransferBetweenAccount (
        @WebParam(name = "cryptogramUserSource") String cryptogramUserSource, 
        @WebParam(name = "emailUser") String emailUser, 
        @WebParam(name = "productId") Long productId, 
        @WebParam(name = "amountTransfer") Float amountTransfer,
        @WebParam(name = "conceptTransaction") String conceptTransaction, 
        @WebParam(name = "cryptogramUserDestination") String cryptogramUserDestination, 
        @WebParam(name = "idUserDestination") Long idUserDestination) {
        
        return operations.SaveTransferBetweenAccount(cryptogramUserSource, emailUser, productId, amountTransfer,
                                                     conceptTransaction, cryptogramUserSource, idUserDestination) ;
    }
    
      @WebMethod
    public TransactionResponse ExchangeProduct ( 
        @WebParam(name = "emailUser") String emailUser, 
        @WebParam(name = "productSourceId") Long productSourceId, 
        @WebParam(name = "productDestinationId") Long productDestinationId,
        @WebParam(name = "amountExchange") Float amountExchange,
        @WebParam(name = "conceptTransaction") String conceptTransaction) {
        return operations.ExchangeProduct(emailUser, productSourceId, productDestinationId, amountExchange, conceptTransaction);
    }

    @WebMethod
    public TopUpInfoListResponse topUpList(
        @WebParam(name = "receiverNumber") String receiverNumber,
        @WebParam(name = "phoneNumber") String phoneNumber){
    return operations.getTopUpInfo(receiverNumber, phoneNumber);         
    }
    
    
       @WebMethod
    public TransactionListResponse getTransactionsByUserIdApp(
        @WebParam(name = "userId") String userId, 
        @WebParam(name = "maxResult") String maxResult)
    
    {
       return operations.getTransactionsByUserIdApp(Long.valueOf(userId), Integer.valueOf(maxResult));
    }
    
     public TransactionResponse ManualWithdrawals ( 
        @WebParam(name = "bankId") Long bankId,
        @WebParam(name = "emailUser") String emailUser,
        @WebParam(name = "accountBank") String accountBank,
        @WebParam(name = "amountWithdrawal") Float amountWithdrawal,
        @WebParam(name = "productId") Long productId, 
        @WebParam(name = "conceptTransaction") String conceptTransaction) {
        return operations.ManualWithdrawals(bankId, emailUser, accountBank, amountWithdrawal, productId, conceptTransaction);
    }
    
    
    @WebMethod
    public CountryListResponse getCountriesHasBank(
         @WebParam(name = "userId") Long userId){
        return operations.getCountriesHasBank(userId);        
    }
    
        //no esta 
    @WebMethod
    public ProductListResponse getProductsByBankId(
        @WebParam(name = "bankId") String bankId,
        @WebParam(name = "userId") String userId){
       return operations.getProductsByBankId(Long.valueOf(bankId),Long.valueOf(userId));         
    }
    
    
     //Desarrollado por Kerwin 2102019
    @WebMethod
    public BalanceHistoryResponse getBalanceHistoryByProductAndUser(
        @WebParam(name = "userId") Long userId, 
        @WebParam(name = "productId") Long productId)
    {
       return operations.getBalanceHistoryByUserAndProduct(userId, productId);
    }

    
}
