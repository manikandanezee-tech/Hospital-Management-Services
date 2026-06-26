package com.ezeeinfo.hospitalmanagementservices.cache;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ezeeinfo.hospitalmanagementservices.dao.UserDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;

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
		if(element!=null) {
			LOGGER.info(" GET-BY-CODE - user get from cache");
			userDTO = (UserDTO) element.getObjectValue();
		}
		
		if (userDTO == null) {
			LOGGER.info(" GET-BY-CODE - cache miss, directly call db for get user by code : {}",code);
			userDTO = userDAO.getByCode(code);
			
			if (userDTO != null) {
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
		if(element!=null) {
			userDTOList = (List<UserDTO>) element.getObjectValue();
		}
		if (userDTOList == null) {
			userDTOList = userDAO.getAll(namespaceId);
			if (userDTOList != null) {
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
		if(element!=null) {
			LOGGER.info(" GET-BY-ID - user get from cache");
			userDTO = (UserDTO) element.getObjectValue();
		}
		
		if (userDTO == null) {
			LOGGER.info(" GET-BY-ID - cache miss, directly call db for get user by id ");
			userDTO = userDAO.getById(id);
			
			if (userDTO != null) {
				cache.put(new Element(id, userDTO));
			}
		}
		System.out.println( userDTO);
		return userDTO;
	}

}
