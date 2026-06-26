package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.PaymentDTO;

public interface PaymentService {

	PaymentDTO save(PaymentDTO paymentDTO, HttpServletRequest request) throws SQLException;

	PaymentDTO getByCode(String code, HttpServletRequest request);

	List<PaymentDTO> getAll(HttpServletRequest request);

}
