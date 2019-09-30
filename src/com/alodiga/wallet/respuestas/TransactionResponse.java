package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.model.Transaction;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ProductResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public Transaction response;

	public TransactionResponse() {
		super();
	}

	public TransactionResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
		this.response = null;
	}

	public TransactionResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = null;
	}

	public TransactionResponse(ResponseCode codigo,
			String mensajeRespuesta, Transaction transactionId) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = transactionId;
	}

}