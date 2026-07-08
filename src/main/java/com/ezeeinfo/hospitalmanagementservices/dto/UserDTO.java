package com.ezeeinfo.hospitalmanagementservices.dto;

import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.UserRoleEM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO<UserDTO> {
	private String userName;
	private String token;
	private UserRoleEM role;
	private NamespaceDTO namespaceDTO;

}
