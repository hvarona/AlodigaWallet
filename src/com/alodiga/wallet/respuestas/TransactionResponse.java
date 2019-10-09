package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.Transaction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ProductResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;
	public List<Product> products;
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

        public TransactionResponse(ResponseCode codigo,
			String mensajeRespuesta, ArrayList<Product> products) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
                this.products = products;
	}
}