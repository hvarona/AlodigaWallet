package com.alodiga.wallet.ws;

import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Logger;
import com.alodiga.wallet.bean.APIOperations;
import com.alodiga.wallet.respuestas.CountryListResponse;
import com.alodiga.wallet.respuestas.ProductListResponse;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ProductResponse;
import com.alodiga.wallet.respuestas.ResponseCode;
import com.alodiga.wallet.respuestas.UserHasProductResponse;
import java.sql.Timestamp;
import java.util.ArrayList;

@WebService
public class APIAlodigaWallet {

    private static final Logger logger = Logger
            .getLogger(APIAlodigaWallet.class);

    @EJB
    private APIOperations operations;

   //coment
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

    
}
