package com.ezeeinfo.hospitalmanagementservices.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.ezeeinfo.hospitalmanagementservices.dao.AppointmentDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AppointmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.AppointmentStatusEM;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class AppointmentDAO {
	@Autowired
	private NamespaceDAO namespaceDAO;
	@Autowired
	private DoctorDAO doctorDAO;
	@Autowired
	private PatientDAO patientDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentDAO.class);

	public AppointmentDTO save(AppointmentDTO appointmentDTO) {
		LOGGER.info(" SAVE - Appointment insert/update on process ");

		String code = "";
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_APPOINTMENT_IUD(?,?,?,?,?,?,?,?,?,?)}");) {
			callableStatement.setString(1, appointmentDTO.getCode());
			callableStatement.setInt(2, appointmentDTO.getPatientDTO().getId());
			callableStatement.setInt(3, appointmentDTO.getDoctorDTO().getId());
			callableStatement.setInt(4, appointmentDTO.getNamespaceDTO().getId());
			callableStatement.setTimestamp(5, Timestamp.valueOf(appointmentDTO.getAppointmentDateTime()));
			callableStatement.setInt(6, appointmentDTO.getTokenNumber());
			callableStatement.setInt(7, appointmentDTO.getStatus().getValue());
			callableStatement.setInt(8, appointmentDTO.getActiveFlag());
			callableStatement.setInt(9, appointmentDTO.getUpdatedBy().getId());
			callableStatement.execute();
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(10, Types.INTEGER);

			code = callableStatement.getString(1);

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		appointmentDTO.setCode(code);
		return appointmentDTO;
	}

	public int getAppointmentCountOnDate(int doctorId, LocalDateTime appointmentDateTime, int namespaceId) {
		LOGGER.info(" GET-BY-DATE - Appointment lookup by DATE,DOCTOR on process");
		int appointmentCount = 0;
		LocalDateTime start = appointmentDateTime.toLocalDate().atStartOfDay();
		LocalDateTime end = appointmentDateTime.toLocalDate().plusDays(1).atStartOfDay();
		String query = "select Count(*) total_appointments from appointment where namespace_id=? and doctor_id=? and appointment_datetime>=? " + "and appointment_datetime<? and active_flag=1";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, namespaceId);
			preparedStatement.setInt(2, doctorId);
			preparedStatement.setTimestamp(3, Timestamp.valueOf(start));
			preparedStatement.setTimestamp(4, Timestamp.valueOf(end));
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					appointmentCount = resultSet.getInt("total_appointments");
				}
			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return appointmentCount;
	}

	public AppointmentDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Appointment lookup by code on process");
		String query = "select id, code, namespace_id, patient_id, doctor_id, appointment_datetime, token_number, status, active_flag from appointment where code=? and active_flag = 1";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, code);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					AppointmentDTO appointmentDTO = new AppointmentDTO();
					appointmentDTO.setId(resultSet.getInt("id"));
					appointmentDTO.setCode(resultSet.getString("code"));
					appointmentDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					appointmentDTO.setPatientDTO(patientDAO.getById(resultSet.getInt("patient_id")));
					appointmentDTO.setDoctorDTO(doctorDAO.getById(resultSet.getInt("doctor_id")));
					appointmentDTO.setAppointmentDateTime(resultSet.getTimestamp("appointment_datetime").toLocalDateTime());
					appointmentDTO.setTokenNumber(resultSet.getInt("token_number"));
					appointmentDTO.setStatus(AppointmentStatusEM.getById(resultSet.getInt("status")));
					appointmentDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return appointmentDTO;
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Appointment Not Found", HttpStatus.NOT_FOUND);
	}

	public List<AppointmentDTO> getAll(int namespaceId) {
		LOGGER.info(" GET-ALL - Appointment lookup process on");
		List<AppointmentDTO> appointmentDTOList = new ArrayList<AppointmentDTO>();
		String query = "select id, code, namespace_id, patient_id, doctor_id, appointment_datetime, token_number, status, active_flag from appointment where namespace_id=? and active_flag =1";

		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, namespaceId);
			NamespaceDTO namespaceDTO = namespaceDAO.getById(namespaceId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					AppointmentDTO appointmentDTO = new AppointmentDTO();
					appointmentDTO.setId(resultSet.getInt("id"));
					appointmentDTO.setCode(resultSet.getString("code"));
					appointmentDTO.setNamespaceDTO(namespaceDTO);
					appointmentDTO.setPatientDTO(patientDAO.getById(resultSet.getInt("patient_id")));
					appointmentDTO.setDoctorDTO(doctorDAO.getById(resultSet.getInt("doctor_id")));
					appointmentDTO.setAppointmentDateTime(resultSet.getTimestamp("appointment_datetime").toLocalDateTime());
					appointmentDTO.setId(resultSet.getInt("token_number"));
					appointmentDTO.setStatus(AppointmentStatusEM.getById(resultSet.getInt("status")));
					appointmentDTO.setActiveFlag(resultSet.getInt("active_flag"));
					appointmentDTOList.add(appointmentDTO);
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return appointmentDTOList;
	}

	public AppointmentDTO getById(int id) {
		LOGGER.info(" GET-BY-ID - Appointment lookup process on");
		String query = "select id, code, namespace_id, patient_id, doctor_id, appointment_datetime, token_number, status, active_flag from appointment where id=? and active_flag =1";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					AppointmentDTO appointmentDTO = new AppointmentDTO();
					appointmentDTO.setId(resultSet.getInt("id"));
					appointmentDTO.setCode(resultSet.getString("code"));
					appointmentDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					appointmentDTO.setPatientDTO(patientDAO.getById(resultSet.getInt("patient_id")));
					appointmentDTO.setDoctorDTO(doctorDAO.getById(resultSet.getInt("doctor_id")));
					appointmentDTO.setAppointmentDateTime(resultSet.getTimestamp("appointment_datetime").toLocalDateTime());
					appointmentDTO.setTokenNumber(resultSet.getInt("token_number"));
					appointmentDTO.setStatus(AppointmentStatusEM.getById(resultSet.getInt("status")));
					appointmentDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return appointmentDTO;
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Appointment Not Found", HttpStatus.NOT_FOUND);
	}

	public AppointmentDTO doctorsTotalAppointments(AppointmentDTO appointmentDTO) throws SQLException {

		String query = "select id from appointment where doctor_id = ? " + "and appointment_datetime < ? and status = 1 and active_flag =1 limit 1";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, appointmentDTO.getDoctorDTO().getId());
			preparedStatement.setTimestamp(2, Timestamp.valueOf(appointmentDTO.getAppointmentDateTime()));

			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					AppointmentDTO appointmentDTO2 = new AppointmentDTO();
					appointmentDTO2.setId(resultSet.getInt("id"));
					return appointmentDTO2;
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
