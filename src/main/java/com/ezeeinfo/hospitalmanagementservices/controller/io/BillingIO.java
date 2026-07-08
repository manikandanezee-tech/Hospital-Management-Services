package com.ezeeinfo.hospitalmanagementservices.controller.io;

import java.math.BigDecimal;


import lombok.Data;

@Data
public class BillingIO {

	private String code;
	private NamespaceIO namespaceIO;
	private AppointmentResponseIO appointmentResponseIO;
	private BigDecimal totalAmount;
	private BigDecimal paidAmount;
	private BigDecimal balanceAmount;
	private String billStatus;
	private int activeFlag;
	
}
