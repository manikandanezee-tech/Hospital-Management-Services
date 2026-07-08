package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PatientDTO;

public interface PatientService {

	PatientDTO save(PatientDTO patientDTO, AuthDTO authDTO) throws SQLException;

	PatientDTO getByCode(String code, AuthDTO authDTO);

	List<PatientDTO> getAll(AuthDTO authDTO);

}
