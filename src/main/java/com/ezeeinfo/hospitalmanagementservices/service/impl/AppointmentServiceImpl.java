package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.dao.AppointmentDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.AppointmentStatusEM;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	@Autowired
	private AppointmentDAO appointmentDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	@Override
	public AppointmentDTO save(AppointmentDTO appointmentDTO, AuthDTO authDTO) throws SQLException {

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
			LOGGER.info("SAVE - Appointment details saved successfully");
			return appointmentDTO2;
		}
		throw new ServiceException("Access denied for Booking Appointment", HttpStatus.UNAUTHORIZED);

	}

	@Override
	public AppointmentDTO getByCode(String code, AuthDTO authDTO) {
		LOGGER.info("GET-BY-CODE - fetching the Appointment by code : {}", code);
		AppointmentDTO appointmentDTO = appointmentDAO.getByCode(code);
		if (authDTO.getUserDTO().getNamespaceDTO().getCode().equals(appointmentDTO.getNamespaceDTO().getCode())) {
			LOGGER.info("GET-BY-CODE - Retrive the Appointment successfully ");
			return appointmentDTO;
		}
		throw new ServiceException("Access Denied to get Appointment Details", HttpStatus.UNAUTHORIZED);
	}

	@Override
	public List<AppointmentDTO> getAll(AuthDTO authDTO) {
		int namespaceId = authDTO.getUserDTO().getNamespaceDTO().getId();

		LOGGER.info("GET-ALL - fetching Appointment list");
		List<AppointmentDTO> appointmentDTOList = appointmentDAO.getAll(namespaceId);
		LOGGER.info("GET-ALL - Retrived Appointment list");
		return appointmentDTOList;
	}

}
