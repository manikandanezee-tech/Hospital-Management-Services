package com.ezeeinfo.hospitalmanagementservices.controller.io;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class PaymentRequestIO {

	private String code;
	private BillingIO billingIO;
	private BigDecimal amount;
	private String paymentMode;
	private int activeFlag;
}
