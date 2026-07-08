package com.ezeeinfo.hospitalmanagementservices.service;

import java.sql.SQLException;
import java.util.List;


import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;

public interface MedicineService {

	MedicineDTO save(MedicineDTO medicineDTO, AuthDTO authDTO) throws SQLException;

	List<MedicineDTO> getAll(AuthDTO authDTO);

	MedicineDTO getByCode(String code, AuthDTO authDTO);

	
}
