package com.ezeeinfo.hospitalmanagementservices.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ConsultationDTO extends BaseDTO<ConsultationDTO>{

	private AppointmentDTO appointmentDTO;
	private String chiefComplaint;
	private String diagnosis;
	private String doctorNotes;
	private LocalDateTime consultationDateTime;
	
}
