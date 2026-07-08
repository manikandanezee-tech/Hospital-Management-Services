package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.dao.BillingDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.ConsultationDAO;
import com.ezeeinfo.hospitalmanagementservices.dao.PrescriptionItemDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.BillingDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PrescriptionItemDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.BillStatusEM;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.BillingService;

@Service
public class BillingServiceImpl implements BillingService {
	@Autowired
	private BillingDAO billingDAO;
	@Autowired
	private ConsultationDAO consultationDAO;
	@Autowired
	private PrescriptionItemDAO prescriptionItemDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(BillingServiceImpl.class);

	@Override
	public BillingDTO save(BillingDTO billingDTO, AuthDTO authDTO) throws SQLException {
		String role = authDTO.getUserDTO().getRole().getCode();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();

		billingDTO.setUpdatedBy(authDTO.getUserDTO());

		if (role.equals("RECEPNST") && namespaceCode.equals(billingDTO.getNamespaceDTO().getCode())) {
			BillingDTO billingDTO1 = new BillingDTO();
			ConsultationDTO isConsulted = consultationDAO.isAlreayConsulted(billingDTO.getAppointmentDTO().getId());
			if (isConsulted == null) {
				throw new ServiceException("Consultation Not found", HttpStatus.NOT_FOUND);
			}
			BillingDTO alreadyBillGenerate = billingDAO.isBillAlreadyGenerated(billingDTO.getAppointmentDTO().getId());
			if (billingDTO.getCode() == null && alreadyBillGenerate != null) {
				throw new ServiceException("Bill already generated", HttpStatus.CONFLICT);
			}
			// calculate the doctor fee for total amount of billing
			BigDecimal totalAmount = billingDTO.getAppointmentDTO().getDoctorDTO().getConsultationFee();
			// one consultation have more prescription so get the prescribe list
			// with appointment id.
			List<PrescriptionItemDTO> list = prescriptionItemDAO.getAllByAppointment(billingDTO.getAppointmentDTO().getId());

			for (PrescriptionItemDTO prescriptionItemDTO : list) {
				// medicine price
				BigDecimal price = prescriptionItemDTO.getMedicineDTO().getPrice();
				// prescribe quantity
				int quantity = prescriptionItemDTO.getQuantity();

				BigDecimal medicinePrice = price.multiply(BigDecimal.valueOf(quantity));
				// added to the total amount
				totalAmount = totalAmount.add(medicinePrice);
			}
			if (billingDTO.getCode() != null) {
				BillingDTO billingDTO2 = billingDAO.getByCode(billingDTO.getCode());
				billingDTO.setTotalAmount(totalAmount);
				billingDTO.setPaidAmount(billingDTO2.getPaidAmount());
				billingDTO.setBalanceAmount(billingDTO2.getBalanceAmount());
				billingDTO.setBillStatus(billingDTO2.getBillStatus());
				LOGGER.info("SAVE - successfully validation for updation");
				billingDTO1 = billingDAO.save(billingDTO);
			}
			else {
				billingDTO.setTotalAmount(totalAmount);
				billingDTO.setPaidAmount(BigDecimal.ZERO);
				billingDTO.setBalanceAmount(totalAmount.subtract(billingDTO.getPaidAmount()));
				billingDTO.setBillStatus(BillStatusEM.PENDING);
				LOGGER.info("SAVE - successfully validation for insertion");
				billingDTO1 = billingDAO.save(billingDTO);
			}
			LOGGER.info("SAVE - billing details saved successfully");
			return billingDTO1;
		}
		throw new ServiceException("Access Denied for Billing", HttpStatus.UNAUTHORIZED);
	}

	@Override
	public BillingDTO getByCode(String code, AuthDTO authDTO) {
		LOGGER.info("GET-BY-CODE - fetching the billing by code : {}", code);
		BillingDTO billingDTO = billingDAO.getByCode(code);

		if (authDTO.getUserDTO().getNamespaceDTO().getCode().equals(billingDTO.getNamespaceDTO().getCode())) {
			LOGGER.info("GET-BY-CODE - Retrive the billing successfully ");
			return billingDTO;
		}
		throw new ServiceException("Access Denied to get Billing Details", HttpStatus.UNAUTHORIZED);
	}

}
