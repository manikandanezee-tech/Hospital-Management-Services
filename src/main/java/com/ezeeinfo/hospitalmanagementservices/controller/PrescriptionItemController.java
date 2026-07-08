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

import com.ezeeinfo.hospitalmanagementservices.controller.io.PrescriptionItemIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PrescriptionItemDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.PrescriptionItemsMapper;
import com.ezeeinfo.hospitalmanagementservices.service.AppointmentService;
import com.ezeeinfo.hospitalmanagementservices.service.MedicineService;
import com.ezeeinfo.hospitalmanagementservices.service.PrescriptionItemService;
import com.ezeeinfo.hospitalmanagementservices.service.UserService;

@RestController
@RequestMapping("/{authToken}/prescription")
public class PrescriptionItemController {

	@Autowired
	private PrescriptionItemService prescriptionItemService;
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private MedicineService medicineService;
	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PrescriptionItemController.class);

	@RequestMapping(method = RequestMethod.POST)
	public PrescriptionItemIO save(@PathVariable String authToken, @RequestBody PrescriptionItemIO prescriptionItemIO) throws SQLException {

		
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		AppointmentDTO appointmentDTO = appointmentService.getByCode(prescriptionItemIO.getAppointmentResponseIO().getCode(), authDTO);
		MedicineDTO medicineDTO = medicineService.getByCode(prescriptionItemIO.getMedicineIO().getCode(), authDTO);

		PrescriptionItemDTO prescriptionItemDTO = PrescriptionItemsMapper.toDTO(prescriptionItemIO, appointmentDTO, medicineDTO);

		LOGGER.info("SAVE - Request to save the prescription");
		PrescriptionItemDTO prescriptionItemDTO2 = prescriptionItemService.save(prescriptionItemDTO, authDTO);

		PrescriptionItemIO prescriptionItemIO2 = PrescriptionItemsMapper.toIO(prescriptionItemDTO2);
		LOGGER.info("SAVE - Prescription saved successfully");
		return prescriptionItemIO2;

	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public PrescriptionItemIO getByCode(@PathVariable String authToken, @PathVariable String code) throws SQLException {
		
		AuthDTO authDTO = userService.getAuthDTO(authToken);

		LOGGER.info("GET-BY-CODE - Request to get prescription by code : {}", code);
		PrescriptionItemDTO prescriptionItemDTO = prescriptionItemService.getByCode(code, authDTO);

		PrescriptionItemIO prescriptionItemIO = PrescriptionItemsMapper.toIO(prescriptionItemDTO);
		LOGGER.info("GET-BY-CODE - Prescription received by code : {}", code);
		return prescriptionItemIO;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<PrescriptionItemIO> getAll(@PathVariable String authToken) throws SQLException {

		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		LOGGER.info("GET-ALL - Request to get all prescription");
		List<PrescriptionItemDTO> prescriptionItemDTOList = prescriptionItemService.getAll(authDTO);
		List<PrescriptionItemIO> prescriptionItemIOList = prescriptionItemDTOList.stream().map(DTO -> {

			PrescriptionItemIO prescriptionItemIO = PrescriptionItemsMapper.toIO(DTO);
			return prescriptionItemIO;
		}).toList();
		
		LOGGER.info("GET-ALL - Successfully received Prescription list");
		return prescriptionItemIOList;
	}

}
