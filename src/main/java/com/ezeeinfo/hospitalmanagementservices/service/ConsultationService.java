package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;

public interface ConsultationService {

	ConsultationDTO save(ConsultationDTO consultationDTO, HttpServletRequest request) throws SQLException;

	ConsultationDTO getByCode(String code);

	List<ConsultationDTO> getAll(HttpServletRequest request);

}
