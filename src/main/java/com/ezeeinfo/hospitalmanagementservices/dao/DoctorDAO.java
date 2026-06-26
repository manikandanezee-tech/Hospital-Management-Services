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
import com.ezeeinfo.hospitalmanagementservices.dto.DoctorDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class DoctorDAO {

	@Autowired
	private DepartmentDAO departmentDAO;
	@Autowired
	private NamespaceDAO namespaceDAO;
	@Autowired
	private UserDAO userDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDAO.class);

	public DoctorDTO save(DoctorDTO doctorDTO, int userId, int namespaceId, int departmentId) {
		LOGGER.info(" SAVE - insertion / updation process on doctor");
		String code="";
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_DOCTOR_IUD(?,?,?,?,?,?,?,?,?,?,?,?)}")) {
			callableStatement.setString(1, doctorDTO.getCode());
			callableStatement.setString(2, doctorDTO.getName());
			callableStatement.setInt(3, userId);
			callableStatement.setInt(4, departmentId);
			callableStatement.setInt(5, namespaceId);
			callableStatement.setString(6, doctorDTO.getMobile());
			callableStatement.setString(7, doctorDTO.getEmail());
			callableStatement.setString(8, doctorDTO.getSpecialization());
			callableStatement.setBigDecimal(9, doctorDTO.getConsultationFee());
			callableStatement.setInt(10, doctorDTO.getActiveFlag());
			callableStatement.setInt(11, doctorDTO.getUpdatedBy().getId());

			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(12, Types.INTEGER);
			callableStatement.execute();
			code = callableStatement.getString(1);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		doctorDTO.setCode(code);
		return doctorDTO;
	}

	public DoctorDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Doctor lookup process on code : {}",code);
		String query = "select id, code, name, namespace_id, user_id, department_id, mobile, email, specialization, consultation_fee, active_flag from doctor where code=? and active_flag <2";
		DoctorDTO doctorDTO = new DoctorDTO();
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, code);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					doctorDTO.setId(resultSet.getInt("id"));
					doctorDTO.setCode(resultSet.getString("code"));
					doctorDTO.setName(resultSet.getString("name"));
					doctorDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					doctorDTO.setUserDTO(userDAO.getById(resultSet.getInt("user_id")));
					doctorDTO.setDepartmentDTO(departmentDAO.getById(resultSet.getInt("department_id")));
					doctorDTO.setMobile(resultSet.getString("mobile"));
					doctorDTO.setEmail(resultSet.getString("email"));
					doctorDTO.setSpecialization(resultSet.getString("specialization"));
					doctorDTO.setConsultationFee(resultSet.getBigDecimal("consultation_fee"));
					doctorDTO.setActiveFlag(resultSet.getInt("active_flag"));

				
						return doctorDTO;
					
				}
			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		throw new ServiceException("Doctor not found", HttpStatus.NOT_FOUND);
	}

	public List<DoctorDTO> getAll(int namespaceId) throws SQLException {
		LOGGER.info(" GET-ALL - Doctor lookup process for list");
		List<DoctorDTO> doctorDTOList = new ArrayList<>();
		String query = "select id, code, name, namespace_id, user_id, department_id, mobile, email, specialization, consultation_fee, active_flag from doctor where namespace_id=? and active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, namespaceId);
			NamespaceDTO namespaceDTO = namespaceDAO.getById(namespaceId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					DoctorDTO doctorDTO = new DoctorDTO();
					doctorDTO.setId(resultSet.getInt("id"));
					doctorDTO.setCode(resultSet.getString("code"));
					doctorDTO.setName(resultSet.getString("name"));
					doctorDTO.setNamespaceDTO(namespaceDTO);
					doctorDTO.setUserDTO(userDAO.getById(resultSet.getInt("user_id")));
					doctorDTO.setDepartmentDTO(departmentDAO.getById(resultSet.getInt("department_id")));
					doctorDTO.setMobile(resultSet.getString("mobile"));
					doctorDTO.setEmail(resultSet.getString("email"));
					doctorDTO.setSpecialization(resultSet.getString("specialization"));
					doctorDTO.setConsultationFee(resultSet.getBigDecimal("consultation_fee"));
					doctorDTO.setActiveFlag(resultSet.getInt("active_flag"));

						doctorDTOList.add(doctorDTO);
					}
			}
			return doctorDTOList;
		}
	}
	
	public DoctorDTO getById(int id) {
		LOGGER.info(" GET-BY-ID - Doctor lookup process by id");
		String query = "select id, code, name, namespace_id, user_id, department_id, mobile, email, specialization, consultation_fee, active_flag from doctor where id=? and active_flag <2";
		DoctorDTO doctorDTO = new DoctorDTO();
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					doctorDTO.setId(resultSet.getInt("id"));
					doctorDTO.setCode(resultSet.getString("code"));
					doctorDTO.setName(resultSet.getString("name"));
					doctorDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					doctorDTO.setUserDTO(userDAO.getById(resultSet.getInt("user_id")));
					doctorDTO.setDepartmentDTO(departmentDAO.getById(resultSet.getInt("department_id")));
					doctorDTO.setMobile(resultSet.getString("mobile"));
					doctorDTO.setEmail(resultSet.getString("email"));
					doctorDTO.setSpecialization(resultSet.getString("specialization"));
					doctorDTO.setConsultationFee(resultSet.getBigDecimal("consultation_fee"));
					doctorDTO.setActiveFlag(resultSet.getInt("active_flag"));

						return doctorDTO;
				}
			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		throw new ServiceException("Doctor NotFound...", HttpStatus.NOT_FOUND);
	}

}
