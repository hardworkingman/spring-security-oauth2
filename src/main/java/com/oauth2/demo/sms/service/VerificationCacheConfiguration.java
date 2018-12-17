package com.oauth2.demo.sms.service;

import com.oauth2.demo.sms.service.impl.GeneralVerificationCodeCache;
import com.oauth2.demo.sms.service.impl.RedisVerificationCodeCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

public class VerificationCacheConfiguration {

    @Bean
    @ConditionalOnBean(RedisOperations.class)
    public RedisVerificationCodeCache redisVerificationCodeCache(StringRedisTemplate stringRedisTemplate){
        return new RedisVerificationCodeCache(stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(VerificationCodeCache.class)
    public GeneralVerificationCodeCache generalVerificationCodeCache(CacheManager cacheManager){
        return new GeneralVerificationCodeCache(cacheManager);
    }
}
