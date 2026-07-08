package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;

import com.ezeeinfo.hospitalmanagementservices.dto.LoginDTO;

public interface AuthService {

	String login(LoginDTO loginDTO) throws SQLException;

	String logout(String authToken);
}
