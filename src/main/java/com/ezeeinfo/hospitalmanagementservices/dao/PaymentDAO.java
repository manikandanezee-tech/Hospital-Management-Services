package com.ezeeinfo.hospitalmanagementservices.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.ezeeinfo.hospitalmanagementservices.dto.PaymentDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.PaymentModeEM;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class PaymentDAO {
	@Autowired
	private BillingDAO billingDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDAO.class);

	public PaymentDTO save(PaymentDTO paymentDTO) {
		LOGGER.info(" SAVE - insertion / updation payment on  process");
		String code = "";
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_PAYMENT_IUD(?,?,?,?,?,?,?,?)}");) {

			// inout parameter for pcrCode
			callableStatement.setString(1, paymentDTO.getCode());
			callableStatement.setInt(2, paymentDTO.getBillingDTO().getId());
			callableStatement.setBigDecimal(3, paymentDTO.getAmount());
			callableStatement.setInt(4, paymentDTO.getPaymentMode().getValue());
			callableStatement.setString(5, paymentDTO.getTransactionRefNumber());
			callableStatement.setInt(6, paymentDTO.getActiveFlag());
			callableStatement.setInt(7, paymentDTO.getUpdatedBy().getId());
			// out parameters (code, setRowCount)
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(8, Types.INTEGER);
			callableStatement.execute();
			code = callableStatement.getString(1);

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		paymentDTO.setCode(code);
		paymentDTO.setPaymentDatetime(LocalDateTime.now());
		return paymentDTO;
	}

	public PaymentDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Payment lookup process by code : {}", code);
		String query = "select id, code, billing_id, amount, payment_mode, payment_datetime, transaction_ref_no, active_flag from payment where code =? and active_flag<2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, code);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					PaymentDTO paymentDTO = new PaymentDTO();
					paymentDTO.setId(resultSet.getInt("id"));
					paymentDTO.setCode(resultSet.getString("code"));
					paymentDTO.setBillingDTO(billingDAO.getById(resultSet.getInt("billing_id")));
					paymentDTO.setAmount(resultSet.getBigDecimal("amount"));
					paymentDTO.setPaymentMode(PaymentModeEM.getByValue(resultSet.getInt("payment_mode")));
					paymentDTO.setPaymentDatetime(resultSet.getTimestamp("payment_datetime").toLocalDateTime());
					paymentDTO.setTransactionRefNumber(resultSet.getString("transaction_ref_no"));
					paymentDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return paymentDTO;
				}

			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Invalid paymentCode", HttpStatus.NOT_FOUND);
	}

	public List<PaymentDTO> getAll(int namespaceId) {
		LOGGER.info(" GET-ALL - Payment lookup process for list");
		List<PaymentDTO> paymentDTOList = new ArrayList<>();
		String query = "select p.id, p.code, p.billing_id, p.amount, p.payment_mode, p.payment_datetime, p.transaction_ref_no, p.active_flag from payment p inner join billing b on p.billing_id = b.id where b.namespace_id =? and p.active_flag<2";
		try (Connection connection = DbUtill.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, namespaceId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					PaymentDTO paymentDTO = new PaymentDTO();
					paymentDTO.setId(resultSet.getInt("id"));
					paymentDTO.setCode(resultSet.getString("code"));
					paymentDTO.setBillingDTO(billingDAO.getById(resultSet.getInt("billing_id")));
					paymentDTO.setAmount(resultSet.getBigDecimal("amount"));
					paymentDTO.setPaymentMode(PaymentModeEM.getByValue(resultSet.getInt("payment_mode")));
					paymentDTO.setPaymentDatetime(resultSet.getTimestamp("payment_datetime").toLocalDateTime());
					paymentDTO.setTransactionRefNumber(resultSet.getString("transaction_ref_no"));
					paymentDTO.setActiveFlag(resultSet.getInt("active_flag"));
					paymentDTOList.add(paymentDTO);
				}

			}

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return paymentDTOList;
	}
}
