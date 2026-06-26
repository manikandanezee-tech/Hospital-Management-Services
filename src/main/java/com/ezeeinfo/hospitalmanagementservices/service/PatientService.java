package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.PatientDTO;

public interface PatientService {

	PatientDTO save(PatientDTO patientDTO, HttpServletRequest request) throws SQLException;

	PatientDTO getByCode(String code);

	List<PatientDTO> getAll(HttpServletRequest request);

}
