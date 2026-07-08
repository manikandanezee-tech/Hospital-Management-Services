package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;

public interface ConsultationService {

	ConsultationDTO save(ConsultationDTO consultationDTO, AuthDTO authDTO) throws SQLException;

	ConsultationDTO getByCode(String code, AuthDTO authDTO);

	List<ConsultationDTO> getAll(AuthDTO authDTO);

}
