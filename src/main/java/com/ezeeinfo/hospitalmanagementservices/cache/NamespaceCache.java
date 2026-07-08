package com.ezeeinfo.hospitalmanagementservices.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.ezeeinfo.hospitalmanagementservices.dao.NamespaceDAO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;

@Component
public class NamespaceCache {
	@Autowired
	private NamespaceDAO namespaceDAO;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceCache.class);

	public NamespaceDTO getByCode(String code) {

		Object object = redisTemplate.opsForValue().get("NAMESPACE_" + code);
		
		NamespaceDTO namespaceDTO = null;
		
		if (object != null) {
			LOGGER.info("GET-BY-CODE - namespace get from cache");
			namespaceDTO = (NamespaceDTO) object;
		}
		if (namespaceDTO == null) {
			LOGGER.info("GET-BY-CODE - cache missing fetching from db");
			namespaceDTO = namespaceDAO.getByCode(code);

			if (namespaceDTO != null) {
				LOGGER.info("SET-CACHE -  Add namespace on cache using Code");
				redisTemplate.opsForValue().set("NAMESPACE_" + code, namespaceDTO, 30, TimeUnit.MINUTES);
			}

		}
		return namespaceDTO;
	}

	@SuppressWarnings("unchecked")
	public List<NamespaceDTO> getAll() {
		String key = "NAMESPACE_ALL";
		LOGGER.info("GET-ALL - Namespacelist fetching from cache");
		List<NamespaceDTO> namespaceDTOList = (List<NamespaceDTO>) redisTemplate.opsForValue().get(key);
		
		if (namespaceDTOList == null) {
			LOGGER.info("GET-ALL - cache missing fetching from db");
			namespaceDTOList = namespaceDAO.getAll();
			if (namespaceDTOList != null) {
				LOGGER.info("SET-CACHE - Add a namespace list on cache using key");
				redisTemplate.opsForValue().set(key, namespaceDTOList, 30, TimeUnit.MINUTES);
			}
		}
		return namespaceDTOList;
	}

}
