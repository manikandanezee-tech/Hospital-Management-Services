package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.cache.NamespaceCache;
import com.ezeeinfo.hospitalmanagementservices.dao.DepartmentDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentDAO departmentDAO;
	@Autowired
	private NamespaceCache namespaceCache;
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);

	@Override
	public DepartmentDTO save(DepartmentDTO departmentDTO, HttpServletRequest request) throws SQLException {
		AuthDTO authDTO = (AuthDTO)request.getAttribute("authDTO");
		String role = authDTO.getUserDTO().getRole().getCode();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();


		departmentDTO.setUpdatedBy(authDTO.getUserDTO());
		
		if (role.equals("ADM") && namespaceCode.equals(departmentDTO.getNamespaceDTO().getCode())) {
			LOGGER.info("SAVE - successfully validation the department");
			DepartmentDTO department = departmentDAO.save(departmentDTO);
			LOGGER.info("SAVE - insert/updated successfully");
			return department;
		}
		throw new ServiceException("Access Denied to add Department", HttpStatus.UNAUTHORIZED);

	}

	@Override
	public DepartmentDTO getByCode(String code) {
		LOGGER.info("GET-BY-CODE - fetching the department by code : {}",code);
		DepartmentDTO departmentDTO = departmentDAO.getByCode(code);
		LOGGER.info("GET-BY-CODE - Retrive the department successfully ");
		return departmentDTO;
	}

	@Override
	public List<DepartmentDTO> getAll(HttpServletRequest request) {
		AuthDTO authDTO = (AuthDTO)request.getAttribute("authDTO");
		NamespaceDTO namespaceDTO = namespaceCache.getByCode(authDTO.getUserDTO().getNamespaceDTO().getCode());
		
		LOGGER.info("GET-ALL - fetching departmet list");
		List<DepartmentDTO> departmentDTOList = departmentDAO.getAll(namespaceDTO.getId());
		LOGGER.info("GET-ALL - retriving department list");
		return departmentDTOList;
	}

}
