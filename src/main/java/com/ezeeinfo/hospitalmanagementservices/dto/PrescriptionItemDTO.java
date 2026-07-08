package com.ezeeinfo.hospitalmanagementservices.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PrescriptionItemDTO extends BaseDTO<PrescriptionItemDTO>{
	
	private AppointmentDTO appointmentDTO;
	private MedicineDTO medicineDTO;
	private int quantity;
	private String notes;
	

}
