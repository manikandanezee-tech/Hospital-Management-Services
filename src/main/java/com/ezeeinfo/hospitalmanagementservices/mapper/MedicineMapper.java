package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.MedicineIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;

public class MedicineMapper {

	public static MedicineDTO toDTO(MedicineIO medicineIO, NamespaceDTO namespaceDTO) {
		MedicineDTO medicineDTO=new MedicineDTO();
		medicineDTO.setCode(medicineIO.getCode());
		medicineDTO.setName(medicineIO.getName());
		medicineDTO.setNamespaceDTO(namespaceDTO);
		medicineDTO.setPrice(medicineIO.getPrice());
		medicineDTO.setCurrentStock(medicineIO.getCurrentStock());
		medicineDTO.setSupplier(medicineIO.getSupplier());
		medicineDTO.setActiveFlag(medicineIO.getActiveFlag());
		return medicineDTO;
	}
	public static MedicineIO toIO(MedicineDTO medicineDTO) {
		
		NamespaceIO namespaceIO = NamespaceMapper.toIO(medicineDTO.getNamespaceDTO());
		
		MedicineIO medicineIO=new MedicineIO();
		medicineIO.setCode(medicineDTO.getCode());
		medicineIO.setName(medicineDTO.getName());
		medicineIO.setNamespaceIO(namespaceIO);
		medicineIO.setPrice(medicineDTO.getPrice());
		medicineIO.setCurrentStock(medicineDTO.getCurrentStock());
		medicineIO.setSupplier(medicineDTO.getSupplier());
		medicineIO.setActiveFlag(medicineDTO.getActiveFlag());
		return medicineIO;
	}
}
