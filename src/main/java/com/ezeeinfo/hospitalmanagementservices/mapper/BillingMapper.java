package com.ezeeinfo.hospitalmanagementservices.mapper;

import com.ezeeinfo.hospitalmanagementservices.controller.io.AppointmentResponseIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.BillingIO;
import com.ezeeinfo.hospitalmanagementservices.controller.io.NamespaceIO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.BillingDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;

public class BillingMapper {
	
	public static BillingDTO toDTO(BillingIO billingIO, NamespaceDTO namespaceDTO, AppointmentDTO appointmentDTO) {
		BillingDTO billingDTO = new BillingDTO();
		billingDTO.setCode(billingIO.getCode());
		billingDTO.setNamespaceDTO(namespaceDTO);
		billingDTO.setAppointmentDTO(appointmentDTO);
		billingDTO.setActiveFlag(billingIO.getActiveFlag());
		return billingDTO;
	}
	
	public static BillingIO toIO(BillingDTO billingDTO) {
		
		NamespaceIO namespaceIO = NamespaceMapper.toIO(billingDTO.getNamespaceDTO());
		
		AppointmentResponseIO appointmentResponseIO = AppointmentMapper.toIO(billingDTO.getAppointmentDTO());
		
		BillingIO billingIO = new BillingIO();
		billingIO.setCode(billingDTO.getCode());
		billingIO.setNamespaceIO(namespaceIO);
		billingIO.setAppointmentResponseIO(appointmentResponseIO);
		billingIO.setTotalAmount(billingDTO.getTotalAmount());
		billingIO.setPaidAmount(billingDTO.getPaidAmount());
		billingIO.setBalanceAmount(billingDTO.getBalanceAmount());
		billingIO.setBillStatus(billingDTO.getBillStatus().getCode());
		billingIO.setActiveFlag(billingDTO.getActiveFlag());
		
		return billingIO;
	}

}
