package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.cache.NamespaceCache;
import com.ezeeinfo.hospitalmanagementservices.dao.DepartmentDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.DoctorDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.UserDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DoctorDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {
	@Autowired
	private DoctorDAO doctorDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private NamespaceCache namespacecCache;
	@Autowired
	private DepartmentDAO departmentDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(DoctorServiceImpl.class);

	@Override
	public DoctorDTO save(DoctorDTO doctorDTO, AuthDTO authDTO) throws SQLException {
		String role = authDTO.getUserDTO().getRole().getCode();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();

		doctorDTO.setUpdatedBy(authDTO.getUserDTO());

		if (!doctorDTO.getMobile().matches("^[6-9][0-9]{9}$")) {
			throw new ServiceException("Invalide Mobile Number", HttpStatus.BAD_REQUEST);
		}
		if (!doctorDTO.getEmail().matches("^[a-zA-Z0-9_-]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$")) {
			throw new ServiceException("Invalid Email Format", HttpStatus.BAD_REQUEST);
		}
		System.out.println(doctorDTO.getUserDTO().getRole().getCode());
		if ((role.equals("ADM")) && (namespaceCode.equals(doctorDTO.getNamespaceDTO().getCode()) && (namespaceCode.equals(doctorDTO.getDepartmentDTO().getNamespaceDTO().getCode()) && (doctorDTO.getUserDTO().getRole().getCode().equals("DOCT"))))) {

			UserDTO userDTO = userDAO.getByCode(doctorDTO.getUserDTO().getCode());

			NamespaceDTO namespaceDTO = namespacecCache.getByCode(doctorDTO.getNamespaceDTO().getCode());

			DepartmentDTO departmentDTO = departmentDAO.getByCode(doctorDTO.getDepartmentDTO().getCode());

			int usersId = userDTO.getId();
			int namespaceId = namespaceDTO.getId();
			int departmentId = departmentDTO.getId();
			LOGGER.info("SAVE - Doctor validation success");
			DoctorDTO doctorDTO2 = doctorDAO.save(doctorDTO, usersId, namespaceId, departmentId);
			LOGGER.info("SAVE - doctor data saved successfully");
			return doctorDTO2;
		} 
		throw new ServiceException("Access Denied update doctor", HttpStatus.UNAUTHORIZED);
	}

	@Override
	public DoctorDTO getByCode(String code, AuthDTO authDTO) {
		LOGGER.info("GET-BY-CODE - fetching doctor by code: {}", code);
		DoctorDTO doctorDTO = doctorDAO.getByCode(code);
		if (authDTO.getUserDTO().getNamespaceDTO().getCode().equals(doctorDTO.getNamespaceDTO().getCode())) {
			LOGGER.info("GET-BY-CODE - doctor retrived successfully");
			return doctorDTO;
		}
		throw new ServiceException("Access Denied to get Doctor", HttpStatus.UNAUTHORIZED);

	}

	@Override
	public List<DoctorDTO> getAll(AuthDTO authDTO) throws SQLException {
		int namespaceId = authDTO.getUserDTO().getNamespaceDTO().getId();
		LOGGER.info("GET-ALL - fetching Doctor list");
		List<DoctorDTO> doctorDTOList = doctorDAO.getAll(namespaceId);
		LOGGER.info("GET-ALL - retrived Doctor list successfully");
		return doctorDTOList;
	}
}
