package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.cache.NamespaceCache;
import com.ezeeinfo.hospitalmanagementservices.dao.NamespaceDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.NamespaceService;

@Service
public class NamespaceServiceImpl implements NamespaceService {
	@Autowired
	private NamespaceDAO namespaceDAO;
	@Autowired
	private NamespaceCache namespaceCache;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceServiceImpl.class);

	@Override
	public NamespaceDTO save(NamespaceDTO namespaceDTO, HttpServletRequest request) throws SQLException {
		AuthDTO authDTO = (AuthDTO)request.getAttribute("authDTO");
		String role = authDTO.getUserDTO().getRole().getCode();

		namespaceDTO.setUpdatedBy(authDTO.getUserDTO());

		if (role.equals("MASTADM")) {
			LOGGER.info("SAVE - Namespace validation success");
			NamespaceDTO namespaceDTO2 = namespaceDAO.save(namespaceDTO);

			redisTemplate.delete("NAMESPACE_" + namespaceDTO.getCode());
			redisTemplate.delete("NAMESPACE_ALL");

			LOGGER.info("SAVE - Namespace insert/update successfully");
			return namespaceDTO2;
		}
		throw new ServiceException("Access Denied", HttpStatus.CONFLICT);

	}

	@Override
	public NamespaceDTO getByCode(String code) {
		LOGGER.info("GET-BY-CODE - fetching namespace by code: {}",code);
		NamespaceDTO namespaceDTO = namespaceCache.getByCode(code);
		LOGGER.info("GET-BY-CODE - Namespace retrived successfully");
		return namespaceDTO;

	}

	@Override
	public List<NamespaceDTO> getAll() {
		LOGGER.info("GET-ALL - fetching namespace list");
		List<NamespaceDTO> namespaceDTOList = namespaceCache.getAll();
		LOGGER.info("GET-ALL - retrived namespace list successfully");
		return namespaceDTOList;
	}

}
