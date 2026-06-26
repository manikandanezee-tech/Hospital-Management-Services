package com.ezeeinfo.hospitalmanagementservices.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class NamespaceDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String code;
	private String name;
	private String address;
	private int activeFlag;
	private UserDTO updatedBy;

}
