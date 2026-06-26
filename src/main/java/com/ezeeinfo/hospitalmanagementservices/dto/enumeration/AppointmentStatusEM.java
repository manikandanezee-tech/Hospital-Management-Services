package com.ezeeinfo.hospitalmanagementservices.dto.enumeration;

import org.springframework.http.HttpStatus;

import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;

public enum AppointmentStatusEM {

	BOOKED("BOOK",1, "Booked"), COMPLETED("COMPL",2, "Completed");

	private final int value;
	private final String label;
	private final String code;

	private AppointmentStatusEM(String code,int value, String label) {
		this.code=code;
		this.value = value;
		this.label = label;
		
	}

	public int getValue() {
		return value;
	}

	public String getLebel() {
		return label;
	}
	public String getCode() {
		return code;
	}
	
	public static AppointmentStatusEM getById(int value) {
		for(AppointmentStatusEM appointmentStatusEM : values()) {
			if(appointmentStatusEM.getValue()==value) {
				return appointmentStatusEM;
			}
		}
		throw new ServiceException("Invalid status value",HttpStatus.NOT_FOUND);
	}
	public static AppointmentStatusEM getBycode(String code) {
		for(AppointmentStatusEM appointmentStatusEM : values()) {
			if(appointmentStatusEM.getCode()==code) {
				return appointmentStatusEM;
			}
		}
		throw new ServiceException("Status Not Found",HttpStatus.NOT_FOUND);
	}

}
