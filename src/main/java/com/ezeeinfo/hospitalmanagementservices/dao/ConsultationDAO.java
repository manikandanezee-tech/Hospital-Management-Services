package com.ezeeinfo.hospitalmanagementservices.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.ezeeinfo.hospitalmanagementservices.dto.ConsultationDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class ConsultationDAO {
	@Autowired
	private AppointmentDAO appointmentDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsultationDAO.class);

	public ConsultationDTO save(ConsultationDTO consultationDTO) {
		LOGGER.info(" SAVE - Consultation insert/update on process ");
		String code="";
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_CONSULTATION_IUD(?,?,?,?,?,?,?,?,?)}");) {

			// inout parameter for pcrCode
			callableStatement.setString(1, consultationDTO.getCode());
			callableStatement.setInt(2, consultationDTO.getAppointmentDTO().getId());
			callableStatement.setString(3, consultationDTO.getChiefComplaint());
			callableStatement.setString(4, consultationDTO.getDiagnosis());
			callableStatement.setString(5, consultationDTO.getDoctorNotes());
			callableStatement.setTimestamp(6, Timestamp.valueOf(consultationDTO.getConsultationDateTime()));
			callableStatement.setInt(7, consultationDTO.getActiveFlag());
			callableStatement.setInt(8, consultationDTO.getUpdatedBy().getId());
			// out parameters (code, setRowCount)
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(9, Types.INTEGER);
			callableStatement.execute();
		code=callableStatement.getString(1);

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		consultationDTO.setCode(code);
		return consultationDTO;
	}

	public ConsultationDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Consultation lookup by code on process");
		String query="select id, code, appointment_id, chief_complaint, diagnosis, doctor_notes, consultation_datetime, active_flag from consultations where code = ? and active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, code);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					ConsultationDTO consultationDTO=new ConsultationDTO();
					consultationDTO.setId(resultSet.getInt("id"));
					consultationDTO.setCode(resultSet.getString("code"));
					consultationDTO.setAppointmentDTO(appointmentDAO.getById(resultSet.getInt("appointment_id")));
					consultationDTO.setChiefComplaint(resultSet.getString("chief_complaint"));
					consultationDTO.setDiagnosis(resultSet.getString("diagnosis"));
					consultationDTO.setDoctorNotes(resultSet.getString("doctor_notes"));
					consultationDTO.setConsultationDateTime(resultSet.getTimestamp("consultation_datetime").toLocalDateTime());
					consultationDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return consultationDTO;
				}
			}
			}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Consultation Not Found", HttpStatus.NOT_FOUND);
	}

	public List<ConsultationDTO> getAll(Integer namespaceId) {
		LOGGER.info(" GET-ALL - Consultation lookup process on");
		String query="select c.id, c.code, c.appointment_id, c.chief_complaint, c.diagnosis, c.doctor_notes, c.consultation_datetime, c.active_flag from consultations c inner join appointments a on c.appointment_id=a.id where a.namespace_id = ? and c.active_flag <2";
		List<ConsultationDTO> consultationDTOList=new ArrayList<>();
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
		preparedStatement.setInt(1, namespaceId);
		try (ResultSet resultSet = preparedStatement.executeQuery();) {
			if (resultSet.next()) {
				ConsultationDTO consultationDTO=new ConsultationDTO();
				consultationDTO.setId(resultSet.getInt("id"));
				consultationDTO.setCode(resultSet.getString("code"));
				consultationDTO.setAppointmentDTO(appointmentDAO.getById(resultSet.getInt("appointment_id")));
				consultationDTO.setChiefComplaint(resultSet.getString("chief_complaint"));
				consultationDTO.setDiagnosis(resultSet.getString("diagnosis"));
				consultationDTO.setDoctorNotes(resultSet.getString("doctor_notes"));
				consultationDTO.setConsultationDateTime(resultSet.getTimestamp("consultation_datetime").toLocalDateTime());
				consultationDTO.setActiveFlag(resultSet.getInt("active_flag"));
				consultationDTOList.add(consultationDTO);
			}
		}
		}
	catch (Exception e) {
		throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return consultationDTOList;
}
	
	public ConsultationDTO isAlreayConsulted(int appointmentId) {
		LOGGER.info(" GET-BY-APPOINTMENT - Department lookup process on using appointment");
		String query="select id, code, appointment_id, chief_complaint, diagnosis, doctor_notes, consultation_datetime from consultations where appointment_id=?";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, appointmentId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					ConsultationDTO consultationDTO=new ConsultationDTO();
					consultationDTO.setId(resultSet.getInt("id"));
					consultationDTO.setCode(resultSet.getString("code"));
					return consultationDTO;
				}
			}
			}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	}
