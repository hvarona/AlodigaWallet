package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.topup.TopUpInfo;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TopUpInfoListResponse extends Response {

	public List<TopUpInfo> topUpInfos;
	
	public TopUpInfoListResponse() {
		super();
	}
	
	public TopUpInfoListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.topUpInfos = null;
	}
	
	public TopUpInfoListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.topUpInfos = null;
	}

	public TopUpInfoListResponse(ResponseCode code, String mensaje, List<TopUpInfo> topUpInfos) {
		super(new Date(), code.getCodigo(), mensaje);
		this.topUpInfos = topUpInfos;
	}
        
}
