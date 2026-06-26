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

import com.ezeeinfo.hospitalmanagementservices.controller.io.MedicineIO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.MedicineMapper;
import com.ezeeinfo.hospitalmanagementservices.service.MedicineService;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;

@RestController
@RequestMapping("/medicine")
public class MedicineController {
	@Autowired
	private MedicineService medicineService;
	@Autowired
	private NamespaceService namespaceService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MedicineController.class);

	@RequestMapping(method = RequestMethod.POST)
	public MedicineIO save(@RequestBody MedicineIO medicineIO, HttpServletRequest request) throws SQLException {

		NamespaceDTO namespaceDTO = namespaceService.getByCode(medicineIO.getNamespaceIO().getCode());

		MedicineDTO medicineDTO = MedicineMapper.toDTO(medicineIO, namespaceDTO);

		LOGGER.info("SAVE - Request to save the Medicine");
		MedicineDTO medicineDTO2 = medicineService.save(medicineDTO, request);

		MedicineIO medicineIO2 = MedicineMapper.toIO(medicineDTO2);
		LOGGER.info("SAVE - Medicine saved successfully");
		return medicineIO2;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public MedicineIO getByCode(@PathVariable String code) {
		LOGGER.info("GET-BY-CODE - Request to get medicine by code : {}", code);
		MedicineDTO medicineDTO = medicineService.getByCode(code);

		MedicineIO medicineIO = MedicineMapper.toIO(medicineDTO);
		LOGGER.info("GET-BY-CODE - Medicine successfully received using code : {}", code);
		return medicineIO;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<MedicineIO> getAll(HttpServletRequest request) {
		LOGGER.info("GET-ALL - Request to get medicine list");
		List<MedicineDTO> medicineDTOList = medicineService.getAll(request);

		List<MedicineIO> medicineIOList = medicineDTOList.stream().map(DTO -> {

			MedicineIO medicineIO = MedicineMapper.toIO(DTO);
			return medicineIO;
		}).toList();

		LOGGER.info("GET-ALL - medicine list received");
		return medicineIOList;
	}
}
