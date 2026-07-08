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
import com.ezeeinfo.hospitalmanagementservices.dao.PaymentDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.BillingDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PaymentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.BillStatusEM;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.PaymentModeEM;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentDAO paymentDAO;
	@Autowired
	private BillingDAO billingDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Override
	public PaymentDTO save(PaymentDTO paymentDTO, AuthDTO authDTO) throws SQLException {
		String role = authDTO.getUserDTO().getRole().getCode();
		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();

		paymentDTO.setUpdatedBy(authDTO.getUserDTO());
		if (role.equals("CASHIER") && namespaceCode.equals(paymentDTO.getBillingDTO().getNamespaceDTO().getCode())) {

			BillingDTO billingDTO = billingDAO.getByCode(paymentDTO.getBillingDTO().getCode());

			if (paymentDTO.getAmount().compareTo(billingDTO.getBalanceAmount()) <= 0) {
				if (!PaymentModeEM.CASH.getCode().equals(paymentDTO.getPaymentMode().getCode())) {
					paymentDTO.setTransactionRefNumber("TXN" + System.currentTimeMillis());
				}

				billingDTO.setPaidAmount(billingDTO.getPaidAmount().add(paymentDTO.getAmount()));
				billingDTO.setBalanceAmount(billingDTO.getTotalAmount().subtract(billingDTO.getPaidAmount()));
				if (billingDTO.getBalanceAmount().compareTo(BigDecimal.ZERO) == 0) {

					billingDTO.setBillStatus(BillStatusEM.PAIDED);
				}

				else if (billingDTO.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
					billingDTO.setBillStatus(BillStatusEM.PARTIALLY_PAID);
				}
				else {
					billingDTO.setBillStatus(BillStatusEM.PENDING);
				}
				billingDTO.setUpdatedBy(authDTO.getUserDTO());
				billingDAO.save(billingDTO);
				paymentDTO.setBillingDTO(billingDTO);

				LOGGER.info(" SAVE - Successfully validating the Payment");
				PaymentDTO paymentDTO2 = paymentDAO.save(paymentDTO);

				LOGGER.info(" SAVE - Successfully saved the payment");
				return paymentDTO2;
			}

			throw new ServiceException("Don't overPay the amount", HttpStatus.BAD_REQUEST);
		}
		throw new ServiceException("Access Denied for payment", HttpStatus.UNAUTHORIZED);

	}

	@Override
	public PaymentDTO getByCode(String code, AuthDTO authDTO) {
		PaymentDTO paymentDTO = paymentDAO.getByCode(code);
		if (paymentDTO.getBillingDTO().getNamespaceDTO().getCode().equals(authDTO.getUserDTO().getNamespaceDTO().getCode())) {
			LOGGER.info(" GET-BY-CODE - Fetching the Payment details by code : {}", code);
			PaymentDTO paymentDTO2 = paymentDAO.getByCode(code);
			LOGGER.info(" GET-BY-CODE - Successfully retrive the payment details by code : {}", code);
			return paymentDTO2;
		}
		throw new ServiceException("Access Denied", HttpStatus.CONFLICT);
	}

	@Override
	public List<PaymentDTO> getAll(AuthDTO authDTO) {
		int namespaceId = authDTO.getUserDTO().getNamespaceDTO().getId();
		LOGGER.info(" GET-ALL - Retrived Payment list");
		return paymentDAO.getAll(namespaceId);
	}

}
