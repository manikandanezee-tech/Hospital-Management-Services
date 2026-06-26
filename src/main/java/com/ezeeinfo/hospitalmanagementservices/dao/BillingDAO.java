package com.ezeeinfo.hospitalmanagementservices.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.ezeeinfo.hospitalmanagementservices.dto.BillingDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.BillStatusEM;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class BillingDAO {

	@Autowired
	private AppointmentDAO appointmentDAO;
	@Autowired
	private NamespaceDAO namespaceDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingDAO.class);
	

	public BillingDTO save(BillingDTO billingDTO) {
		LOGGER.info(" SAVE - Billing insert/update on process ");
		String code="";
		try(Connection connection=DbUtill.getConnection();
				CallableStatement callableStatement=connection.prepareCall("{ CALL EZEE_SP_BILLING_IUD(?,?,?,?,?,?,?,?,?,?)}");) {
			callableStatement.setString(1, billingDTO.getCode());
			callableStatement.setInt(2, billingDTO.getNamespaceDTO().getId());
			callableStatement.setInt(3, billingDTO.getAppointmentDTO().getId());
			callableStatement.setBigDecimal(4, billingDTO.getTotalAmount());
			callableStatement.setBigDecimal(5, billingDTO.getPaidAmount());
			callableStatement.setBigDecimal(6, billingDTO.getBalanceAmount());
			callableStatement.setInt(7, billingDTO.getBillStatus().getValue());
			callableStatement.setInt(8, billingDTO.getActiveFlag());
			callableStatement.setInt(9, billingDTO.getUpdatedBy().getId());
			callableStatement.execute();
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(10, Types.INTEGER);
			code = callableStatement.getString(1);
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		billingDTO.setCode(code);
		return billingDTO;
	}

	
	
	
	public BillingDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Billing lookup by code on process");
		String query = "select id, code, namespace_id, appointment_id, total_amount, bill_datetime, paid_amount, balance_amount, billing_status, active_flag from billing where code =? and active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, code);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					BillingDTO billingDTO=new BillingDTO();
					billingDTO.setId(resultSet.getInt("id"));
					billingDTO.setCode(resultSet.getString("code"));
					billingDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					billingDTO.setAppointmentDTO(appointmentDAO.getById(resultSet.getInt("appointment_id")));
					billingDTO.setTotalAmount(resultSet.getBigDecimal("total_amount"));
					billingDTO.setPaidAmount(resultSet.getBigDecimal("paid_amount"));
					billingDTO.setBalanceAmount(resultSet.getBigDecimal("balance_amount"));
					billingDTO.setBillDateTime(resultSet.getTimestamp("bill_datetime").toLocalDateTime());
					billingDTO.setBillStatus(BillStatusEM.getByValue(resultSet.getInt("billing_status")));
					billingDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return billingDTO;
		}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Invalid Bill Code", HttpStatus.NOT_FOUND);
	}




	public BillingDTO isBillAlreadyGenerated(int appointmentId) {
		LOGGER.info(" GET-BY-APPOINTMENT - billing lookup by appointment on process");
		String query="select id, code from billing where appointment_id=?";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, appointmentId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					BillingDTO billingDTO=new BillingDTO();
					billingDTO.setId(resultSet.getInt("id"));
					billingDTO.setCode(resultSet.getString("code"));
					return billingDTO;
				}
			}
			}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	public BillingDTO getById(int id) {
		LOGGER.info(" GET-BY-ID - billing lookup by id on process");
		String query = "select id, code, namespace_id, appointment_id, total_amount, bill_datetime, paid_amount, balance_amount, billing_status, active_flag from billing where id =? and active_flag <2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					BillingDTO billingDTO=new BillingDTO();
					billingDTO.setId(resultSet.getInt("id"));
					billingDTO.setCode(resultSet.getString("code"));
					billingDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					billingDTO.setAppointmentDTO(appointmentDAO.getById(resultSet.getInt("appointment_id")));
					billingDTO.setTotalAmount(resultSet.getBigDecimal("total_amount"));
					billingDTO.setPaidAmount(resultSet.getBigDecimal("paid_amount"));
					billingDTO.setBalanceAmount(resultSet.getBigDecimal("balance_amount"));
					billingDTO.setBillDateTime(resultSet.getTimestamp("bill_datetime").toLocalDateTime());
					billingDTO.setBillStatus(BillStatusEM.getByValue(resultSet.getInt("billing_status")));
					billingDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return billingDTO;
		}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Invalid Bill Code...", HttpStatus.NOT_FOUND);
	}
}
