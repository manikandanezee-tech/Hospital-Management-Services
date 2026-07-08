package com.ezeeinfo.hospitalmanagementservices.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicineDTO extends BaseDTO<MedicineDTO>{
	 
	private NamespaceDTO namespaceDTO;
	private BigDecimal price;
	private Integer currentStock;
	private String supplier;

}
