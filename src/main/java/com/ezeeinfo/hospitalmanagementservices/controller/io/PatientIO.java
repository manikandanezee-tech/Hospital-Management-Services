package com.ezeeinfo.hospitalmanagementservices.controller.io;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientIO extends BaseIO {
	private NamespaceIO namespaceIO;
	private String mobile;
	private String gender;
	private String address;
}
