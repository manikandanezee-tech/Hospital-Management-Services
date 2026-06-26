package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.ezeeinfo.hospitalmanagementservices.controller.io.UserResponseIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.UserIO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.UserMapper;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;
import com.ezeeinfo.hospitalmanagementservices.service.UserDTOService;


@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserDTOService userService;
	@Autowired
	private NamespaceService namespaceService;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(method = RequestMethod.POST)
	public UserResponseIO save(@RequestBody UserIO userIO, HttpServletRequest request) throws SQLException {

		NamespaceDTO namespaceDTO = namespaceService.getByCode(userIO.getNamespaceIO().getCode());

		UserDTO userDTO = UserMapper.toDTO(userIO, namespaceDTO);
		LOGGER.info("SAVE - Request to save the user");
		UserDTO userDTO2 = userService.save(userDTO, request);

		UserResponseIO userResponseIO = UserMapper.toIO(userDTO2);
		LOGGER.info("SAVE - User save successfully");
		return userResponseIO;

	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public UserResponseIO getByCode(@PathVariable String code) throws SQLException {
		LOGGER.info("GET-BY-CODE - Request to get user by code : {}",code);
		UserDTO userDTO = userService.getByCode(code);

		UserResponseIO userResponseIO = UserMapper.toIO(userDTO);
		LOGGER.info("GET-BY-CODE - User successfully received using code : {}",code);
		return userResponseIO;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<UserResponseIO> getAll(HttpServletRequest request) {
		LOGGER.info("GET-ALL - Request to get UserList");
		List<UserDTO> userDTOlList = userService.getAll(request);

		List<UserResponseIO> userResponseIOList = userDTOlList.stream().map(DTO -> {

			UserResponseIO userResponseIO = UserMapper.toIO(DTO);

			return userResponseIO;
		}).toList();
		LOGGER.info("GET-ALL - UserList Successfully received");
		return userResponseIOList;

	}
}