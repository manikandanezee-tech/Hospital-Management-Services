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
import com.ezeeinfo.hospitalmanagementservices.dao.AppointmentDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.ConsultationDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.UserDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.AppointmentStatusEM;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.ConsultationService;

@Service
public class ConsultationServiceImpl implements ConsultationService {

	@Autowired
	private ConsultationDAO consultationDAO;
	@Autowired
	private NamespaceCache namespaceCache;
	@Autowired
	private AppointmentDAO appointmendDAO;
	@Autowired
	private UserDAO userDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsultationServiceImpl.class);

	@Override
	public ConsultationDTO save(ConsultationDTO consultationDTO, HttpServletRequest request) throws SQLException {
		AuthDTO authDTO = (AuthDTO)request.getAttribute("authDTO");

		int userId = authDTO.getUserDTO().getId();
		
		consultationDTO.setUpdatedBy(authDTO.getUserDTO());

		ConsultationDTO consultationDTO2 = null;

		if (consultationDTO.getAppointmentDTO().getDoctorDTO().getUserDTO().getId() == userId) {
			// for update already exist consulting
			if (consultationDTO.getCode() != null) {
				consultationDAO.getByCode(consultationDTO.getCode());
				LOGGER.info("SAVE - successfully validation the consultation");
				consultationDTO2 = consultationDAO.save(consultationDTO);
				
			}
			// for new consulting (insert)
			// check if already consulted using appointment id.
			ConsultationDTO ExistConsultationDTO = consultationDAO.isAlreayConsulted(consultationDTO.getAppointmentDTO().getId());
			if (ExistConsultationDTO == null) {
				consultationDTO2 = consultationDAO.save(consultationDTO);

				AppointmentDTO appointmentDTO = appointmendDAO.getByCode(consultationDTO2.getAppointmentDTO().getCode());
				appointmentDTO.setStatus(AppointmentStatusEM.COMPLETED);
				appointmentDTO.setUpdatedBy(userDAO.getById(userId));
				LOGGER.info("SAVE - successfully validation the consultation");
				appointmendDAO.save(appointmentDTO);
				
			}
			if(consultationDTO2!=null) {
				LOGGER.info("SAVE - insert/updated successfully");
				return consultationDTO2;
			}
			throw new ServiceException("Appointment Already Consulted", HttpStatus.CONFLICT);
		}
		throw new ServiceException("Access Denied for Consulting Patient", HttpStatus.UNAUTHORIZED);

	}

	@Override
	public ConsultationDTO getByCode(String code) {
		LOGGER.info("GET-BY-CODE - fetching the consultation by code : {}",code);
		ConsultationDTO consultationDTO = consultationDAO.getByCode(code);
		LOGGER.info("GET-BY-CODE - Retrive the Consultation successfully ");
		return consultationDTO;
	}

	@Override
	public List<ConsultationDTO> getAll(HttpServletRequest request) {
		AuthDTO authDTO = (AuthDTO)request.getAttribute("authDTO");
		NamespaceDTO namespaceDTO = namespaceCache.getByCode(authDTO.getUserDTO().getNamespaceDTO().getCode());
		
		LOGGER.info("GET-ALL - fetching Consultation list");
		List<ConsultationDTO> consultationDTOList = consultationDAO.getAll(namespaceDTO.getId());
		LOGGER.info("GET-ALL - retriving Consultation list");
		return consultationDTOList;
	}
}
