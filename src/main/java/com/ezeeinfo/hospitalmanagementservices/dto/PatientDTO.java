package com.ezeeinfo.hospitalmanagementservices.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientDTO extends BaseDTO {

	private NamespaceDTO namespaceDTO;
	private String mobile;
	private String gender;
	private String address;
	private int activeFlag;

}
