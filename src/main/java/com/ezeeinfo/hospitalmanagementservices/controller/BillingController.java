package com.ezeeinfo.hospitalmanagementservices.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.hospitalmanagementservices.controller.io.BillingIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.BillingDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.BillingMapper;
import com.ezeeinfo.hospitalmanagementservices.service.AppointmentService;
import com.ezeeinfo.hospitalmanagementservices.service.BillingService;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;

@RestController
@RequestMapping("/billing")
public class BillingController {

	@Autowired
	private BillingService billingService;
	@Autowired
	private NamespaceService namespaceService;
	@Autowired
	private AppointmentService appointmentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(BillingController.class);

	@RequestMapping(method = RequestMethod.POST)
	public BillingIO save(@RequestBody BillingIO billingIO, HttpServletRequest request) throws SQLException {

		NamespaceDTO namespaceDTO = namespaceService.getByCode(billingIO.getNamespaceIO().getCode());
		AppointmentDTO appointmentDTO = appointmentService.getByCode(billingIO.getAppointmentResponseIO().getCode());
		
		BillingDTO billingDTO = BillingMapper.toDTO(billingIO, namespaceDTO, appointmentDTO);
		
		LOGGER.info("SAVE - Request to save the Billing");
		BillingDTO billingDTO2 = billingService.save(billingDTO, request);

		BillingIO billingIO2 = BillingMapper.toIO(billingDTO2);
		
		LOGGER.info("SAVE - Billing save successfully");
		return billingIO2;

	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public BillingIO getByCode(@PathVariable String code) {
		LOGGER.info("GET-BY-CODE - Request to get Billing data by code : {}", code);
		BillingDTO billingDTO = billingService.getByCode(code);
		
		BillingIO billingIO = BillingMapper.toIO(billingDTO);
		LOGGER.info("GET-BY-CODE -  Billing successfully received using code : {}", code);
		return billingIO;
	}

}
