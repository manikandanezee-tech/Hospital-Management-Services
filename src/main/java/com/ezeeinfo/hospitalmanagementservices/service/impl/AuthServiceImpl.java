package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.dao.UserDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.LoginDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.AuthService;
import com.ezeeinfo.hospitalmanagementservices.util.JwtUtill;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Override
	public String login(LoginDTO loginDTO) throws SQLException {
		LOGGER.info("LOGIN -  login request forward to dao");
		UserDTO userDTO = userDAO.login(loginDTO.getUserName());

		if (userDTO.getUserName().equals(loginDTO.getUserName())) {
			if (passwordEncoder.matches(loginDTO.getPassword(), userDTO.getToken())) {
				LOGGER.info("LOGIN -  successfully validate the login details");
				return JwtUtill.generateToken(userDTO);
			}

		}

		throw new ServiceException("invalid credential", HttpStatus.BAD_REQUEST);
	}
}
