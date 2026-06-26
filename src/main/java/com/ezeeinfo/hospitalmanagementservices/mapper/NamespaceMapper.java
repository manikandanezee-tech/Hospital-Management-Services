package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;

public class NamespaceMapper {

	public static NamespaceIO toIO(NamespaceDTO namespaceDTO) {

		NamespaceIO namespaceIO = new NamespaceIO();
		namespaceIO.setCode(namespaceDTO.getCode());
		namespaceIO.setName(namespaceDTO.getName());
		namespaceIO.setAddress(namespaceDTO.getAddress());
		namespaceIO.setActiveFlag(namespaceDTO.getActiveFlag());

		return namespaceIO;
	}
	
	public static NamespaceDTO toDTO(NamespaceIO namespaceIO) {
		NamespaceDTO namespaceDTO = new NamespaceDTO();
		namespaceDTO.setCode(namespaceIO.getCode());
		namespaceDTO.setName(namespaceIO.getName());
		namespaceDTO.setAddress(namespaceIO.getAddress());
		namespaceDTO.setActiveFlag(namespaceIO.getActiveFlag());
		return namespaceDTO;
	}
}
