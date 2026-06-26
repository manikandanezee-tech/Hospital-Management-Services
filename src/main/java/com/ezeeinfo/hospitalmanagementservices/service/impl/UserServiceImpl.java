package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.cache.NamespaceCache;
import com.ezeeinfo.hospitalmanagementservices.cache.UserCache;
import com.ezeeinfo.hospitalmanagementservices.dao.UserDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.UserDTOService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Service
public class UserServiceImpl implements UserDTOService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private UserCache userCache;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private NamespaceCache namespaceCache;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserDTO save(UserDTO userDTO, HttpServletRequest request) throws SQLException {
		AuthDTO authDTO = (AuthDTO) request.getAttribute("authDTO");

		String namespaceCode = authDTO.getUserDTO().getNamespaceDTO().getCode();
		String role = authDTO.getUserDTO().getRole().getCode();
		
		
		userDTO.setUpdatedBy(authDTO.getUserDTO());

		UserDTO userDTO2 = null;
		// check the login user is master admin then update the user without checking namespace.
		if (role.equals("MASTADM") || role.equals("ADM") && namespaceCode.equals(userDTO.getNamespaceDTO().getCode())) {
			userDTO.setToken(passwordEncoder.encode(userDTO.getToken()));
			LOGGER.info("SAVE - successfully validation the user ");
			userDTO2 = userDAO.save(userDTO);

			Cache cache = cacheManager.getCache("userCache");
			cache.remove(userDTO.getCode());
			cache.remove(userDTO.getNamespaceDTO().getId());

		}
		else {
			throw new ServiceException("Access Denied", HttpStatus.UNAUTHORIZED);
		}
		if (userDTO2 != null) {
			LOGGER.info("SAVE - user insert/update successfully");
			return userDTO2;
		}
		throw new ServiceException("Something wrong on update user", HttpStatus.BAD_REQUEST);
		
	}

	@Override
	public UserDTO getByCode(String code) throws SQLException {
		LOGGER.info("GET-BY-CODE - fetching the user by code : {}",code);
		UserDTO userDTO = userCache.getByCode(code);
		LOGGER.info("GET-BY-CODE - Successfully Retrived the user by code : {}",code);
		return userDTO;

	}

	@Override
	public List<UserDTO> getAll(HttpServletRequest request) {
		AuthDTO authDTO = (AuthDTO) request.getAttribute("authDTO");
		NamespaceDTO namespaceDTO = namespaceCache.getByCode(authDTO.getUserDTO().getNamespaceDTO().getCode());
		LOGGER.info("GET-ALL - fetching user list");
		List<UserDTO> userDTOList = userCache.getAll(namespaceDTO.getId());
		LOGGER.info("GET-ALL - successfully retrived user list");
		return userDTOList;
	}

}
