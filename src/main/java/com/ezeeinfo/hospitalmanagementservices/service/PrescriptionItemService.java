package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.PrescriptionItemDTO;

public interface PrescriptionItemService {

	PrescriptionItemDTO save(PrescriptionItemDTO prescriptionItemDTO, HttpServletRequest request) throws SQLException;

	PrescriptionItemDTO getByCode(String code);

	List<PrescriptionItemDTO> getAll(HttpServletRequest request);

}
