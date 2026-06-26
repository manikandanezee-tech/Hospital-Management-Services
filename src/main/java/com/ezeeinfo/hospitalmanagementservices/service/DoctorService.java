package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.DoctorDTO;

public interface DoctorService {

	DoctorDTO save(DoctorDTO doctorDTO,HttpServletRequest request) throws SQLException;

	DoctorDTO getByCode(String code);
	
	List<DoctorDTO>getAll(HttpServletRequest request) throws SQLException;

}
