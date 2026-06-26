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

import com.ezeeinfo.hospitalmanagementservices.controller.io.DoctorIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.DoctorResponseIO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DoctorDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.DoctorMapper;
import com.ezeeinfo.hospitalmanagementservices.service.DepartmentService;
import com.ezeeinfo.hospitalmanagementservices.service.DoctorService;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;
import com.ezeeinfo.hospitalmanagementservices.service.UserDTOService;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

	@Autowired
	private DoctorService doctorService;
	@Autowired
	private NamespaceService namespaceService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private UserDTOService userService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);

	@RequestMapping(method = RequestMethod.POST)
	public DoctorResponseIO save(@RequestBody DoctorIO doctorIO,HttpServletRequest request) throws SQLException {
		
		
		NamespaceDTO namespaceDTO= namespaceService.getByCode(doctorIO.getNamespaceIO().getCode());
		UserDTO userDTO =userService.getByCode(doctorIO.getUserResponseIO().getCode());
		DepartmentDTO departmentDTO = departmentService.getByCode(doctorIO.getDepartmentIO().getCode());
		
		DoctorDTO doctorDTO = DoctorMapper.toDTO(doctorIO, userDTO, namespaceDTO, departmentDTO);		
		LOGGER.info("SAVE - Doctor information forward");
		DoctorDTO doctorDTO2 = doctorService.save(doctorDTO,request);

		DoctorResponseIO doctorResponseIO = DoctorMapper.toIO(doctorDTO2);
		LOGGER.info("SAVE - Doctor information return");
		return doctorResponseIO;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public DoctorResponseIO getByCode(@PathVariable String code) {
		LOGGER.info("GET-BY-CODE - Doctor information forward");
		DoctorDTO doctorDTO = doctorService.getByCode(code);

		DoctorResponseIO doctorResponseIO = DoctorMapper.toIO(doctorDTO);
		LOGGER.info("GET-BY-CODE - Doctor information return");
		return doctorResponseIO;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<DoctorResponseIO> getAll(HttpServletRequest request) throws SQLException {
		LOGGER.info("GET-ALL - Doctor information forward");
		List<DoctorDTO> doctorDTOList = doctorService.getAll(request);

		List<DoctorResponseIO> doctorResponseIOList = doctorDTOList.stream().map(DTO -> {
			DoctorResponseIO doctorResponseIO = DoctorMapper.toIO(DTO);
			return doctorResponseIO;

		}).toList();
		LOGGER.info("GET-ALL - Doctor information return");
		return doctorResponseIOList;
	}

}
