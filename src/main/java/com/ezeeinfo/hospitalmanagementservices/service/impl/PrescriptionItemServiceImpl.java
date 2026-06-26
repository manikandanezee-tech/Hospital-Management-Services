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
import com.ezeeinfo.hospitalmanagementservices.dao.ConsultationDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.MedicineDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.PrescriptionItemDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.UserDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PrescriptionItemDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.PrescriptionItemService;

@Service
public class PrescriptionItemServiceImpl implements PrescriptionItemService {
	@Autowired
	private PrescriptionItemDAO prescriptionItemDAO;
	@Autowired
	private NamespaceCache namespaceCache;
	@Autowired
	private MedicineDAO medicineDAO;
	@Autowired
	private ConsultationDAO consultationDAO;
	@Autowired
	private UserDAO userDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrescriptionItemServiceImpl.class);

	@Override
	public PrescriptionItemDTO save(PrescriptionItemDTO prescriptionItemDTO, HttpServletRequest request) throws SQLException {
		AuthDTO authDTO = (AuthDTO) request.getAttribute("authDTO");
		
		int userId = authDTO.getUserDTO().getId();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();
		
		
		prescriptionItemDTO.setUpdatedBy(authDTO.getUserDTO());

		ConsultationDTO consultationDTO = consultationDAO.isAlreayConsulted(prescriptionItemDTO.getAppointmentDTO().getId());
		if (consultationDTO == null) {
			throw new ServiceException("Please complete the consultation before prescribing medicines...", HttpStatus.BAD_REQUEST);
		}
		// check prescribe doctor and appointment doctor are same
		if (prescriptionItemDTO.getAppointmentDTO().getDoctorDTO().getUserDTO().getId() == userId 
				&& prescriptionItemDTO.getMedicineDTO().getNamespaceDTO().getCode().equals(namespaceCode)) {
			MedicineDTO medicineDTO = medicineDAO.getByCode(prescriptionItemDTO.getMedicineDTO().getCode());

			if (medicineDTO.getCurrentStock() < prescriptionItemDTO.getQuantity()) {

				throw new ServiceException("Medicine Stock OutofRange", HttpStatus.CONFLICT);
			}
			LOGGER.info(" SAVE - Successfully validating the prescription");
			PrescriptionItemDTO prescriptionItemDTO2 = prescriptionItemDAO.save(prescriptionItemDTO);

			medicineDTO.setCurrentStock(medicineDTO.getCurrentStock() - prescriptionItemDTO.getQuantity());
			medicineDTO.setUpdatedBy(userDAO.getById(userId));
			medicineDAO.save(medicineDTO);
			LOGGER.info(" SAVE - Prescription successfully inserted/updated ");
			return prescriptionItemDTO2;
		}
		throw new ServiceException("Access Denied to prescribe medicines", HttpStatus.FORBIDDEN);

	}

	@Override
	public PrescriptionItemDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Fetching the prescription details by code : {}",code);
		PrescriptionItemDTO prescriptionItemDTO = prescriptionItemDAO.getByCode(code);
		LOGGER.info(" GET-BY-CODE - successfully retrived prescription details by code : {}",code);
		return prescriptionItemDTO;
	}

	@Override
	public List<PrescriptionItemDTO> getAll(HttpServletRequest request) {
		AuthDTO authDTO = (AuthDTO) request.getAttribute("authDTO");
		NamespaceDTO namespaceDTO = namespaceCache.getByCode(authDTO.getUserDTO().getNamespaceDTO().getCode());
		if (request.getAttribute("role").equals("DOCT")) {
			LOGGER.info(" GET-ALL - retrived prescription list");
			return prescriptionItemDAO.getAll(namespaceDTO.getId());
		}
		throw new ServiceException("Access Denied", HttpStatus.FORBIDDEN);
	}

}
