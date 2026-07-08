package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.BillingDTO;

public interface BillingService {

	BillingDTO save(BillingDTO billingDTO, AuthDTO authDTO) throws SQLException;

	BillingDTO getByCode(String code, AuthDTO authDTO);

}
