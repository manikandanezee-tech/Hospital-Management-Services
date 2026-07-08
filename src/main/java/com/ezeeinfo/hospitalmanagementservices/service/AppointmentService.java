package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;

public interface AppointmentService {
	
	AppointmentDTO save(AppointmentDTO appointmentDTO, AuthDTO authDTO)
			throws SQLException;

	AppointmentDTO getByCode(String code, AuthDTO authDTO);

	List<AppointmentDTO> getAll(AuthDTO authDTO);
	
	
	

}
