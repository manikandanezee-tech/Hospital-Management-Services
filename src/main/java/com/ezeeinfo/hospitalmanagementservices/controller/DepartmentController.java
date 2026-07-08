package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.hospitalmanagementservices.controller.io.DepartmentIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.DepartmentMapper;
import com.ezeeinfo.hospitalmanagementservices.service.DepartmentService;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;
import com.ezeeinfo.hospitalmanagementservices.service.UserService;

@RestController
@RequestMapping("/{authToken}/department")
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private NamespaceService namespaceService;
	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

	@RequestMapping(method = RequestMethod.POST)
	public DepartmentIO save(@PathVariable String authToken, @RequestBody DepartmentIO departmentIO) throws SQLException {

		AuthDTO authDTO = userService.getAuthDTO(authToken);
		NamespaceDTO namespaceDTO = namespaceService.getByCode(authDTO, departmentIO.getNamespaceIO().getCode());

		DepartmentDTO departmentDTO = DepartmentMapper.toDTO(departmentIO, namespaceDTO);
		LOGGER.info("SAVE - Request to save the Department");
		DepartmentDTO departmentDTO2 = departmentService.save(departmentDTO, authDTO);

		DepartmentIO departmentIO2 = DepartmentMapper.toIO(departmentDTO2);
		LOGGER.info("SAVE - Department save successfully");
		return departmentIO2;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public DepartmentIO getByCode(@PathVariable String authToken, @PathVariable String code) throws SQLException {
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		LOGGER.info("GET-BY-CODE - Request to get Department by code : {}", code);
		DepartmentDTO departmentDTO = departmentService.getByCode(code, authDTO);

		DepartmentIO departmentIO = DepartmentMapper.toIO(departmentDTO);
		LOGGER.info("GET-BY-CODE -  Department successfully received using code : {}", code);
		return departmentIO;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<DepartmentIO> getAll(@PathVariable String authToken) throws SQLException {
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		LOGGER.info("GET-ALL - Request to get Department List");
		List<DepartmentDTO> departmentDTOList = departmentService.getAll(authDTO);

		List<DepartmentIO> departmentIOList = departmentDTOList.stream().map(DTO -> {
			DepartmentIO departmentIO = DepartmentMapper.toIO(DTO);
			return departmentIO;
		}).toList();
		LOGGER.info("GET-ALL - Department List successfully received");
		return departmentIOList;
	}

}
