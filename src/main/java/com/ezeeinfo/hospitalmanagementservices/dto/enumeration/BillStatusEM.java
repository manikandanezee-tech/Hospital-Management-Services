package com.ezeeinfo.hospitalmanagementservices.dto.enumeration;


import org.springframework.http.HttpStatus;

import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;

public enum BillStatusEM {

	PAIDED("PAIDED",1,"Paided"),
	PARTIALLY_PAID("PARTIALLY_PAID",2,"PartiallyPaided"),
	PENDING("PENDING",3,"Pending");
	private final String code;
	private final int value;
	private final String label;
	
	private BillStatusEM(String code, int value, String label) {
		this.code=code;
		this.value=value;
		this.label=label;
	}
	
	public String getCode() {
		return code;
	}
	public int getValue() {
		return value;
	}
	public String getLabel() {
		return label;
	}
	
	public static BillStatusEM getByValue(int value) {
		for(BillStatusEM billStatusEM:values()) {
			if(billStatusEM.getValue()==value) {
				return billStatusEM;
			}
		}
		throw new ServiceException("invalid Value", HttpStatus.BAD_REQUEST);
	}
	public static BillStatusEM getByCode(String code) {
		for(BillStatusEM billStatusEM:values()) {
			if(billStatusEM.getCode()==code) {
				return billStatusEM;
			}
		}
		throw new ServiceException("invalid code", HttpStatus.BAD_REQUEST);
	}
}
