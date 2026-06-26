package com.ezeeinfo.hospitalmanagementservices.controller.io;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ConsultationIO  {

	private String code;
	private AppointmentResponseIO appointmentResponseIO;
	private String chiefComplaint;
	private String diagnosis;
	private String doctorNotes;
	private LocalDateTime consultationDateTime;
	private int activeFlag;
}
