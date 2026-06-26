package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;

public interface AppointmentService {
	
	AppointmentDTO save(AppointmentDTO appointmentDTO,HttpServletRequest request)
			throws SQLException;

	AppointmentDTO getByCode(String code);

	List<AppointmentDTO> getAll(HttpServletRequest request);
	
	
	

}
