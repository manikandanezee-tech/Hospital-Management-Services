package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;

public interface UserDTOService {

	UserDTO save(UserDTO userDTO, HttpServletRequest request) throws SQLException;

	UserDTO getByCode(String code) throws SQLException;

	List<UserDTO> getAll(HttpServletRequest request);

}
