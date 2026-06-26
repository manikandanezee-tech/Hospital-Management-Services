package com.ezeeinfo.hospitalmanagementservices.cache;

import java.util.List;

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
		LOGGER.info("GET-BY-CODE - namespace get from cache");
		NamespaceDTO namespaceDTO = (NamespaceDTO) redisTemplate
				.opsForValue()
				.get("NAMESPACE_"+code);
		if(namespaceDTO==null) {
			LOGGER.info("GET-BY-CODE - cache missing fetching from db");
			namespaceDTO=namespaceDAO.getByCode(code);
			
			if(namespaceDTO!=null) {
				LOGGER.info("GET-BY-CODE - update the cache : {}",code);
				redisTemplate.opsForValue().set("NAMESPACE_"+code, namespaceDTO);
			}
			
		}
		return namespaceDTO;
	}
	
	@SuppressWarnings("unchecked")
	public List<NamespaceDTO> getAll(){
		String key = "NAMESPACE_ALL";
		List<NamespaceDTO> namespaceDTOList=(List<NamespaceDTO>) redisTemplate
				.opsForValue()
				.get(key);
		if(namespaceDTOList ==null) {
			namespaceDTOList=namespaceDAO.getAll();
			if(namespaceDTOList!=null) {
				redisTemplate.opsForValue().set(key, namespaceDTOList);
			}
		}
		return namespaceDTOList;
	}
	
}
