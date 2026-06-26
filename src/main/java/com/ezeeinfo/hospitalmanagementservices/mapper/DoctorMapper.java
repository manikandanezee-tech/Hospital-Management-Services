package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.DepartmentIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.DoctorIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.DoctorResponseIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.UserResponseIO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DoctorDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;

public class DoctorMapper {

	public static DoctorDTO toDTO(DoctorIO doctorIO, UserDTO userDTO, NamespaceDTO namespaceDTO, DepartmentDTO departmentDTO) {
		DoctorDTO doctorDTO = new DoctorDTO();
		doctorDTO.setCode(doctorIO.getCode());
		doctorDTO.setName(doctorIO.getName());
		doctorDTO.setNamespaceDTO(namespaceDTO);
		doctorDTO.setUserDTO(userDTO);
		doctorDTO.setDepartmentDTO(departmentDTO);
		doctorDTO.setEmail(doctorIO.getEmail());
		doctorDTO.setMobile(doctorIO.getMobile());
		doctorDTO.setSpecialization(doctorIO.getSpecialization());
		doctorDTO.setConsultationFee(doctorIO.getConsultationFee());
		doctorDTO.setActiveFlag(doctorIO.getActiveFlag());
		return doctorDTO;
	}
	
	public static DoctorResponseIO toIO(DoctorDTO doctorDTO) {
		
		DoctorResponseIO doctorResponseIO = new DoctorResponseIO();
		doctorResponseIO.setCode(doctorDTO.getCode());
		doctorResponseIO.setName(doctorDTO.getName());
		doctorResponseIO.setNamespaceCode(doctorDTO.getNamespaceDTO().getCode());
		doctorResponseIO.setUserCode(doctorDTO.getUserDTO().getCode());
		doctorResponseIO.setDepartmentCode(doctorDTO.getDepartmentDTO().getCode());
		doctorResponseIO.setMobile(doctorDTO.getMobile());
		doctorResponseIO.setEmail(doctorDTO.getEmail());
		doctorResponseIO.setSpecialization(doctorDTO.getSpecialization());
		doctorResponseIO.setConsultationFee(doctorDTO.getConsultationFee());
		doctorResponseIO.setActiveFlag(doctorDTO.getActiveFlag());
		return doctorResponseIO;
	}
	public static DoctorIO toIo(DoctorDTO doctorDTO) {
		NamespaceIO namespaceIO = NamespaceMapper.toIO(doctorDTO.getNamespaceDTO());
		DepartmentIO departmentIO = DepartmentMapper.toIO(doctorDTO.getDepartmentDTO());
		UserResponseIO userResponseIO = UserMapper.toIO(doctorDTO.getUserDTO());
		
		DoctorIO doctorIO = new DoctorIO();
		doctorIO.setCode(doctorDTO.getCode());
		doctorIO.setName(doctorDTO.getName());
		doctorIO.setNamespaceIO(namespaceIO);
		doctorIO.setDepartmentIO(departmentIO);
		doctorIO.setMobile(doctorDTO.getMobile());
		doctorIO.setEmail(doctorDTO.getEmail());
		doctorIO.setSpecialization(doctorDTO.getSpecialization());
		doctorIO.setConsultationFee(doctorDTO.getConsultationFee());
		doctorIO.setUserResponseIO(userResponseIO);
		doctorIO.setActiveFlag(doctorDTO.getActiveFlag());
		return doctorIO;
	}
}
