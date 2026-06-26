package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.UserIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.UserResponseIO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.UserRoleEM;

public class UserMapper {
public static UserDTO toDTO(UserIO userIO,NamespaceDTO namespaceDTO) {
	UserDTO userDTO = new UserDTO();
	
	userDTO.setUserName(userIO.getUserName());
	userDTO.setCode(userIO.getCode());
	userDTO.setRole(UserRoleEM.getUserRoleEMByCode(userIO.getRole()));
	userDTO.setToken(userIO.getToken());
	
	userDTO.setNamespaceDTO(namespaceDTO);
	userDTO.setActiveFlag(userIO.getActiveFlag());
	return userDTO;

}
public static UserResponseIO toIO(UserDTO userDTO) {
	UserResponseIO userResponseIO = new UserResponseIO();
	userResponseIO.setCode(userDTO.getCode());
	userResponseIO.setUserName(userDTO.getUserName());
	
	NamespaceIO namespaceIO = NamespaceMapper.toIO(userDTO.getNamespaceDTO());
	
	userResponseIO.setNamespaceIO(namespaceIO);
	userResponseIO.setRole(userDTO.getRole().getCode());
	userResponseIO.setActiveFlag(userDTO.getActiveFlag());
	return userResponseIO;
}
}
