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

import com.ezeeinfo.hospitalmanagementservices.dto.MedicineDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.util.DbUtill;

@Repository
public class MedicineDAO {
	@Autowired
	private NamespaceDAO namespaceDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MedicineDAO.class);

	public MedicineDTO save(MedicineDTO medicineDTO) {
		LOGGER.info(" SAVE - insertion / updation process on medicine");
		String code="";
		try (Connection connection = DbUtill.getConnection(); CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_MEDICINE_IUD(?,?,?,?,?,?,?,?,?)}");) {

			// inout parameter for pcrCode
			callableStatement.setString(1, medicineDTO.getCode());

			callableStatement.setString(2, medicineDTO.getName());
			callableStatement.setInt(3, medicineDTO.getNamespaceDTO().getId());
			callableStatement.setBigDecimal(4, medicineDTO.getPrice());
			callableStatement.setInt(5, medicineDTO.getCurrentStock());
			callableStatement.setString(6, medicineDTO.getSupplier());
			callableStatement.setInt(7, medicineDTO.getActiveFlag());
			callableStatement.setInt(8, medicineDTO.getUpdatedBy().getId());
			// out parameters (code, setRowCount)
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(9, Types.INTEGER);
			callableStatement.execute();
		code=callableStatement.getString(1);

		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		medicineDTO.setCode(code);
		return medicineDTO;
	}

	public MedicineDTO getByCode(String code) {
		LOGGER.info(" GET-BY-CODE - Medicine lookup process on code : {}",code);
		String query="select id, code, name, namespace_id, price, current_stock, supplier, active_flag from medicines where code =? and active_flag < 2";
		try (Connection connection = DbUtill.getConnection();
				PreparedStatement preparedStatement=connection.prepareStatement(query);){
			preparedStatement.setString(1, code);
			try(ResultSet resultSet=preparedStatement.executeQuery();){
				if(resultSet.next()) {
					MedicineDTO medicineDTO=new MedicineDTO();
					medicineDTO.setId(resultSet.getInt("id"));
					medicineDTO.setCode(resultSet.getString("code"));
					medicineDTO.setName(resultSet.getString("name"));
					medicineDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					medicineDTO.setPrice(resultSet.getBigDecimal("price"));
					medicineDTO.setCurrentStock(resultSet.getInt("current_stock"));
					medicineDTO.setSupplier(resultSet.getString("supplier"));
					medicineDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return medicineDTO;
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Medicine NotFound", HttpStatus.NOT_FOUND);
	}

	public List<MedicineDTO> getAll(Integer namespaceId) {
		LOGGER.info(" GET-ALL - Medicine lookup process for list");
		String query="select id, code, name, namespace_id, price, current_stock, supplier, active_flag from medicines where namespace_id =? and active_flag < 2";
		List<MedicineDTO> medicineDTOList=new ArrayList<MedicineDTO>();

		try (Connection connection = DbUtill.getConnection();
				PreparedStatement preparedStatement=connection.prepareStatement(query);){
			preparedStatement.setInt(1, namespaceId);
			NamespaceDTO namespaceDTO = namespaceDAO.getById(namespaceId);
			try(ResultSet resultSet=preparedStatement.executeQuery();){
				while(resultSet.next()) {
					MedicineDTO medicineDTO=new MedicineDTO();
					medicineDTO.setId(resultSet.getInt("id"));
					medicineDTO.setCode(resultSet.getString("code"));
					medicineDTO.setName(resultSet.getString("name"));
					medicineDTO.setNamespaceDTO(namespaceDTO);
					medicineDTO.setPrice(resultSet.getBigDecimal("price"));
					medicineDTO.setCurrentStock(resultSet.getInt("current_stock"));
					medicineDTO.setSupplier(resultSet.getString("supplier"));
					medicineDTO.setActiveFlag(resultSet.getInt("active_flag"));
					medicineDTOList.add(medicineDTO);
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return medicineDTOList;
	}
	
	public MedicineDTO getById(int id) {
		LOGGER.info(" GET-BY-ID - Medicine lookup process by id");
		String query="select id, code, name, namespace_id, price, current_stock, supplier, active_flag from medicines where id =? and active_flag < 2";
		try (Connection connection = DbUtill.getConnection();
				PreparedStatement preparedStatement=connection.prepareStatement(query);){
			preparedStatement.setInt(1, id);
			try(ResultSet resultSet=preparedStatement.executeQuery();){
				if(resultSet.next()) {
					MedicineDTO medicineDTO=new MedicineDTO();
					medicineDTO.setId(resultSet.getInt("id"));
					medicineDTO.setCode(resultSet.getString("code"));
					medicineDTO.setName(resultSet.getString("name"));
					medicineDTO.setNamespaceDTO(namespaceDAO.getById(resultSet.getInt("namespace_id")));
					medicineDTO.setPrice(resultSet.getBigDecimal("price"));
					medicineDTO.setCurrentStock(resultSet.getInt("current_stock"));
					medicineDTO.setSupplier(resultSet.getString("supplier"));
					medicineDTO.setActiveFlag(resultSet.getInt("active_flag"));
					return medicineDTO;
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new ServiceException("Medicine NotFound...", HttpStatus.NOT_FOUND);
	}


}
