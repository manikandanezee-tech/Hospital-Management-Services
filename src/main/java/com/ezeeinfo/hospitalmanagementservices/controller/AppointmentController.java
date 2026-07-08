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

import com.ezeeinfo.hospitalmanagementservices.controller.io.AppointmentIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.AppointmentResponseIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DoctorDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PatientDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.AppointmentMapper;
import com.ezeeinfo.hospitalmanagementservices.service.AppointmentService;
import com.ezeeinfo.hospitalmanagementservices.service.DoctorService;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;
import com.ezeeinfo.hospitalmanagementservices.service.PatientService;
import com.ezeeinfo.hospitalmanagementservices.service.UserService;

@RestController
@RequestMapping("/{authToken}/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private NamespaceService namespaceService;
	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

	@RequestMapping(method = RequestMethod.POST)
	public AppointmentResponseIO save( @PathVariable String authToken, @RequestBody AppointmentIO appointmentIO) throws SQLException {
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		NamespaceDTO namespaceDTO = namespaceService.getByCode(authDTO, appointmentIO.getNamespaceIO().getCode());
		DoctorDTO doctorDTO = doctorService.getByCode(appointmentIO.getDoctorIO().getCode(), authDTO);
		PatientDTO patientDTO = patientService.getByCode(appointmentIO.getPatientIO().getCode(), authDTO);

		AppointmentDTO appointmentDTO = AppointmentMapper.toDTO(appointmentIO, namespaceDTO, doctorDTO, patientDTO);
		LOGGER.info("SAVE - Request to save the Appointment");
		AppointmentDTO appointmentDTO2 = appointmentService.save(appointmentDTO, authDTO);

		AppointmentResponseIO appointmentResponseIO = AppointmentMapper.toIO(appointmentDTO2);
		LOGGER.info("SAVE - Appointment save successfully");
		return appointmentResponseIO;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public AppointmentResponseIO getByCode( @PathVariable String authToken, @PathVariable String code) throws SQLException {
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		LOGGER.info("GET-BY-CODE - Request to get Appointment data by code : {}", code);
		AppointmentDTO appointmentDTO = appointmentService.getByCode(code, authDTO);

		AppointmentResponseIO appointmentResponseIO = AppointmentMapper.toIO(appointmentDTO);
		LOGGER.info("GET-BY-CODE -  Appointment successfully received using code : {}", code);
		return appointmentResponseIO;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<AppointmentResponseIO> getAll(@PathVariable String authToken) throws SQLException {
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		LOGGER.info("GET-ALL - Appointment information forward");
		List<AppointmentDTO> appointmentDTOList = appointmentService.getAll(authDTO);

		List<AppointmentResponseIO> appointmentResponseIOList = appointmentDTOList.stream().map(DTO -> {
			AppointmentResponseIO appointmentResponseIO = AppointmentMapper.toIO(DTO);
			return appointmentResponseIO;
		}).toList();

		LOGGER.info("GET-ALL - Appointment information return");
		return appointmentResponseIOList;
	}

}
