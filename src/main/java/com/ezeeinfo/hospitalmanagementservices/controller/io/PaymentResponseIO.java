package com.ezeeinfo.hospitalmanagementservices.controller.io;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentResponseIO {
	private String code;
	private BillingIO billingIO;
	private BigDecimal amount;
	private String paymentMode;
	private LocalDateTime paymentDateTime;
	private String transactionRefNumber;
	private int activeFlag;

}
