package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.AppointmentResponseIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.ConsultationIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;

public class ConsultationMapper {

	public static ConsultationDTO toDTO(ConsultationIO consultationIO, AppointmentDTO appointmentDTO) {
		ConsultationDTO consultationDTO = new ConsultationDTO();
		consultationDTO.setCode(consultationIO.getCode());
		consultationDTO.setAppointmentDTO(appointmentDTO);
		consultationDTO.setChiefComplaint(consultationIO.getChiefComplaint());
		consultationDTO.setDiagnosis(consultationIO.getDiagnosis());
		consultationDTO.setDoctorNotes(consultationIO.getDoctorNotes());
		consultationDTO.setConsultationDateTime(consultationIO.getConsultationDateTime());
		consultationDTO.setActiveFlag(consultationIO.getActiveFlag());
		return consultationDTO;
	}

	public static ConsultationIO toIO(ConsultationDTO consultationDTO) {

		AppointmentResponseIO appointmentResponseIO = AppointmentMapper.toIO(consultationDTO.getAppointmentDTO());

		ConsultationIO consultationIO = new ConsultationIO();
		consultationIO.setCode(consultationDTO.getCode());
		consultationIO.setAppointmentResponseIO(appointmentResponseIO);
		consultationIO.setChiefComplaint(consultationDTO.getChiefComplaint());
		consultationIO.setConsultationDateTime(consultationDTO.getConsultationDateTime());
		consultationIO.setDiagnosis(consultationDTO.getDiagnosis());
		consultationIO.setDoctorNotes(consultationDTO.getDoctorNotes());
		return consultationIO;
	}
}
