package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PaymentDTO;

public interface PaymentService {

	PaymentDTO save(PaymentDTO paymentDTO, AuthDTO authDTO) throws SQLException;

	PaymentDTO getByCode(String code, AuthDTO authDTO);

	List<PaymentDTO> getAll(AuthDTO authDTO);

}
