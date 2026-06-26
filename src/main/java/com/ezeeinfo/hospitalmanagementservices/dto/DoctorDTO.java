package com.ezeeinfo.hospitalmanagementservices.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorDTO extends BaseDTO {

	private UserDTO userDTO;
	private DepartmentDTO departmentDTO;
	private NamespaceDTO namespaceDTO;
	private String mobile;
	private String email;
	private String specialization;
	private BigDecimal consultationFee;

}
