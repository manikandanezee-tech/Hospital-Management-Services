package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.BillingDTO;

public interface BillingService {

	BillingDTO save(BillingDTO billingDTO, HttpServletRequest request) throws SQLException;

	BillingDTO getByCode(String code);

}
