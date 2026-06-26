package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.NamespaceMapper;

@RestController
@RequestMapping("/namespace")
public class NamespaceController {
	@Autowired
	private NamespaceService namespaceService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceController.class);

	@RequestMapping(method = RequestMethod.POST)
	public NamespaceIO save(@RequestBody NamespaceIO namespaceIO,HttpServletRequest request) throws SQLException {
		NamespaceDTO namespaceDTO = NamespaceMapper.toDTO(namespaceIO);
		LOGGER.info("SAVE - Request to save the namespace");
		NamespaceDTO namespaceDTO2 = namespaceService.save(namespaceDTO, request);

		NamespaceIO namespaceIO2 = NamespaceMapper.toIO(namespaceDTO2);
		LOGGER.info("SAVE - Namespace saved successfully");
		return namespaceIO2;

	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public NamespaceIO getByCode(@PathVariable String code) throws SQLException {
		LOGGER.info("GET-BY-CODE - Request to get namespace by code : {}",code);
		NamespaceDTO namespaceDTO = namespaceService.getByCode(code);

		NamespaceIO namespaceIO = NamespaceMapper.toIO(namespaceDTO);
		LOGGER.info("GET-BY-CODE - Namespace successfully received using code : {}",code);
		return namespaceIO;

	}

	@RequestMapping(method = RequestMethod.GET)
	public List<NamespaceIO> getAll() {
		LOGGER.info("GET-ALL - Request to get namespace list");
		List<NamespaceDTO> namespaceDTOList = namespaceService.getAll();
		List<NamespaceIO> namespaceIOList = namespaceDTOList.stream().map(DTO -> {
			NamespaceIO namespaceIO = NamespaceMapper.toIO(DTO);
			return namespaceIO;
		}).toList();
		LOGGER.info("GET-ALL - Namespace list received");
		return namespaceIOList;

	}

}