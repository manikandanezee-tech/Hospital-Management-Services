package com.ezeeinfo.hospitalmanagementservices.dto.enumeration;

import org.springframework.http.HttpStatus;

import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;

public enum PaymentModeEM {

	CASH("CASH", 1), CARD("CARD", 2), UPI("UPI", 3), NET_BANKING("NET_BANKING", 4);

	private final String code;
	private final int value;

	private PaymentModeEM(String code, int value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public int getValue() {
		return value;
	}

	public static PaymentModeEM getByValue(int value) {
		for (PaymentModeEM paymentModeEM : values()) {
			if (paymentModeEM.getValue() == value) {
				return paymentModeEM;
			}
		}
		throw new ServiceException("invalid Value", HttpStatus.NOT_FOUND);
	}

	public static PaymentModeEM getByCode(String code) {
		for (PaymentModeEM paymentModeEM : values()) {
			if (paymentModeEM.getCode() .equals(code)) {
				return paymentModeEM;
			}
		}
		throw new ServiceException("invalid Code", HttpStatus.NOT_FOUND);
	}
}
