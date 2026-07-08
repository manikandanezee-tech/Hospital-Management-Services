package com.ezeeinfo.hospitalmanagementservices.dto;

import lombok.Data;

@Data
public class BaseDTO<T>{


	private Integer id;
	private String code;
	private String name;
	private int activeFlag;
	private UserDTO updatedBy;
}
