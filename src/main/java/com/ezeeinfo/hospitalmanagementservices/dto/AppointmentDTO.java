package com.ezeeinfo.hospitalmanagementservices.dto;

import java.time.LocalDateTime;

import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.AppointmentStatusEM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentDTO extends BaseDTO<AppointmentDTO> {

	private PatientDTO patientDTO;
	private DoctorDTO doctorDTO;
	private NamespaceDTO namespaceDTO;
	private LocalDateTime appointmentDateTime;
	private int tokenNumber;
	private AppointmentStatusEM status;

}
