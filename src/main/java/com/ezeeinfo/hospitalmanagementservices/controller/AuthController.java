package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.hospitalmanagementservices.dto.LoginDTO;
import com.ezeeinfo.hospitalmanagementservices.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody LoginDTO loginDTO) throws SQLException {
		LOGGER.info("LOGIN -  Request to Login");
		String token = authService.login(loginDTO);

		LOGGER.info("LOGIN - Login success");
		return token;
	}
	@RequestMapping(value = "/{authToken}/logout", method = RequestMethod.POST)
	public String logout(@PathVariable String authToken) {
		String message = authService.logout(authToken);
		return message;
	}
}
