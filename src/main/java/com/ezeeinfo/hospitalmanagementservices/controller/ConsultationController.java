package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.ConsultationMapper;
import com.ezeeinfo.hospitalmanagementservices.service.AppointmentService;
import com.ezeeinfo.hospitalmanagementservices.service.ConsultationService;

@RestController
@RequestMapping("/consultation")
public class ConsultationController {

	@Autowired
	private ConsultationService consultationService;
	@Autowired
	private AppointmentService appointmentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsultationController.class);

	@RequestMapping(method = RequestMethod.POST)
	public ConsultationIO save(@RequestBody ConsultationIO consultationIO, HttpServletRequest request) throws SQLException {

		AppointmentDTO appointmentDTO = appointmentService.getByCode(consultationIO.getAppointmentResponseIO().getCode());

		ConsultationDTO consultationDTO = ConsultationMapper.toDTO(consultationIO, appointmentDTO);

		LOGGER.info("SAVE - Request to save the consultation");
		ConsultationDTO consultationDTO2 = consultationService.save(consultationDTO, request);

		ConsultationIO consultationIO2 = ConsultationMapper.toIO(consultationDTO2);
		LOGGER.info("SAVE - consultation save successfully");
		return consultationIO2;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public ConsultationIO getByCode(@PathVariable String code) {
		LOGGER.info("GET-BY-CODE - Request to get consultation by code : {}", code);
		ConsultationDTO consultationDTO = consultationService.getByCode(code);

		ConsultationIO consultationIO = ConsultationMapper.toIO(consultationDTO);

		LOGGER.info("GET-BY-CODE -  Consultation successfully received using code : {}", code);
		return consultationIO;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<ConsultationIO> getAll(HttpServletRequest request) {
		LOGGER.info("GET-ALL - Request to get Consultation List");
		List<ConsultationDTO> consultationDTOList = consultationService.getAll(request);

		List<ConsultationIO> consultationIOList = consultationDTOList.stream().map(DTO -> {

			ConsultationIO consultationIO = ConsultationMapper.toIO(DTO);

			return consultationIO;
		}).toList();
		
		LOGGER.info("GET-ALL - Department List successfully received");
		return consultationIOList;
	}
}
