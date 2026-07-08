package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PrescriptionItemDTO;

public interface PrescriptionItemService {

	PrescriptionItemDTO save(PrescriptionItemDTO prescriptionItemDTO, AuthDTO authDTO) throws SQLException;

	PrescriptionItemDTO getByCode(String code, AuthDTO authDTO);

	List<PrescriptionItemDTO> getAll(AuthDTO authDTO);

}
