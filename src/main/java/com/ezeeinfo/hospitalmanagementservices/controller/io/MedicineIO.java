package com.ezeeinfo.hospitalmanagementservices.controller.io;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicineIO extends BaseIO {

	private NamespaceIO namespaceIO;
	private BigDecimal price;
	private Integer currentStock;
	private String supplier;
	
}
