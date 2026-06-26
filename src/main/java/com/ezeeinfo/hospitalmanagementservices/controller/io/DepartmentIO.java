package com.ezeeinfo.hospitalmanagementservices.controller.io;

import lombok.Data;

@Data
public class DepartmentIO {
 
	private String code;
	private String name;
	private NamespaceIO namespaceIO;
	private int activeFlag;
	
}
