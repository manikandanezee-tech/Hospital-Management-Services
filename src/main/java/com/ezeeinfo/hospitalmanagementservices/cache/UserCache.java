package com.ezeeinfo.hospitalmanagementservices.cache;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ezeeinfo.hospitalmanagementservices.dao.UserDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;
import com.ezeeinfo.hospitalmanagementservices.exception.ServiceException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component
public class UserCache {
	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private UserDAO userDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserCache.class);

	public UserDTO getByCode(String code) throws SQLException {
		Cache cache = cacheManager.getCache("userCache");
		Element element = cache.get(code);
		UserDTO userDTO = null;
		if (element != null) {
			LOGGER.info(" GET-BY-CODE - User get from cache");
			userDTO = (UserDTO) element.getObjectValue();
		}

		if (userDTO == null) {
			LOGGER.info(" GET-BY-CODE - cache miss, directly call db for get user by code : {}", code);
			userDTO = userDAO.getByCode(code);

			if (userDTO != null) {
				LOGGER.info("SET-CACHE - Add a user on cache using user Code");
				cache.put(new Element(code, userDTO));
			}
		}
		return userDTO;
	}

	@SuppressWarnings("unchecked")
	public List<UserDTO> getAll(int namespaceId) {
		Cache cache = cacheManager.getCache("userCache");
		Element element = cache.get(namespaceId);
		List<UserDTO> userDTOList = null;
		if (element != null) {
			LOGGER.info(" GET-BY-CODE - Userlist get from cache");
			userDTOList = (List<UserDTO>) element.getObjectValue();
		}
		if (userDTOList == null) {
			LOGGER.info(" GET-BY-CODE - cache miss, directly call db for get userList");
			userDTOList = userDAO.getAll(namespaceId);
			if (userDTOList != null) {
				LOGGER.info("SET-CACHE - Add User list on cache using namespaceId");
				cache.put(new Element(namespaceId, userDTOList));
			}

		}
		return userDTOList;
	}

	public UserDTO getById(int id) throws SQLException {
		System.out.println("cache");
		Cache cache = cacheManager.getCache("userCache");
		Element element = cache.get(id);
		UserDTO userDTO = null;
		if (element != null) {
			LOGGER.info(" GET-BY-ID - user get from cache");
			userDTO = (UserDTO) element.getObjectValue();
		}

		if (userDTO == null) {
			LOGGER.info(" GET-BY-ID - cache miss, directly call db for get user by id ");
			userDTO = userDAO.getById(id);

			if (userDTO != null) {
				LOGGER.info("SET-CACHE - Add a user on cache using id");
				cache.put(new Element(id, userDTO));
			}
		}
		System.out.println(userDTO);
		return userDTO;
	}

	public AuthDTO getAuthDTO(String authToken) throws SQLException {
		Cache cache = cacheManager.getCache("userCache");
		Element element = cache.get(authToken);
		if (element != null) {
			LOGGER.info(" GET-BY-TOKEN - AuthDTO get from cache");
			AuthDTO authDTO = (AuthDTO) element.getObjectValue();
			return authDTO;

		}
		throw new ServiceException("INVALID TOKEN", HttpStatus.UNAUTHORIZED);
	}

}
