package com.ezeeinfo.hospitalmanagementservices.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;


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
	public CacheManager ehCacheManager() {
		return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
	}

}