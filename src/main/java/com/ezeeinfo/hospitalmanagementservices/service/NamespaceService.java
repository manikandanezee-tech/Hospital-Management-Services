package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;


public interface NamespaceService {
	
	List<NamespaceDTO>getAll(AuthDTO authDTO);
	
	NamespaceDTO getByCode(AuthDTO authDTO, String code);
	
	NamespaceDTO save(NamespaceDTO namespaceDTO, AuthDTO authDTO) throws SQLException;
	
	



}
