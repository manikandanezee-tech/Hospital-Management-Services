package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.dao.ConsultationDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.MedicineDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.PrescriptionItemDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PrescriptionItemDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.PrescriptionItemService;

@Service
public class PrescriptionItemServiceImpl implements PrescriptionItemService {
	@Autowired
	private PrescriptionItemDAO prescriptionItemDAO;
	@Autowired
	private MedicineDAO medicineDAO;
	@Autowired
	private ConsultationDAO consultationDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(PrescriptionItemServiceImpl.class);

	@Override
	public PrescriptionItemDTO save(PrescriptionItemDTO prescriptionItemDTO, AuthDTO authDTO) throws SQLException {

		int userId = authDTO.getUserDTO().getId();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();

		prescriptionItemDTO.setUpdatedBy(authDTO.getUserDTO());

		ConsultationDTO consultationDTO = consultationDAO.isAlreayConsulted(prescriptionItemDTO.getAppointmentDTO().getId());
		if (consultationDTO == null) {
			throw new ServiceException("Please complete the consultation before prescribing medicines...", HttpStatus.BAD_REQUEST);
		}
		// check prescribe doctor and appointment doctor are same
		if (prescriptionItemDTO.getAppointmentDTO().getDoctorDTO().getUserDTO().getId() == userId && prescriptionItemDTO.getMedicineDTO().getNamespaceDTO().getCode().equals(namespaceCode)) {
			MedicineDTO medicineDTO = medicineDAO.getByCode(prescriptionItemDTO.getMedicineDTO().getCode());

			if (medicineDTO.getCurrentStock() < prescriptionItemDTO.getQuantity()) {
				throw new ServiceException("Medicine Stock Out of Range", HttpStatus.CONFLICT);
			}
			
			LOGGER.info(" SAVE - Successfully validating the prescription");
			PrescriptionItemDTO prescriptionItemDTO2 = prescriptionItemDAO.save(prescriptionItemDTO);

			medicineDTO.setCurrentStock(medicineDTO.getCurrentStock() - prescriptionItemDTO.getQuantity());
			medicineDTO.setUpdatedBy(authDTO.getUserDTO());
			medicineDAO.save(medicineDTO);
			LOGGER.info(" SAVE - Prescription successfully saved ");
			return prescriptionItemDTO2;
		}
		throw new ServiceException("Access Denied to prescribe medicines", HttpStatus.FORBIDDEN);

	}

	@Override
	public PrescriptionItemDTO getByCode(String code, AuthDTO authDTO) {
		LOGGER.info(" GET-BY-CODE - Fetching the prescription details by code : {}", code);
		PrescriptionItemDTO prescriptionItemDTO = prescriptionItemDAO.getByCode(code);
		if (authDTO.getUserDTO().getNamespaceDTO().getCode().equals(prescriptionItemDTO.getAppointmentDTO().getNamespaceDTO().getCode())) {
			LOGGER.info(" GET-BY-CODE - successfully retrived prescription details by code : {}", code);
			return prescriptionItemDTO;
		}
		throw new ServiceException("Access Denied to get Prescription Details", HttpStatus.UNAUTHORIZED);
	}

	@Override
	public List<PrescriptionItemDTO> getAll(AuthDTO authDTO) {
		int namespaceId = authDTO.getUserDTO().getNamespaceDTO().getId();

		if (authDTO.getUserDTO().getRole().getCode().equals("DOCT")) {
			LOGGER.info(" GET-ALL - retrived prescription list");
			return prescriptionItemDAO.getAll(namespaceId);
		}
		throw new ServiceException("Access Denied", HttpStatus.FORBIDDEN);
	}

}
