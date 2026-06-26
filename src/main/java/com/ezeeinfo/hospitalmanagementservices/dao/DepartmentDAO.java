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

import com.ezeeinfo.hospitalmanagementservices.dao.DepartmentDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.DepartmentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class DepartmentDAO {
	@Autowired
	private NamespaceDAO namespaceDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDAO.class);

	public DepartmentDTO save(DepartmentDTO departmentDTO) {
		LOGGER.info(" SAVE - Department insert/update on process ");
		String code = "";
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_DEPARTMENT_IUD(?,?,?,?,?,?)}");) {
			callableStatement.setString(1, departmentDTO.getCode());
			callableStatement.setString(2, departmentDTO.getName());
			callableStatement.setInt(3, departmentDTO.getNamespaceDTO().getId());
			callableStatement.setInt(4, departmentDTO.getActiveFlag());
			callableStatement.setInt(5, departmentDTO.getUpdatedBy().getId());
			callableStatement.execute();
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(6, Types.INTEGER);
			code = callableStatement.getString(1);
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		departmentDTO.setCode(code);
		return departmentDTO;
	}

	public DepartmentDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Department lookup by code on process");
		String query = "select id, code, name, namespace_id, active_flag from departments where code=? and active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, code);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					DepartmentDTO departmentDTO = new DepartmentDTO();
					departmentDTO.setId(resultSet.getInt("id"));
					departmentDTO.setCode(resultSet.getString("code"));
					departmentDTO.setName(resultSet.getString("name"));
					departmentDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					departmentDTO.setActiveFlag(resultSet.getInt("active_flag"));
				
						return departmentDTO;
					
				}
			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Department Not Found", HttpStatus.NOT_FOUND);

	}

	public DepartmentDTO getById(int id) {
		LOGGER.info(" GET-BY-ID - Department lookup process on");
		String query = "select id, code, name, namespace_id, active_flag from departments where id=? and active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					DepartmentDTO departmentDTO = new DepartmentDTO();
					departmentDTO.setId(resultSet.getInt("id"));
					departmentDTO.setCode(resultSet.getString("code"));
					departmentDTO.setName(resultSet.getString("name"));
					departmentDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					departmentDTO.setActiveFlag(resultSet.getInt("active_flag"));

						return departmentDTO;
					
				}
			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Department NotFound...", HttpStatus.NOT_FOUND);

	}

	public List<DepartmentDTO> getAll(int namespaceId) {
		LOGGER.info(" GET-ALL - department  lookup process on for list");
		List<DepartmentDTO> departmentDTOList = new ArrayList<DepartmentDTO>();

		String query = "select id, code, name, namespace_id, active_flag from departments where namespace_id=? and active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, namespaceId);
			NamespaceDTO namespaceDTO = namespaceDAO.getById(namespaceId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					DepartmentDTO departmentDTO = new DepartmentDTO();
					departmentDTO.setId(resultSet.getInt("id"));
					departmentDTO.setCode(resultSet.getString("code"));
					departmentDTO.setName(resultSet.getString("name"));
					departmentDTO.setNamespaceDTO(namespaceDTO);
					departmentDTO.setActiveFlag(resultSet.getInt("active_flag"));

						departmentDTOList.add(departmentDTO);
				}
			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return departmentDTOList;

	}
}
