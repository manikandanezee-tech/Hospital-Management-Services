package com.ezeeinfo.hospitalmanagementservices.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.ezeeinfo.hospitalmanagementservices.dto.PrescriptionItemDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class PrescriptionItemDAO {
	@Autowired
	private AppointmentDAO appointmentDAO;
	@Autowired
	private MedicineDAO medicineDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrescriptionItemDAO.class);


	public PrescriptionItemDTO save(PrescriptionItemDTO prescriptionItemDTO) {
		LOGGER.info(" SAVE - insertion / updation prescription on  process");
		String code = "";
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_PRESCRIPTION_IUD(?,?,?,?,?,?,?,?)}");) {

			// inout parameter for pcrCode
			callableStatement.setString(1, prescriptionItemDTO.getCode());
			callableStatement.setInt(2, prescriptionItemDTO.getAppointmentDTO().getId());
			callableStatement.setInt(3, prescriptionItemDTO.getMedicineDTO().getId());
			callableStatement.setString(4, prescriptionItemDTO.getNotes());
			callableStatement.setInt(5, prescriptionItemDTO.getQuantity());
			callableStatement.setInt(6, prescriptionItemDTO.getActiveFlag());
			callableStatement.setInt(7, prescriptionItemDTO.getUpdatedBy().getId());
			// out parameters (code, setRowCount)
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(8, Types.INTEGER);
			callableStatement.execute();
			code = callableStatement.getString(1);

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		prescriptionItemDTO.setCode(code);
		return prescriptionItemDTO;
	}

	public PrescriptionItemDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - prescription lookup process by code : {}",code);
		String query = "select id, code, appointment_id, medicine_id, notes, quantity, active_flag from prescription_item where code =? and active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, code);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					PrescriptionItemDTO prescriptionItemDTO = new PrescriptionItemDTO();
					prescriptionItemDTO.setId(resultSet.getInt("id"));
					prescriptionItemDTO.setCode(resultSet.getString("code"));
					prescriptionItemDTO.setAppointmentDTO(appointmentDAO.getById(resultSet.getInt("appointment_id")));
					prescriptionItemDTO.setMedicineDTO(medicineDAO.getById(resultSet.getInt("medicine_id")));
					prescriptionItemDTO.setNotes(resultSet.getString("notes"));
					prescriptionItemDTO.setQuantity(resultSet.getInt("quantity"));
					prescriptionItemDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return prescriptionItemDTO;
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Prescription Not Found", HttpStatus.NOT_FOUND);
	}

	public List<PrescriptionItemDTO> getAll(Integer namespaceId) {
		LOGGER.info(" GET-ALL - prescription lookup process for list");
		List<PrescriptionItemDTO> prescriptionItemDTOList = new ArrayList<PrescriptionItemDTO>();
		String query = "select p.id, p.code, p.appointment_id, p.medicine_id, p.notes, p.quantity, p.active_flag from prescription_item p inner join appointments a on p.appointment_id = a.id where a.namespace_id =? and p.active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, namespaceId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					PrescriptionItemDTO prescriptionItemDTO = new PrescriptionItemDTO();
					prescriptionItemDTO.setId(resultSet.getInt("id"));
					prescriptionItemDTO.setCode(resultSet.getString("code"));
					prescriptionItemDTO.setAppointmentDTO(appointmentDAO.getById(resultSet.getInt("appointment_id")));
					prescriptionItemDTO.setMedicineDTO(medicineDAO.getById(resultSet.getInt("medicine_id")));
					prescriptionItemDTO.setNotes(resultSet.getString("notes"));
					prescriptionItemDTO.setQuantity(resultSet.getInt("quantity"));
					prescriptionItemDTO.setActiveFlag(resultSet.getInt("active_flag"));
					prescriptionItemDTOList.add(prescriptionItemDTO);
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return prescriptionItemDTOList;
	}
	
	public List<PrescriptionItemDTO> getAllByAppointment(Integer appointmetnId) {
		LOGGER.info(" GET-ALL - prescription lookup process for getAll by appointment ");
		List<PrescriptionItemDTO> prescriptionItemDTOList = new ArrayList<PrescriptionItemDTO>();
		String query = "select id, code, appointment_id, medicine_id, notes, quantity, active_flag from prescription_item where appointment_id =? and active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, appointmetnId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					PrescriptionItemDTO prescriptionItemDTO = new PrescriptionItemDTO();
					prescriptionItemDTO.setId(resultSet.getInt("id"));
					prescriptionItemDTO.setCode(resultSet.getString("code"));
					prescriptionItemDTO.setAppointmentDTO(appointmentDAO.getById(resultSet.getInt("appointment_id")));
					prescriptionItemDTO.setMedicineDTO(medicineDAO.getById(resultSet.getInt("medicine_id")));
					prescriptionItemDTO.setNotes(resultSet.getString("notes"));
					prescriptionItemDTO.setQuantity(resultSet.getInt("quantity"));
					prescriptionItemDTO.setActiveFlag(resultSet.getInt("active_flag"));
					prescriptionItemDTOList.add(prescriptionItemDTO);
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return prescriptionItemDTOList;
	}

}
