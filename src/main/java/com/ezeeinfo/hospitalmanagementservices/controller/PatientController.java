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

import com.ezeeinfo.hospitalmanagementservices.controller.io.PatientIO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PatientDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.PatientMapper;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;
import com.ezeeinfo.hospitalmanagementservices.service.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private PatientService patientService;
	@Autowired
	private NamespaceService namespaceService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

	@RequestMapping(method = RequestMethod.POST)
	public PatientIO save(@RequestBody PatientIO patientIO, HttpServletRequest request) throws SQLException {
		
		

		NamespaceDTO namespaceDTO = namespaceService.getByCode(patientIO.getNamespaceIO().getCode());
		PatientDTO patientDTO = PatientMapper.toDTO(patientIO, namespaceDTO);
		LOGGER.info("SAVE - Request to save Patient");
		PatientDTO patientDTO2 = patientService.save(patientDTO, request);
		PatientIO patientIO2 = PatientMapper.toIO(patientDTO2);
		LOGGER.info("SAVE - Successfully Patient saved");
		return patientIO2;

	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public PatientIO getByCode(@PathVariable String code) {
		LOGGER.info("GET-BY-CODE - Requested to get Patient by code : {}",code);
		PatientDTO patientDTO = patientService.getByCode(code);
		PatientIO patientIO = PatientMapper.toIO(patientDTO);
		LOGGER.info("GET-BY-CODE - Sucessfully Patient retrived by code");
		return patientIO;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<PatientIO> getAll(HttpServletRequest request) {
		LOGGER.info("GET-ALL - request to get all Patients");
		List<PatientDTO> patientDTOList = patientService.getAll(request);
		List<PatientIO> patientIOList = patientDTOList.stream().map(DTO -> {
			PatientIO patientIO = PatientMapper.toIO(DTO);
			return patientIO;
		}).toList();
		LOGGER.info("GET-ALL - Susccessfully get all patient list");
		return patientIOList;
	}

}
