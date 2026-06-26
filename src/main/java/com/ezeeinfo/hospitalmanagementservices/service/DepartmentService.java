package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;

public interface DepartmentService {
  
	DepartmentDTO save(DepartmentDTO departmentDTO,HttpServletRequest request) throws SQLException;
	
	DepartmentDTO getByCode(String code);
	
	List<DepartmentDTO> getAll(HttpServletRequest request);
}
