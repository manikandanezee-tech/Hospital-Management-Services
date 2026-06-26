package com.ezeeinfo.hospitalmanagementservices.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.BillStatusEM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BillingDTO extends BaseDTO {

	private NamespaceDTO namespaceDTO;
	private BigDecimal totalAmount;
	private AppointmentDTO appointmentDTO;
	private BigDecimal paidAmount;
	private BigDecimal balanceAmount;
	private BillStatusEM billStatus;
	private LocalDateTime billDateTime;
	
}
