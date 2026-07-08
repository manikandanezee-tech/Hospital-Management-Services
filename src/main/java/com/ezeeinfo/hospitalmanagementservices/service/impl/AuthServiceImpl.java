package com.ezeeinfo.hospitalmanagementservices.service.impl;

import java.sql.SQLException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ezeeinfo.hospitalmanagementservices.dao.UserDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.LoginDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;
import com.ezeeinfo.hospitalmanagementservices.service.AuthService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CacheManager cacheManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Override
	public String login(LoginDTO loginDTO) throws SQLException {
		LOGGER.info("LOGIN -  login request forward to dao");
		AuthDTO authDTO = userDAO.login(loginDTO.getUserName());

		if (authDTO.getUserDTO().getUserName().equals(loginDTO.getUserName())) {
			if (passwordEncoder.matches(loginDTO.getPassword(), authDTO.getUserDTO().getToken())) {

				LOGGER.info("LOGIN -  successfully validate the login details");
				String authToken = UUID.randomUUID().toString();

				Cache cache = cacheManager.getCache("userCache");
				cache.put(new Element(authToken, authDTO));

				return authToken;
			}

		}

		throw new ServiceException("Invalid credential", HttpStatus.BAD_REQUEST);
	}

	@Override
	public String logout(String authToken) {
		Cache cache = cacheManager.getCache("userCache");
		if (cache.remove(authToken)) {
			return "Log out success....";
		}
		throw new ServiceException("Logout Failed", HttpStatus.BAD_REQUEST);
	}
}
