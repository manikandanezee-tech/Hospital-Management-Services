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
import com.ezeeinfo.hospitalmanagementservices.dao.MedicineDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.MedicineService;

@Service
public class MedicineServiceImpl implements MedicineService {

	@Autowired
	private MedicineDAO medicineDAO;
	@Autowired
	private NamespaceCache namespaceCache;
	private static final Logger LOGGER = LoggerFactory.getLogger(MedicineServiceImpl.class);

	@Override
	public MedicineDTO save(MedicineDTO medicineDTO, HttpServletRequest request) throws SQLException {
		AuthDTO authDTO = (AuthDTO) request.getAttribute("authDTO");

		String role = authDTO.getUserDTO().getRole().getCode();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();

		medicineDTO.setUpdatedBy(authDTO.getUserDTO());
		
		if (role.equals("PHARMA") && namespaceCode.equals(medicineDTO.getNamespaceDTO().getCode())) {
			LOGGER.info("SAVE - Medicine validation success");
			MedicineDTO medicineDTO2 = medicineDAO.save(medicineDTO);
			LOGGER.info("SAVE - Medicine insert/update successfully");
			return medicineDTO2;
		}
		throw new ServiceException("Access Denied for Medicine update", HttpStatus.UNAUTHORIZED);
	}

	@Override
	public MedicineDTO getByCode(String code) {
		LOGGER.info("GET-BY-CODE - fetching Medicine by code: {}",code);
		MedicineDTO medicineDTO = medicineDAO.getByCode(code);
		LOGGER.info("GET-BY-CODE - Medicine retrived successfully");
		return medicineDTO;
	}

	@Override
	public List<MedicineDTO> getAll(HttpServletRequest request) {
		AuthDTO authDTO = (AuthDTO) request.getAttribute("authDTO");
		NamespaceDTO namespaceDTO = namespaceCache.getByCode(authDTO.getUserDTO().getNamespaceDTO().getCode());
		LOGGER.info("GET-ALL - retrived medicine list successfully");
		return medicineDAO.getAll(namespaceDTO.getId());
	}

}
