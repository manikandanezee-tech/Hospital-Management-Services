package com.ezeeinfo.hospitalmanagementservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		return new JedisConnectionFactory();
	}
	
	@Bean
	public RedisTemplate<String,Object>redisTemplate(){
		RedisTemplate<String, Object> template=new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory());
		template.afterPropertiesSet();
		return template;
	}
}
