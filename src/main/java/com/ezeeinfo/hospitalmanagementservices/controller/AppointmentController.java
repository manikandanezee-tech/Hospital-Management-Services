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

import com.ezeeinfo.hospitalmanagementservices.controller.io.AppointmentIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.AppointmentResponseIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DoctorDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PatientDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.AppointmentMapper;
import com.ezeeinfo.hospitalmanagementservices.service.AppointmentService;
import com.ezeeinfo.hospitalmanagementservices.service.DoctorService;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;
import com.ezeeinfo.hospitalmanagementservices.service.PatientService;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private NamespaceService namespaceService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

	@RequestMapping(method = RequestMethod.POST)
	public AppointmentResponseIO save(@RequestBody AppointmentIO appointmentIO, HttpServletRequest request) throws SQLException {

		NamespaceDTO namespaceDTO = namespaceService.getByCode(appointmentIO.getNamespaceIO().getCode());
		DoctorDTO doctorDTO = doctorService.getByCode(appointmentIO.getDoctorIO().getCode());
		PatientDTO patientDTO = patientService.getByCode(appointmentIO.getPatientIO().getCode());

		AppointmentDTO appointmentDTO = AppointmentMapper.toDTO(appointmentIO, namespaceDTO, doctorDTO, patientDTO);
		LOGGER.info("SAVE - Request to save the Appointment");
		AppointmentDTO appointmentDTO2 = appointmentService.save(appointmentDTO, request);

		AppointmentResponseIO appointmentResponseIO = AppointmentMapper.toIO(appointmentDTO2);
		LOGGER.info("SAVE - Appointment save successfully");
		return appointmentResponseIO;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public AppointmentResponseIO getByCode(@PathVariable String code) {
		LOGGER.info("GET-BY-CODE - Request to get Appointment data by code : {}", code);
		AppointmentDTO appointmentDTO = appointmentService.getByCode(code);

		AppointmentResponseIO appointmentResponseIO = AppointmentMapper.toIO(appointmentDTO);
		LOGGER.info("GET-BY-CODE -  Appointment successfully received using code : {}", code);
		return appointmentResponseIO;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<AppointmentResponseIO> getAll(HttpServletRequest request) {
		LOGGER.info("GET-ALL - Appointment information forward");
		List<AppointmentDTO> appointmentDTOList = appointmentService.getAll(request);

		List<AppointmentResponseIO> appointmentResponseIOList = appointmentDTOList.stream().map(DTO -> {
			AppointmentResponseIO appointmentResponseIO = AppointmentMapper.toIO(DTO);
			return appointmentResponseIO;
		}).toList();

		LOGGER.info("GET-ALL - Appointment information return");
		return appointmentResponseIOList;
	}

}
