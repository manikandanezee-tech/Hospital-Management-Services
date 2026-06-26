package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.hospitalmanagementservices.controller.io.DepartmentIO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.DepartmentMapper;
import com.ezeeinfo.hospitalmanagementservices.service.DepartmentService;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;

@RestController
@RequestMapping("/department")
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private NamespaceService namespaceService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

	@RequestMapping(method = RequestMethod.POST)
	public DepartmentIO save(@RequestBody DepartmentIO departmentIO, HttpServletRequest request) throws SQLException {
		
		
		NamespaceDTO namespaceDTO = namespaceService.getByCode(departmentIO.getNamespaceIO().getCode());
		
		DepartmentDTO departmentDTO = DepartmentMapper.toDTO(departmentIO, namespaceDTO);
		LOGGER.info("SAVE - Request to save the Department");
		DepartmentDTO departmentDTO2 = departmentService.save(departmentDTO, request);

		DepartmentIO departmentIO2 = DepartmentMapper.toIO(departmentDTO2);
		LOGGER.info("SAVE - Department save successfully");
		return departmentIO2;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public DepartmentIO getByCode(@PathVariable String code) {
		LOGGER.info("GET-BY-CODE - Request to get Department by code : {}",code);
		DepartmentDTO departmentDTO = departmentService.getByCode(code);

		DepartmentIO departmentIO = DepartmentMapper.toIO(departmentDTO);
		LOGGER.info("GET-BY-CODE -  Department successfully received using code : {}",code);
		return departmentIO;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<DepartmentIO> getAll(HttpServletRequest request) {
		LOGGER.info("GET-ALL - Request to get Department List");
		List<DepartmentDTO> departmentDTOList = departmentService.getAll(request);
		
		List<DepartmentIO> departmentIOList = departmentDTOList.stream().map(DTO -> {
			DepartmentIO departmentIO = DepartmentMapper.toIO(DTO);
			return departmentIO;
		}).toList();
		LOGGER.info("GET-ALL - Department List successfully received");
		return departmentIOList;
	}

}
