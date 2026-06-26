package com.ezeeinfo.hospitalmanagementservices.dto.enumeration;

import org.springframework.http.HttpStatus;

import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;

public enum UserRoleEM {

	MASTERADMIN("MASTADM", 1, "MasterAdmin"), ADMIN("ADM", 2, "Admin"), DOCTOR("DOCT", 3, "Doctor"), RECEPTIONIST("RECEPNST", 4, "Receptionist"), PHARMACIST("PHARMA", 5, "Pharmacist"), CASHIER("CASHIER", 6, "Cashier");

	private final String code;
	private final int value;
	private final String label;

	private UserRoleEM(String code, int value, String label) {
		this.code = code;
		this.value = value;
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public String getCode() {
		return code;
	}

	public static UserRoleEM getUserRoleEM(int value) {
		for (UserRoleEM userRoleEM : values()) {
			if (userRoleEM.getValue() == value) {
				return userRoleEM;
			}

		}
		throw new ServiceException("invalid Role", HttpStatus.NOT_FOUND);
	}

	public static UserRoleEM getUserRoleEMByCode(String code) {
		for (UserRoleEM userRoleEM : values()) {
			if (userRoleEM.getCode().equalsIgnoreCase(code)) {
				return userRoleEM;
			}

		}
		throw new ServiceException("Invalid Role Code", HttpStatus.BAD_REQUEST);
	}
}
