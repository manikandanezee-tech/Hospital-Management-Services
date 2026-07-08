package com.ezeeinfo.hospitalmanagementservices.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.PaymentModeEM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentDTO extends BaseDTO<PaymentDTO> {
	
	private BillingDTO billingDTO;
	private BigDecimal amount;
	private PaymentModeEM paymentMode;
	private LocalDateTime paymentDatetime;
	private String transactionRefNumber;
	

}
