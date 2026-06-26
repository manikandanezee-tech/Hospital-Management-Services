package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.PatientIO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PatientDTO;

public class PatientMapper {

	public static PatientDTO toDTO(PatientIO patientIO, NamespaceDTO namespaceDTO) {
		PatientDTO patientDTO = new PatientDTO();
		patientDTO.setCode(patientIO.getCode());
		patientDTO.setName(patientIO.getName());
		patientDTO.setNamespaceDTO(namespaceDTO);
		patientDTO.setMobile(patientIO.getMobile());
		patientDTO.setGender(patientIO.getGender());
		patientDTO.setAddress(patientIO.getAddress());
		patientDTO.setActiveFlag(patientIO.getActiveFlag());
		return patientDTO;
	}
	public static PatientIO toIO(PatientDTO patientDTO) {
		PatientIO patientIO = new PatientIO();
		patientIO.setCode(patientDTO.getCode());
		patientIO.setName(patientDTO.getName());

		NamespaceIO namespaceIO = NamespaceMapper.toIO(patientDTO.getNamespaceDTO());

		patientIO.setNamespaceIO(namespaceIO);
		patientIO.setMobile(patientDTO.getMobile());
		patientIO.setGender(patientDTO.getGender());
		patientIO.setAddress(patientDTO.getAddress());
		patientIO.setActiveFlag(patientDTO.getActiveFlag());

		return patientIO;
	}
}
