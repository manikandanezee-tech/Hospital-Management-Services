package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.dao.MedicineDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.MedicineService;

@Service
public class MedicineServiceImpl implements MedicineService {

	@Autowired
	private MedicineDAO medicineDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(MedicineServiceImpl.class);

	@Override
	public MedicineDTO save(MedicineDTO medicineDTO, AuthDTO authDTO) throws SQLException {

		String role = authDTO.getUserDTO().getRole().getCode();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();

		medicineDTO.setUpdatedBy(authDTO.getUserDTO());

		if (role.equals("PHARMA") && namespaceCode.equals(medicineDTO.getNamespaceDTO().getCode())) {
			LOGGER.info("SAVE - Medicine validation success");
			MedicineDTO medicineDTO2 = medicineDAO.save(medicineDTO);
			LOGGER.info("SAVE - Medicine saved successfully");
			return medicineDTO2;
		}
		throw new ServiceException("Access Denied for Medicine update", HttpStatus.UNAUTHORIZED);
	}

	@Override
	public MedicineDTO getByCode(String code, AuthDTO authDTO) {
		LOGGER.info("GET-BY-CODE - fetching Medicine by code: {}", code);
		MedicineDTO medicineDTO = medicineDAO.getByCode(code);
		if (authDTO.getUserDTO().getNamespaceDTO().getCode().equals(medicineDTO.getNamespaceDTO().getCode())) {
			LOGGER.info("GET-BY-CODE - Medicine retrived successfully");
			return medicineDTO;
		}
		throw new ServiceException("Access Denied to get Medicine Details", HttpStatus.UNAUTHORIZED);
	}

	@Override
	public List<MedicineDTO> getAll(AuthDTO authDTO) {
		int namespaceId = authDTO.getUserDTO().getNamespaceDTO().getId();
		LOGGER.info("GET-ALL - retrived medicine list successfully");
		List<MedicineDTO> medicineDTOList = medicineDAO.getAll(namespaceId);
		return medicineDTOList;
	}

}
