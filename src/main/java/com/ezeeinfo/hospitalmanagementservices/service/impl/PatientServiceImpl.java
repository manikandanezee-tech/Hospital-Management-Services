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
import com.ezeeinfo.hospitalmanagementservices.dao.PatientDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PatientDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

	@Autowired
	private PatientDAO patientDAO;
	@Autowired
	private NamespaceCache namespaceCache;
	private static final Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);

	@Override
	public PatientDTO save(PatientDTO patientDTO, HttpServletRequest request) throws SQLException {
		AuthDTO authDTO =(AuthDTO) request.getAttribute("authDTO");
		String role = authDTO.getUserDTO().getRole().getCode();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();

		

		patientDTO.setUpdatedBy(authDTO.getUserDTO());

		if (!patientDTO.getMobile().matches("^[6-9][0-9]{9}$")) {
			throw new ServiceException("invalide Mobile Number", HttpStatus.BAD_REQUEST);
		}
		if (role.equals("RECEPNST") && namespaceCode.equals(patientDTO.getNamespaceDTO().getCode())) {
			LOGGER.info(" SAVE - Successfully validating the Patient");
			PatientDTO patientDTO2 = patientDAO.save(patientDTO);
			LOGGER.info(" SAVE - Successfully insert/update the patient");
			return patientDTO2;
		}
		throw new ServiceException("Access Denied", HttpStatus.CONFLICT);
	}

	@Override
	public PatientDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Fetching the Patient details by code : {}",code);
		PatientDTO patientDTO = patientDAO.getByCode(code);
		LOGGER.info(" GET-BY-CODE - Successfully retrive the patient details by code : {}",code);
		return patientDTO;
	}

	@Override
	public List<PatientDTO> getAll(HttpServletRequest request) {
		AuthDTO authDTO =(AuthDTO) request.getAttribute("authDTO");
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();
		NamespaceDTO namespaceDTO = namespaceCache.getByCode(namespaceCode);
		LOGGER.info("GET-ALL - fetching patient list");
		List<PatientDTO> patientDTOList = patientDAO.getAll(namespaceDTO.getId());
		LOGGER.info(" GET-ALL - Retrived patient list");
		return patientDTOList;
	}
}
