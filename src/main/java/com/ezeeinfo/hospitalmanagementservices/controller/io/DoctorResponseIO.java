package com.ezeeinfo.hospitalmanagementservices.controller.io;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)

public class DoctorResponseIO extends BaseIO {
	private String namespaceCode;
	private String userCode;
	private String departmentCode;
	private String mobile;
	private String email;
	private String specialization;
	private BigDecimal consultationFee;
	
}
