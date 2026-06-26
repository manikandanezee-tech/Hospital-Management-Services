package com.ezeeinfo.hospitalmanagementservices.controller.io;

import lombok.Data;

@Data
public class PrescriptionItemIO {

	private String code;
	private AppointmentResponseIO appointmentResponseIO;
	private MedicineIO medicineIO;
	private int quantity;
	private String notes;
	private int activeFlag;
}
