package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;
import com.ezeeinfo.hospitalmanagementservices.service.UserService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.NamespaceMapper;

@RestController
@RequestMapping("{authToken}/namespace")
public class NamespaceController {
	@Autowired
	private NamespaceService namespaceService;
	@Autowired
	private UserService userService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceController.class);

	@RequestMapping(method = RequestMethod.POST)
	public NamespaceIO save( @PathVariable String authToken, @RequestBody NamespaceIO namespaceIO) throws SQLException {
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		NamespaceDTO namespaceDTO = NamespaceMapper.toDTO(namespaceIO);
		LOGGER.info("SAVE - Request to save the namespace");
		NamespaceDTO namespaceDTO2 = namespaceService.save(namespaceDTO, authDTO);

		NamespaceIO namespaceIO2 = NamespaceMapper.toIO(namespaceDTO2);
		LOGGER.info("SAVE - Namespace saved successfully");
		return namespaceIO2;

	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public NamespaceIO getByCode( @PathVariable String authToken, @PathVariable String code) throws SQLException {
		
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		LOGGER.info("GET-BY-CODE - Request to get namespace by code : {}",code);
		NamespaceDTO namespaceDTO = namespaceService.getByCode(authDTO,code);

		NamespaceIO namespaceIO = NamespaceMapper.toIO(namespaceDTO);
		LOGGER.info("GET-BY-CODE - Namespace successfully received using code : {}",code);
		return namespaceIO;

	}

	@RequestMapping(method = RequestMethod.GET)
	public List<NamespaceIO> getAll( @PathVariable String authToken) throws SQLException {
		
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		LOGGER.info("GET-ALL - Request to get namespace list");
		List<NamespaceDTO> namespaceDTOList = namespaceService.getAll(authDTO);
		List<NamespaceIO> namespaceIOList = namespaceDTOList.stream().map(DTO -> {
			NamespaceIO namespaceIO = NamespaceMapper.toIO(DTO);
			return namespaceIO;
		}).toList();
		LOGGER.info("GET-ALL - Namespace list received");
		return namespaceIOList;

	}

}