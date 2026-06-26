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

import com.ezeeinfo.hospitalmanagementservices.dao.UserDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.UserRoleEM;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class UserDAO {
	@Autowired
	private NamespaceDAO namespaceDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

	public UserDTO save(UserDTO userDTO) throws SQLException {
		LOGGER.info(" SAVE - insertion / updation User on  process");
		String code = "";
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{ CALL EZEE_SP_USER_IUD(?,?,?,?,?,?,?,?)}");) {
			callableStatement.setString(1, userDTO.getCode());

			callableStatement.setString(2, userDTO.getUserName());
			callableStatement.setString(3, userDTO.getToken());
			callableStatement.setInt(4, userDTO.getRole().getValue());

			callableStatement.setInt(5,userDTO.getNamespaceDTO().getId());
			callableStatement.setInt(6, userDTO.getActiveFlag());
			callableStatement.setInt(7, userDTO.getUpdatedBy().getId());
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(8, Types.INTEGER);

			callableStatement.execute();

			code = callableStatement.getString(1);

		}

		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		userDTO.setCode(code);
		return userDTO;
	}

	public UserDTO getByCode(String code) throws SQLException {
		LOGGER.info(" GET-BY-CODE - User lookup process by code : {}",code);
		String query = "select id, code, user_name, namespace_id, role, active_flag from user where code =? and active_flag < 2";

		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, code);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					UserDTO userDTO = new UserDTO();
					userDTO.setId(resultSet.getInt("id"));
					userDTO.setCode(resultSet.getString("code"));
					userDTO.setUserName(resultSet.getString("user_name"));
					userDTO.setRole(UserRoleEM.getUserRoleEM(resultSet.getInt("role")));
					userDTO.setActiveFlag(resultSet.getInt("active_flag"));
					userDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
						return userDTO;
				
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Invalid User Code", HttpStatus.NOT_FOUND);
	}

	public List<UserDTO> getAll(int namespaceId) {
		LOGGER.info(" GET-ALL - User lookup process on");
		List<UserDTO> list = new ArrayList<>();

		String query = "select id, code, user_name, namespace_id, role, active_flag from user where namespace_id=? and active_flag < 2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, namespaceId);
			NamespaceDTO namespaceDTO = namespaceDAO.getById(namespaceId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {

					UserDTO userDTO = new UserDTO();
					userDTO.setId(resultSet.getInt("id"));
					userDTO.setUserName(resultSet.getString("user_name"));
					userDTO.setCode(resultSet.getString("code"));
					userDTO.setNamespaceDTO(namespaceDTO);
					userDTO.setRole(UserRoleEM.getUserRoleEM(resultSet.getInt("role")));
					userDTO.setActiveFlag(resultSet.getInt("active_flag"));
			
						list.add(userDTO);
					

				}
			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return list;
	}

	public UserDTO login(String userName) {
		LOGGER.info(" LOGIN - User lookup for login");
		String query = "select id, code, user_name, token, role, namespace_id from user "

				+ "where user_name = ? and active_flag = 1";

		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, userName);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				
				if (resultSet.next()) {
					UserDTO userDTO = new UserDTO();
					userDTO.setId(resultSet.getInt("id"));
					userDTO.setCode(resultSet.getString("code"));
					userDTO.setUserName(resultSet.getString("user_name"));
					userDTO.setToken(resultSet.getString("token"));
					userDTO.setRole(UserRoleEM.getUserRoleEM(resultSet.getInt("role")));
					userDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
				
						return userDTO;
					

				}

			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("User NotFound", HttpStatus.NOT_FOUND);

	}

	public UserDTO getById(int id) throws SQLException {
		LOGGER.info(" GET-BY-ID - User lookup process on ID : {}",id);

		String query = "select id, code, user_name, namespace_id, role, active_flag from user where id =? and active_flag <2";
		UserDTO userDTO = new UserDTO();

		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					userDTO.setId(resultSet.getInt("id"));
					userDTO.setCode(resultSet.getString("code"));
					userDTO.setUserName(resultSet.getString("user_name"));
					userDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					userDTO.setRole(UserRoleEM.getUserRoleEM(resultSet.getInt("role")));
					userDTO.setActiveFlag(resultSet.getInt("active_flag"));
					if (resultSet.getInt("active_flag") < 2) {
						return userDTO;
					}
				}
			}

		}

		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("User NotFound ...", HttpStatus.NOT_FOUND);
	}

}
