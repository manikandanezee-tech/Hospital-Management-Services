package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.AppointmentIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.AppointmentResponseIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.DoctorIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.PatientIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.DoctorDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PatientDTO;

public class AppointmentMapper {
	
	public static AppointmentDTO toDTO(AppointmentIO appointmentIO, NamespaceDTO namespaceDTO, DoctorDTO doctorDTO, PatientDTO patientDTO) {

		AppointmentDTO appointmentDTO = new AppointmentDTO();

		appointmentDTO.setCode(appointmentIO.getCode());
		appointmentDTO.setNamespaceDTO(namespaceDTO);
		appointmentDTO.setDoctorDTO(doctorDTO);
		appointmentDTO.setPatientDTO(patientDTO);
		appointmentDTO.setAppointmentDateTime(appointmentIO.getAppointmentDateTime());
		appointmentDTO.setActiveFlag(appointmentIO.getActiveFlag());
		return appointmentDTO;

	}

	public static AppointmentResponseIO toIO(AppointmentDTO appointmentDTO) {
		NamespaceIO namespaceIO = NamespaceMapper.toIO(appointmentDTO.getNamespaceDTO());
		
		DoctorIO doctorIO = DoctorMapper.toIo(appointmentDTO.getDoctorDTO());
		
		PatientIO patientIO = PatientMapper.toIO(appointmentDTO.getPatientDTO());
		
		AppointmentResponseIO appointmentResponseIO = new AppointmentResponseIO();

		appointmentResponseIO.setCode(appointmentDTO.getCode());
		appointmentResponseIO.setNamespaceIO(namespaceIO);
		appointmentResponseIO.setDoctorIO(doctorIO);
		appointmentResponseIO.setPatientIO(patientIO);
		appointmentResponseIO.setAppointmentDateTime(appointmentDTO.getAppointmentDateTime());
		appointmentResponseIO.setTokenNumber(appointmentDTO.getTokenNumber());
		appointmentResponseIO.setStatus(appointmentDTO.getStatus().getCode());
		appointmentResponseIO.setActiveFlag(appointmentDTO.getActiveFlag());
		return appointmentResponseIO;
	}
}