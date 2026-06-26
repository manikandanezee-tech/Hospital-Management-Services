package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;


public interface NamespaceService {
	
	List<NamespaceDTO>getAll();
	
	NamespaceDTO getByCode(String code);
	
	NamespaceDTO save(NamespaceDTO namespaceDTO,HttpServletRequest request) throws SQLException;
	
	



}
