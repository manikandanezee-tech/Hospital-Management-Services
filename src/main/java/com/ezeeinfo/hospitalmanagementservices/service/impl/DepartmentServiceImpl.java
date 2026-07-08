package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.dao.DepartmentDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentDAO departmentDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);

	@Override
	public DepartmentDTO save(DepartmentDTO departmentDTO, AuthDTO authDTO) throws SQLException {
		String role = authDTO.getUserDTO().getRole().getCode();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();

		departmentDTO.setUpdatedBy(authDTO.getUserDTO());

		if (role.equals("ADM") && namespaceCode.equals(departmentDTO.getNamespaceDTO().getCode())) {
			LOGGER.info("SAVE - successfully validation the department");
			DepartmentDTO department = departmentDAO.save(departmentDTO);
			LOGGER.info("SAVE - Department saved successfully");
			return department;
		}
		throw new ServiceException("Access Denied to add Department", HttpStatus.UNAUTHORIZED);

	}

	@Override
	public DepartmentDTO getByCode(String code, AuthDTO authDTO) {
		LOGGER.info("GET-BY-CODE - fetching the department by code : {}", code);
		DepartmentDTO departmentDTO = departmentDAO.getByCode(code);
		if (authDTO.getUserDTO().getNamespaceDTO().getCode().equals(departmentDTO.getNamespaceDTO().getCode())) {
			LOGGER.info("GET-BY-CODE - Retrive the department successfully ");
			return departmentDTO;
		}
		throw new ServiceException("Access Denied to get Department", HttpStatus.UNAUTHORIZED);
	}

	@Override
	public List<DepartmentDTO> getAll(AuthDTO authDTO) {
		int namespaceId = authDTO.getUserDTO().getNamespaceDTO().getId();

		LOGGER.info("GET-ALL - fetching departmet list");
		List<DepartmentDTO> departmentDTOList = departmentDAO.getAll(namespaceId);
		LOGGER.info("GET-ALL - retriving department list");
		return departmentDTOList;
	}

}
