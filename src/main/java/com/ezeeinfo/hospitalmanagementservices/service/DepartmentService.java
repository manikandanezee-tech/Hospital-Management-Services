package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;

public interface DepartmentService {
  
	DepartmentDTO save(DepartmentDTO departmentDTO,AuthDTO authDTO) throws SQLException;
	
	DepartmentDTO getByCode(String code,AuthDTO authDTO);
	
	List<DepartmentDTO> getAll(AuthDTO authDTO);
}
