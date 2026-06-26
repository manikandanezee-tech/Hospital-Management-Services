package com.ezeeinfo.hospitalmanagementservices.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PrescriptionItemDTO extends BaseDTO{
	
	private AppointmentDTO appointmentDTO;
	private MedicineDTO medicineDTO;
	private int quantity;
	private String notes;
	

}
