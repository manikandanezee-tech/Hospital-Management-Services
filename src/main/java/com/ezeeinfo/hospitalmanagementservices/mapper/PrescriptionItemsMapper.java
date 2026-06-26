package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.AppointmentResponseIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.MedicineIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.PrescriptionItemIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PrescriptionItemDTO;

public class PrescriptionItemsMapper {

	public static PrescriptionItemDTO toDTO(PrescriptionItemIO prescriptionItemIO, AppointmentDTO appointmentDTO, MedicineDTO medicineDTO) {
		
		PrescriptionItemDTO prescriptionItemDTO = new PrescriptionItemDTO();
		prescriptionItemDTO.setCode(prescriptionItemIO.getCode());
		prescriptionItemDTO.setAppointmentDTO(appointmentDTO);
		prescriptionItemDTO.setMedicineDTO(medicineDTO);
		prescriptionItemDTO.setQuantity(prescriptionItemIO.getQuantity());
		prescriptionItemDTO.setNotes(prescriptionItemIO.getNotes());
		prescriptionItemDTO.setActiveFlag(prescriptionItemIO.getActiveFlag());
		return prescriptionItemDTO;
	}
	
	public static PrescriptionItemIO toIO(PrescriptionItemDTO prescriptionItemDTO) {
		
		AppointmentResponseIO appointmentResponseIO = AppointmentMapper.toIO(prescriptionItemDTO.getAppointmentDTO());
		MedicineIO medicineIO = MedicineMapper.toIO(prescriptionItemDTO.getMedicineDTO());
		
		PrescriptionItemIO prescriptionItemIO = new PrescriptionItemIO();
		prescriptionItemIO.setCode(prescriptionItemDTO.getCode());
		prescriptionItemIO.setAppointmentResponseIO(appointmentResponseIO);
		prescriptionItemIO.setMedicineIO(medicineIO);
		prescriptionItemIO.setQuantity(prescriptionItemDTO.getQuantity());
		prescriptionItemIO.setNotes(prescriptionItemDTO.getNotes());
		prescriptionItemIO.setActiveFlag(prescriptionItemDTO.getActiveFlag());
		return prescriptionItemIO;
	}
}
