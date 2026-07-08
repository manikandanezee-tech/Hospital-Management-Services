package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;

public interface UserService {

	UserDTO save(UserDTO userDTO, AuthDTO authDTO) throws SQLException;

	UserDTO getByCode(String code, AuthDTO authDTO) throws SQLException;

	List<UserDTO> getAll(AuthDTO authDTO);
	
	AuthDTO getAuthDTO(String authToken) throws SQLException;

}
