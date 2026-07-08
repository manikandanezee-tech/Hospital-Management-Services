package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.hospitalmanagementservices.controller.io.ConsultationIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.ConsultationMapper;
import com.ezeeinfo.hospitalmanagementservices.service.AppointmentService;
import com.ezeeinfo.hospitalmanagementservices.service.ConsultationService;
import com.ezeeinfo.hospitalmanagementservices.service.UserService;

@RestController
@RequestMapping("/{authToken}/consultation")
public class ConsultationController {

	@Autowired
	private ConsultationService consultationService;
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsultationController.class);

	@RequestMapping(method = RequestMethod.POST)
	public ConsultationIO save(@PathVariable String authToken, @RequestBody ConsultationIO consultationIO) throws SQLException {

		AuthDTO authDTO = userService.getAuthDTO(authToken);

		AppointmentDTO appointmentDTO = appointmentService.getByCode(consultationIO.getAppointmentResponseIO().getCode(), authDTO);

		ConsultationDTO consultationDTO = ConsultationMapper.toDTO(consultationIO, appointmentDTO);

		LOGGER.info("SAVE - Request to save the Consultation");
		ConsultationDTO consultationDTO2 = consultationService.save(consultationDTO, authDTO);

		ConsultationIO consultationIO2 = ConsultationMapper.toIO(consultationDTO2);
		LOGGER.info("SAVE - Consultation save successfully");
		return consultationIO2;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public ConsultationIO getByCode(@PathVariable String authToken, @PathVariable String code) throws SQLException {
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		LOGGER.info("GET-BY-CODE - Request to get Consultation by code : {}", code);
		ConsultationDTO consultationDTO = consultationService.getByCode(code, authDTO);

		ConsultationIO consultationIO = ConsultationMapper.toIO(consultationDTO);

		LOGGER.info("GET-BY-CODE -  Consultation successfully received using code : {}", code);
		return consultationIO;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ConsultationIO> getAll(@PathVariable String authToken) throws SQLException {
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		LOGGER.info("GET-ALL - Request to get Consultation List");
		List<ConsultationDTO> consultationDTOList = consultationService.getAll(authDTO);

		List<ConsultationIO> consultationIOList = consultationDTOList.stream().map(DTO -> {

			ConsultationIO consultationIO = ConsultationMapper.toIO(DTO);

			return consultationIO;
		}).toList();

		LOGGER.info("GET-ALL - Consultation List successfully received");
		return consultationIOList;
	}
}
