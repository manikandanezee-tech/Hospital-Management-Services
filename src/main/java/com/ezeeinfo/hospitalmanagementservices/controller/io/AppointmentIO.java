package com.ezeeinfo.hospitalmanagementservices.controller.io;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentIO extends BaseIO{
	private NamespaceIO namespaceIO;
	private DoctorIO doctorIO;
	private PatientIO patientIO;
	private LocalDateTime appointmentDateTime;
}
