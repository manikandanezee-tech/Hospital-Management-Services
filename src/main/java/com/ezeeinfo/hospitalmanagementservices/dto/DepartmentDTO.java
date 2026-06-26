package com.ezeeinfo.hospitalmanagementservices.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DepartmentDTO extends BaseDTO {

	private NamespaceDTO namespaceDTO;
}
