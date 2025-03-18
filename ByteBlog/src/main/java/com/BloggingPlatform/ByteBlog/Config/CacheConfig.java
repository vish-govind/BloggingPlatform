package com.BloggingPlatform.ByteBlog.Config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class CacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        logger.info("Initializing Redis Cache Manager with TTL of 180 minutes.");
        
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(180)) 
            .disableCachingNullValues();
        
        CacheManager cacheManager = RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();

        logger.info("Redis Cache Manager initialized successfully.");
        return cacheManager;
    }
}
