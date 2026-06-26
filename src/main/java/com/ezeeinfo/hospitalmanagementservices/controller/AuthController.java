package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.hospitalmanagementservices.dto.LoginDTO;
import com.ezeeinfo.hospitalmanagementservices.service.AuthService;

@RestController
public class AuthController {

	@Autowired
	private AuthService loginService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@RequestMapping(value = "/auth/login", method = RequestMethod.POST)
	public String login(@RequestBody LoginDTO loginDTO) throws SQLException {
		LOGGER.info("LOGIN -  Request to Login");
		String token = loginService.login(loginDTO);

		LOGGER.info("LOGIN - Login success");
		return token;
	}
}
