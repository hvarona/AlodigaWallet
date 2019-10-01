package com.alodiga.wallet.enumeradas;

public enum WithdrawalType {
    MANUAL("1"),
    AUTOMATIC("2");
    
    private String codigo;
    
    WithdrawalType(String codigo) { 
       this.codigo = codigo;
    }
   
    public String getCodigo(){
        return codigo;
    }
    
}
