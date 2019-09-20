package com.alodiga.wallet.model;

public enum TransactionStatus {
    CREATED("CREATED"),
    IN_PROCESS("IN_PROCESS"),
    COMPLETE("COMPLETE");
    
    private String codigo;
    
    TransactionStatus(String codigo) { 
       this.codigo = codigo;
    }
   
    public String getCodigo(){
        return codigo;
    }
    
}
