package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DoctorDTO;

public interface DoctorService {

	DoctorDTO save(DoctorDTO doctorDTO, AuthDTO authDTO) throws SQLException;

	DoctorDTO getByCode(String code, AuthDTO authDTO);
	
	List<DoctorDTO>getAll(AuthDTO authDTO) throws SQLException;

}
