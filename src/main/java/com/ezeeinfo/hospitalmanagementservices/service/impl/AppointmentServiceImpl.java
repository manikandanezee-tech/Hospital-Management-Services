package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.cache.NamespaceCache;
import com.ezeeinfo.hospitalmanagementservices.dao.AppointmentDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.AppointmentStatusEM;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	@Autowired
	private AppointmentDAO appointmentDAO;
	@Autowired
	private NamespaceCache namespaceCache;
	private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	@Override
	public AppointmentDTO save(AppointmentDTO appointmentDTO, HttpServletRequest request) throws SQLException {
		AuthDTO authDTO = (AuthDTO) request.getAttribute("authDTO");
		String role = authDTO.getUserDTO().getRole().getCode();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();

		appointmentDTO.setUpdatedBy(authDTO.getUserDTO());

		if (role.equals("RECEPNST") && namespaceCode.equals(appointmentDTO.getNamespaceDTO().getCode())) {
			AppointmentDTO appointmentDTO2 = new AppointmentDTO();
			int doctorId = appointmentDTO.getDoctorDTO().getId();

			LocalDateTime date = appointmentDTO.getAppointmentDateTime();

			int namespaceId = appointmentDTO.getNamespaceDTO().getId();

			int tokenNumber = appointmentDAO.getAppointmentCountOnDate(doctorId, date, namespaceId);

			appointmentDTO.setTokenNumber(tokenNumber + 1);
			appointmentDTO.setStatus(AppointmentStatusEM.BOOKED);
			LOGGER.info("SAVE - successfully validation the appointment");
			appointmentDTO2 = appointmentDAO.save(appointmentDTO);
			LOGGER.info("SAVE - Appointment details insert/updated successfully");
			return appointmentDTO2;
		}
		throw new ServiceException("Access denied for Booking Appointment", HttpStatus.UNAUTHORIZED);

	}

	@Override
	public AppointmentDTO getByCode(String code) {
		LOGGER.info("GET-BY-CODE - fetching the Appointment by code : {}",code);
		AppointmentDTO appointmentDTO = appointmentDAO.getByCode(code);
		LOGGER.info("GET-BY-CODE - Retrive the Appointment successfully ");
		return appointmentDTO;
	}

	@Override
	public List<AppointmentDTO> getAll(HttpServletRequest request) {
		AuthDTO authDTO = (AuthDTO) request.getAttribute("authDTO");
		NamespaceDTO namespaceDTO = namespaceCache.getByCode(authDTO.getUserDTO().getNamespaceDTO().getCode());
		
		LOGGER.info("GET-ALL - fetching Appointment list");
		List<AppointmentDTO> appointmentDTOList = appointmentDAO.getAll(namespaceDTO.getId());
		LOGGER.info("GET-ALL - Retrived Appointment list");
		return appointmentDTOList;
	}

}
