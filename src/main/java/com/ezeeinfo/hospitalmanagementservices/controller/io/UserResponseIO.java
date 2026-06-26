package com.ezeeinfo.hospitalmanagementservices.controller.io;


import lombok.Data;
@Data
public class UserResponseIO {

	private String code;
	private String userName;
	private String role;
	private NamespaceIO namespaceIO;
	private int activeFlag;
}
