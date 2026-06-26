package com.ezeeinfo.hospitalmanagementservices.controller.io;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AppointmentResponseIO {
 
	private String code;
	private NamespaceIO namespaceIO;
	private DoctorIO doctorIO;
	private PatientIO patientIO;
	private LocalDateTime appointmentDateTime;
	private int tokenNumber;
	private String status;
	private int activeFlag;
}
