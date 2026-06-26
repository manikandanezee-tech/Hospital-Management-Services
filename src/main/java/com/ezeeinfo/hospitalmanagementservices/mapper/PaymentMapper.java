package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.BillingIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.PaymentRequestIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.PaymentResponseIO;
import com.ezeeinfo.hospitalmanagementservices.dto.BillingDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PaymentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.PaymentModeEM;

public class PaymentMapper {

	public static PaymentDTO toDTO(PaymentRequestIO paymentRequestIO, BillingDTO billingDTO) {
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setCode(paymentRequestIO.getCode());
		paymentDTO.setBillingDTO(billingDTO);
		paymentDTO.setAmount(paymentRequestIO.getAmount());
		paymentDTO.setPaymentMode(PaymentModeEM.getByCode(paymentRequestIO.getPaymentMode()));
		paymentDTO.setActiveFlag(paymentRequestIO.getActiveFlag());
		return paymentDTO;
	}
	
	public static PaymentResponseIO toIO(PaymentDTO paymentDTO) {
		
		BillingIO billingIO = BillingMapper.toIO(paymentDTO.getBillingDTO());
		
		PaymentResponseIO paymentResponseIO = new PaymentResponseIO();
		paymentResponseIO.setCode(paymentDTO.getCode());
		paymentResponseIO.setBillingIO(billingIO);
		paymentResponseIO.setAmount(paymentDTO.getAmount());
		paymentResponseIO.setPaymentDateTime(paymentDTO.getPaymentDatetime());
		paymentResponseIO.setPaymentMode(paymentDTO.getPaymentMode().getCode());
		paymentResponseIO.setTransactionRefNumber(paymentDTO.getTransactionRefNumber());
		paymentResponseIO.setActiveFlag(paymentDTO.getActiveFlag());
		return paymentResponseIO;
	}
}
