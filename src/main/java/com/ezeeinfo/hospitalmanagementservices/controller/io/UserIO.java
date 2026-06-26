package com.ezeeinfo.hospitalmanagementservices.controller.io;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserIO extends BaseIO{

	private String userName;
	private String token;
	private String role;
	private NamespaceIO namespaceIO;
	private int activeFlag;
}
