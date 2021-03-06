package com.oauth2.demo.sms.service.impl;

import com.oauth2.demo.sms.service.VerificationCodeCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存，会自动过期
 **/
@Component
public class RedisVerificationCodeCache implements VerificationCodeCache {

    private StringRedisTemplate redisTemplate;

    public RedisVerificationCodeCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void set(String cacheName, String key, String value, long expire){
        redisTemplate.opsForValue().set(cacheName + ":" + key,value);
        redisTemplate.expire(cacheName + ":" + key,expire, TimeUnit.MILLISECONDS);
    }

    @Override
    public void remove(String cacheName, String key) {
        redisTemplate.delete(cacheName + ":" + key);
    }

    @Override
    public String get(String cacheName, String key){
        return redisTemplate.opsForValue().get(cacheName + ":" + key);
    }

    @Override
    public boolean validate(String cacheName, String key, String value){
        String originValue = this.get(cacheName,key);
        if(StringUtils.isNotEmpty(originValue)){
            return originValue.equals(value);
        }
        return  false;
    }

    @Override
    public boolean isExpire(String cacheName, String key) {
        return redisTemplate.opsForValue().get(cacheName + ":" + key) == null;
    }
}