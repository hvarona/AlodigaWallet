package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.model.Bank;
import com.alodiga.wallet.model.Country;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BankListResponse extends Response {

	public List<Bank> bank;
	
	public BankListResponse() {
		super();
	}
	
	public BankListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.bank = null;
	}
	
	public BankListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.bank = null;
	}

	public BankListResponse(ResponseCode code, String mensaje, List<Bank> bank) {
		super(new Date(), code.getCodigo(), mensaje);
		this.bank = bank;
	}
        
}
