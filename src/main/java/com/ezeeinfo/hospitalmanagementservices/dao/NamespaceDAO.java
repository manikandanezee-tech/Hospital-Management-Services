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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.ezeeinfo.hospitalmanagementservices.dao.NamespaceDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;

import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;

@Repository
public class NamespaceDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceDAO.class);

	public NamespaceDTO save(NamespaceDTO namespaceDTO) {
		String code = "";
		LOGGER.info(" SAVE - insertion / updation process on namespace");
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_NAMESPACE_IUD(?,?,?,?,?,?)}");) {

			// inout parameter for pcrCode
			callableStatement.setString(1, namespaceDTO.getCode());

			callableStatement.setString(2, namespaceDTO.getName());
			callableStatement.setString(3, namespaceDTO.getAddress());
			// Active flag
			callableStatement.setInt(4, namespaceDTO.getActiveFlag());

			callableStatement.setInt(5, namespaceDTO.getUpdatedBy().getId());
			// out parameters (code, setRowCount)
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(6, Types.INTEGER);
			callableStatement.execute();
			code = callableStatement.getString(1);

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		namespaceDTO.setCode(code);
		return namespaceDTO;
	}

	public NamespaceDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Namespace lookup process on code : {}",code);
		String query = "select id, code, name, address, active_flag from namespace where code=? and active_flag <2";

		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, code);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				if (resultSet.next()) {

					NamespaceDTO namespaceDTO = new NamespaceDTO();

					namespaceDTO.setId(resultSet.getInt("id"));
					namespaceDTO.setCode(resultSet.getString("code"));
					namespaceDTO.setName(resultSet.getString("name"));
					namespaceDTO.setAddress(resultSet.getString("address"));
					namespaceDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return namespaceDTO;

				}
			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		throw new ServiceException("Namespace NotFound", HttpStatus.NOT_FOUND);
	}

	public List<NamespaceDTO> getAll() {
		
		LOGGER.info(" GET-ALL - namespace lookup process for list");
		String query = "select id, code, name, address, active_flag from namespace where active_flag <2";
		List<NamespaceDTO> namespaceDTOList = new ArrayList<>();
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery();) {

			while (resultSet.next()) {
				NamespaceDTO namespaceDTO = new NamespaceDTO();
				namespaceDTO.setId(resultSet.getInt("id"));
				namespaceDTO.setCode(resultSet.getString("code"));
				namespaceDTO.setName(resultSet.getString("name"));
				namespaceDTO.setAddress(resultSet.getString("address"));
				namespaceDTO.setActiveFlag(resultSet.getInt("active_flag"));
				namespaceDTOList.add(namespaceDTO);

			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return namespaceDTOList;

	}

	public NamespaceDTO getById(int id) {
		LOGGER.info(" GET-BY-ID - namespace lookup process by id");
		String query = "select id, code, name, address, active_flag from namespace where id= ? and active_flag <2";
		NamespaceDTO namespaceDTO = new NamespaceDTO();
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {
					namespaceDTO.setId(resultSet.getInt("id"));
					namespaceDTO.setCode(resultSet.getString("code"));
					namespaceDTO.setName(resultSet.getString("name"));
					namespaceDTO.setAddress(resultSet.getString("address"));
					namespaceDTO.setActiveFlag(resultSet.getInt("active_flag"));

					return namespaceDTO;

				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		throw new ServiceException("Namespace Not Found...", HttpStatus.NOT_FOUND);
	}
}
