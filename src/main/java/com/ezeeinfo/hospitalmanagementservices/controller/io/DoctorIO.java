package com.ezeeinfo.hospitalmanagementservices.controller.io;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorIO extends BaseIO{

	private NamespaceIO namespaceIO;
	private UserResponseIO userResponseIO;
	private DepartmentIO departmentIO;
	private String mobile;
	private String email;
	private String specialization;
	private BigDecimal consultationFee;
	private int activeFlag;

	
}
