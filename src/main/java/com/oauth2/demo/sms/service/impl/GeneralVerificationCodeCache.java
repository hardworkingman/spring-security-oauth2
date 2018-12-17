package com.oauth2.demo.sms.service.impl;

import com.oauth2.demo.sms.service.VerificationCodeCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

/**
 * 普通的缓存，手动控制过期时间，不会自动过去，建议在测试环境下使用
 **/
public class GeneralVerificationCodeCache implements VerificationCodeCache {

    private CacheManager cacheManager;

    public GeneralVerificationCodeCache(CacheManager cacheManager){
        Assert.notNull(cacheManager,"CacheManager nust not be null");
    }

    @Override
    public void set(String cacheName, String key, String value, long expire) {
        String body = this.build(value,expire);
        this.getCache(cacheName).put(key,body);
    }

    /**
     * 通过TOKEN获取验证码主体
     * @param token
     * @return
     */
    private String getBodyByToken(String token,String cacheName) {
        Assert.isTrue(StringUtils.isNotEmpty(token), "验证码TOKEN为空");
        Cache cache = this.getCache(cacheName);
        Cache.ValueWrapper valueWrapper = cache.get(token);
        if (valueWrapper == null) {
            return null;
        }
        return valueWrapper.get().toString();
    }

    public boolean validate(String cacheName,String token, String code, boolean ignoreCase) {
        //验证是否已经过期
        boolean isExpired = this.isExpire(token,cacheName);
        if (isExpired) {
            this.remove(cacheName,token);
            return false;
        }
        String value = this.getCodeByToken(token,cacheName);
        //验证是否匹配
        boolean matched = ignoreCase ? value.equalsIgnoreCase(code) : value.equals(code);
        return matched;
    }

    /**
     * 获取验证码
     *
     * @param code
     * @return
     */
    private String getCode(String code) {
        String[] parts = this.getParts(code);
        return parts[0];
    }


    /**
     * 通过TOKEN获取验证码
     * @param token
     * @return
     */
    private String getCodeByToken(String token,String cacheName) {
        String code = this.getBodyByToken(token,cacheName);
        return this.getCode(code);
    }


    /**
     * 获取验证码缓存
     * @return
     */
    private Cache getCache(String cacheName) {
        return cacheManager.getCache(cacheName);
    }


    /**
     * 构建验证码主体
     * @param code 验证码
     * @param expire 过期时间
     * @return
     */
    private String build(String code,long expire){
        return String.format("%s@%s@%s", code, expire, System.currentTimeMillis());
    }


    @Override
    public void remove(String cacheName, String key) {

    }

    @Override
    public String get(String cacheName, String key) {
        return null;
    }

    @Override
    public boolean validate(String cacheName, String key, String value) {
        return false;
    }

    @Override
    public boolean isExpire(String cacheName, String key) {
        String code = this.getBodyByToken(key,cacheName);
        return this.validateExpire(code);
    }

    /**
     * 获取验证码结构数组
     *
     * @param code
     * @return
     */
    private String[] getParts(String code) {
        Assert.isTrue(StringUtils.isNoneEmpty(code), "验证码为空");
        String[] parts = code.split("@");
        Assert.isTrue(parts.length == 3, "验证码格式错误");
        return parts;
    }

    /**
     * 验证是否过期
     *
     * @param code
     * @return
     */
    private boolean validateExpire(String code) {
        String[] parts = this.getParts(code);
        Assert.isTrue(parts.length == 3, "验证码格式错误");
        Long expire = Long.valueOf(parts[1]);
        Long creationStamp = Long.valueOf(parts[2]);
        return System.currentTimeMillis() - creationStamp > expire;
    }
}