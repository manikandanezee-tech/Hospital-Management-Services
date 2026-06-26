package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;

public interface MedicineService {

	MedicineDTO save(MedicineDTO medicineDTO, HttpServletRequest request) throws SQLException;

	List<MedicineDTO> getAll(HttpServletRequest request);

	MedicineDTO getByCode(String code);

	
}
