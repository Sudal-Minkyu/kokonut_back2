package com.app.kokonut.auth.niceId;

import lombok.Data;

import java.util.Date;

@Data
public class CryptoToken {

	private String reqDtim;

	private String reqNo;
	
	private String tokenVersionId = "";
	
	private String tokenValue = "";
	
	private String siteCode;
	
	private Date issDate;
	
	private long period = 0;
	
	private static volatile CryptoToken cpt;

	public static CryptoToken getInstance(){
		if(cpt == null){
			synchronized(CryptoToken.class) {
			if(cpt == null)
				return cpt = new CryptoToken();
			}
		} else return cpt;
		return cpt;
	}

}
