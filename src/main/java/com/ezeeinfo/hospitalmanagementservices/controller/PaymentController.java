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

import com.ezeeinfo.hospitalmanagementservices.controller.io.PaymentRequestIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.PaymentResponseIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.BillingDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PaymentDTO;
import com.ezeeinfo.hospitalmanagementservices.mapper.PaymentMapper;
import com.ezeeinfo.hospitalmanagementservices.service.BillingService;
import com.ezeeinfo.hospitalmanagementservices.service.PaymentService;
import com.ezeeinfo.hospitalmanagementservices.service.UserService;

@RestController
@RequestMapping("/{authToken}/payment")
public class PaymentController {
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private BillingService billingService;
	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

	@RequestMapping(method = RequestMethod.POST)
	public PaymentResponseIO save(@PathVariable String authToken, @RequestBody PaymentRequestIO paymentRequestIO) throws SQLException {

		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		BillingDTO billingDTO = billingService.getByCode(paymentRequestIO.getBillingIO().getCode(), authDTO);

		PaymentDTO paymentDTO = PaymentMapper.toDTO(paymentRequestIO, billingDTO);

		LOGGER.info("SAVE - Request to save payment");
		PaymentDTO paymentDTO2 = paymentService.save(paymentDTO, authDTO);

		PaymentResponseIO paymentResponseIO = PaymentMapper.toIO(paymentDTO2);

		LOGGER.info("SAVE - Successfully Payment saved");
		return paymentResponseIO;
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public PaymentResponseIO getByCode(@PathVariable String authToken, @PathVariable String code) throws SQLException {
		
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		LOGGER.info("GET-BY-CODE - Requested to get payment by code : {}", code);
		PaymentDTO paymentDTO = paymentService.getByCode(code, authDTO);

		PaymentResponseIO paymentResponseIO = PaymentMapper.toIO(paymentDTO);

		LOGGER.info("GET-BY-CODE - Sucessfully payment retrived by code");
		return paymentResponseIO;
	}
	@RequestMapping(method = RequestMethod.GET)
	public List<PaymentResponseIO> getAll(@PathVariable String authToken) throws SQLException {
		
		AuthDTO authDTO = userService.getAuthDTO(authToken);
		
		LOGGER.info("GET-ALL - request to get all payment");
		List<PaymentDTO> paymentDTOList = paymentService.getAll(authDTO);

		List<PaymentResponseIO> paymentResponseIOList = paymentDTOList.stream().map(DTO -> {

			PaymentResponseIO paymentResponseIO = PaymentMapper.toIO(DTO);

			return paymentResponseIO;

		}).toList();

		LOGGER.info("GET-ALL - Susccessfully get all payment list");
		return paymentResponseIOList;
	}
}
