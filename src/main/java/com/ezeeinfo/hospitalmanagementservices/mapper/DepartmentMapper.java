package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.DepartmentIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;

public class DepartmentMapper {

	public static DepartmentDTO toDTO(DepartmentIO departmentIO, NamespaceDTO namespaceDTO) {
		
		DepartmentDTO departmentDTO = new DepartmentDTO();
		departmentDTO.setCode(departmentIO.getCode());
		departmentDTO.setName(departmentIO.getName());
		departmentDTO.setNamespaceDTO(namespaceDTO);
		departmentDTO.setActiveFlag(departmentIO.getActiveFlag());
		return departmentDTO;
	}
	
	public static DepartmentIO toIO(DepartmentDTO departmentDTO) {
		DepartmentIO departmentIO = new DepartmentIO();
		departmentIO.setCode(departmentDTO.getCode());
		departmentIO.setName(departmentDTO.getName());
		
		NamespaceIO namespaceIO = NamespaceMapper.toIO(departmentDTO.getNamespaceDTO());
		departmentIO.setNamespaceIO(namespaceIO);
		departmentIO.setActiveFlag(departmentDTO.getActiveFlag());
		return departmentIO;
	}
}
