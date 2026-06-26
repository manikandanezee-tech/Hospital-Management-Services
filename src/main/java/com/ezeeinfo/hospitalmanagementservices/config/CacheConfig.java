package com.ezeeinfo.hospitalmanagementservices.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {

		EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();

		factory.setConfigLocation(new ClassPathResource("ehcache.xml"));

		factory.setShared(true);

		return factory;
	}

	@Bean("ehCacheManager")
	@Primary
	public org.springframework.cache.CacheManager ehCacheManager() {
		return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
	}

//	@Bean("redisCacheManager")
//    public org.springframework.cache.CacheManager redisCacheManager(){
//    	RedisStandaloneConfiguration config=new RedisStandaloneConfiguration("localhost",6379);
//    	JedisConnectionFactory factory=new JedisConnectionFactory(config);
//        factory.afterPropertiesSet();
//        RedisCacheConfiguration cacheConfig=RedisCacheConfiguration.defaultCacheConfig()
//        		.entryTtl(Duration.ofMinutes(30));
//        
//        return RedisCacheManager.builder(factory)
//        		.cacheDefaults(cacheConfig).build();
//    }
}