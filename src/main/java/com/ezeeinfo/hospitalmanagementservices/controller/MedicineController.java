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

import com.ezeeinfo.hospitalmanagementservices.controller.io.MedicineIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.MedicineMapper;
import com.ezeeinfo.hospitalmanagementservices.service.MedicineService;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;
import com.ezeeinfo.hospitalmanagementservices.service.UserService;

@RestController
@RequestMapping("/{authToken}/medicine")
public class MedicineController {
	@Autowired
	private MedicineService medicineService;
	@Autowired
	private NamespaceService namespaceService;
	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MedicineController.class);

	@RequestMapping(method = RequestMethod.POST)
	public MedicineIO save(@PathVariable String authToken, @RequestBody MedicineIO medicineIO) throws SQLException {

		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		NamespaceDTO namespaceDTO = namespaceService.getByCode(authDTO, medicineIO.getNamespaceIO().getCode());

		MedicineDTO medicineDTO = MedicineMapper.toDTO(medicineIO, namespaceDTO);

		LOGGER.info("SAVE - Request to save the Medicine");
		MedicineDTO medicineDTO2 = medicineService.save(medicineDTO, authDTO);

		MedicineIO medicineIO2 = MedicineMapper.toIO(medicineDTO2);
		LOGGER.info("SAVE - Medicine saved successfully");
		return medicineIO2;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public MedicineIO getByCode(@PathVariable String authToken, @PathVariable String code) throws SQLException {
		
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		LOGGER.info("GET-BY-CODE - Request to get medicine by code : {}", code);
		MedicineDTO medicineDTO = medicineService.getByCode(code, authDTO);

		MedicineIO medicineIO = MedicineMapper.toIO(medicineDTO);
		LOGGER.info("GET-BY-CODE - Medicine successfully received using code : {}", code);
		return medicineIO;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<MedicineIO> getAll(@PathVariable String authToken) throws SQLException {
		
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		LOGGER.info("GET-ALL - Request to get medicine list");
		List<MedicineDTO> medicineDTOList = medicineService.getAll(authDTO);

		List<MedicineIO> medicineIOList = medicineDTOList.stream().map(DTO -> {

			MedicineIO medicineIO = MedicineMapper.toIO(DTO);
			return medicineIO;
		}).toList();

		LOGGER.info("GET-ALL - medicine list received");
		return medicineIOList;
	}
}
