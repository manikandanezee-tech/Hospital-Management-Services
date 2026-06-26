package com.ezeeinfo.hospitalmanagementservices.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import com.ezeeinfo.hospitalmanagementservices.dao.PatientDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.PatientDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class PatientDAO {
	@Autowired
	private NamespaceDAO namespaceDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PatientDAO.class);

	public PatientDTO save(PatientDTO patientDTO) {
		LOGGER.info(" SAVE - insertion / updation Patient on  process");
		String code = "";
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_PATIENT_IUD(?,?,?,?,?,?,?,?,?)}")) {
			callableStatement.setString(1, patientDTO.getCode());
			callableStatement.setString(2, patientDTO.getName());
			callableStatement.setInt(3, patientDTO.getNamespaceDTO().getId());
			callableStatement.setString(4, patientDTO.getMobile());
			callableStatement.setString(5, patientDTO.getGender());
			callableStatement.setString(6, patientDTO.getAddress());
			callableStatement.setInt(7, patientDTO.getActiveFlag());
			callableStatement.setInt(8, patientDTO.getUpdatedBy().getId());
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(9, Types.INTEGER);
			callableStatement.execute();
			code = callableStatement.getString(1);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		patientDTO.setCode(code);
		return patientDTO;
	}

	public List<PatientDTO> getAll(int namespaceId) {
		LOGGER.info(" GET-ALL - Patient lookup process for list");
		String query = "select id, name, code, namespace_id, mobile, gender, address, active_flag from patient where namespace_id=? and active_flag < 2";
		List<PatientDTO> patientList = new ArrayList<>();
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, namespaceId);
			NamespaceDTO namespaceDTO = namespaceDAO.getById(namespaceId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					PatientDTO patientDTO = new PatientDTO();
					patientDTO.setId(resultSet.getInt("id"));
					patientDTO.setCode(resultSet.getString("code"));
					patientDTO.setName(resultSet.getString("name"));
					patientDTO.setNamespaceDTO(namespaceDTO);
					patientDTO.setMobile(resultSet.getString("mobile"));
					patientDTO.setGender(resultSet.getString("gender"));
					patientDTO.setAddress(resultSet.getString("address"));
					patientDTO.setActiveFlag(resultSet.getInt("active_flag"));

					patientList.add(patientDTO);

				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return patientList;
	}

	public PatientDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Patient lookup process by code : {}",code);
		String query = "select id, name, code, namespace_id, mobile, gender, address, active_flag from patient where code=? and active_flag < 2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, code);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {
					PatientDTO patientDTO = new PatientDTO();
					patientDTO.setId(resultSet.getInt("id"));
					patientDTO.setCode(resultSet.getString("code"));
					patientDTO.setName(resultSet.getString("name"));
					patientDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					patientDTO.setMobile(resultSet.getString("mobile"));
					patientDTO.setGender(resultSet.getString("gender"));
					patientDTO.setAddress(resultSet.getString("address"));
					patientDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return patientDTO;
				}

			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Patient NotFound", HttpStatus.NOT_FOUND);
	}

	public PatientDTO getById(int id) throws SQLException {
		LOGGER.info(" GET-BY-CODE - Patient lookup process by id");
		String query = "select id, name, code, namespace_id, mobile, gender, address, active_flag from patient where id=? and active_flag < 2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {
					PatientDTO patientDTO = new PatientDTO();
					patientDTO.setId(resultSet.getInt("id"));
					patientDTO.setCode(resultSet.getString("code"));
					patientDTO.setName(resultSet.getString("name"));
					patientDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					patientDTO.setMobile(resultSet.getString("mobile"));
					patientDTO.setGender(resultSet.getString("gender"));
					patientDTO.setAddress(resultSet.getString("address"));
					patientDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return patientDTO;
				}

			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Patient NotFound...", HttpStatus.NOT_FOUND);
	}
}
