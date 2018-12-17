package com.oauth2.demo.sms.service.impl;

import com.oauth2.demo.sms.service.VerificationCodeCache;
import com.oauth2.demo.sms.service.VerificationCodeService;
import com.oauth2.demo.sms.utils.VerificationCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 系统验证码服务类
 *
 * @author LIQIU
 * @date 2018-1-2
 */
@Service("verificationCodeService")
@Slf4j
@EnableCaching
@CacheConfig(cacheNames = {VerificationCodeService.VERIFICATION_CODE_CACHE_NAME,
        VerificationCodeService.VERIFICATION_CODE_SMS_SEND_CACHE_NAME})
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationCodeCache verificationCodeCache;

    /**
     * 默认验证码长度
     */
    @Value("${vc.size:4}")
    private Integer verificationCodeSize;

    /**
     * 发送验证码短信模板
     */
    @Value("${vc.sms.template-code:SMS_143868260}")
    private String templateCode;

    /**
     * 验证码参数名称
     */
    @Value("${vc.sms.code-param-name:code}")
    private String codeParamName;

    /**
     * 验证码参数名称
     */
    @Value("${vc.sms.sign-name:clever}")
    private String signName;

    /**
     * 默认验证码失效时间，默认为5分钟
     */
    @Value("${vc.expire:300000}")
    private Long verificationCodeExpireTime;

    /**
     * 短信验证码发送间隔,默认为1分钟
     */
    @Value("${vc.sms.interval:60000}")
    private Long smsSendInterval;

    /**
     * 数字验证码
     */
    private static final String NUMBER_VRIFICATION_CODE = "0";

    /**
     * 数字英文混合验证码
     */
    private static final String MIXED_VRIFICATION_CODE = "1";

    @Override
    public String getToken(Integer size, Long expire, String type, String subject) {
        if (size == null) {
            size = this.verificationCodeSize;
        }
        if (expire == null) {
            expire = this.verificationCodeExpireTime;
        }
        String code = "";
        if (NUMBER_VRIFICATION_CODE.equals(type)) {
            code = VerificationCodeUtils.generateNumberVerifyCode(size);
        } else {
            code = VerificationCodeUtils.generateVerifyCode(size);
        }

        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String realToken = token;
        if (StringUtils.isNotEmpty(subject)) {
            realToken = subject + "@" + token;
        }

        if (log.isInfoEnabled()) {
            log.info("【验证码】手机号：{}，验证码：{}，TOKEN：{}。", subject, code, token);
        }
        this.putCache(realToken, code, expire);
        return token;
    }

    @Override
    public Map<String, String> getTokenAndCode(Integer size, Long expire, String type, String subject) {
        if (size == null) {
            size = this.verificationCodeSize;
        }
        if (expire == null) {
            expire = this.verificationCodeExpireTime;
        }
        String code = "";
        if (NUMBER_VRIFICATION_CODE.equals(type)) {
            code = VerificationCodeUtils.generateNumberVerifyCode(size);
        } else {
            code = VerificationCodeUtils.generateVerifyCode(size);
        }

        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String realToken = token;
        if (StringUtils.isNotEmpty(subject)) {
            realToken = subject + "@" + token;
        }

        if (log.isInfoEnabled()) {
            log.info("【验证码】手机号：{}，验证码：{}，TOKEN：{}。", subject, code, token);
        }
        this.putCache(realToken, code, expire);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("code", code);
        return map;
    }


    @Override
    public void refresh(String token) {
        this.refresh(token, verificationCodeExpireTime);
    }

    @Override
    public void refresh(String token, long expire) {
        String code = this.getCode(token);
        this.updateCache(token, code, expire);
    }

    @Override
    public void sendSms(String token, String phoneNumber) {
        this.sendSms(token, this.templateCode, this.signName, phoneNumber, this.codeParamName, null);
    }

    @Override
    public void sendSms(String token, String templateCode, String signName, String phoneNumber, String codeParamName, Map<String, Object> params) {
        Assert.isTrue(StringUtils.isNotEmpty(templateCode), "短信模板没有配置");
        Assert.isTrue(StringUtils.isNotEmpty(signName), "短信模板没有没有配置");
        Assert.isTrue(StringUtils.isNotEmpty(codeParamName), "验证码参数不能为空");
        if (params == null) {
            params = new HashMap<>();
        }

        token = phoneNumber + "@" + token;

        //验证是否已经过期
        boolean isExpired = this.verificationCodeCache.isExpire(VERIFICATION_CODE_CACHE_NAME, token);
        if (isExpired) {
            this.evictCache(token);
        }
        Assert.isTrue(!isExpired, "验证码已过期");

        //判断短信验证码的发送间隔
        validateSmsSendInterval(token);

        //通过通知中心发送通知
//        SmsNotification smsNotification = new SmsNotification();
//        smsNotification.setPhoneNumber(phoneNumber);
//        smsNotification.setSignName(signName);
//        smsNotification.setTemplateCode(templateCode);
//        String value = this.getCode(token);
//        params.put(codeParamName, value);
//        smsNotification.setParams(params);
//        notificationClient.send(smsNotification);
        setSmsSendTimeStamp(token);
    }

    /**
     * 设置短信发送的时间戳
     *
     * @param token
     */
    private void setSmsSendTimeStamp(String token) {
        this.verificationCodeCache.set(VERIFICATION_CODE_SMS_SEND_CACHE_NAME, token, String.valueOf(System.currentTimeMillis()), smsSendInterval);
    }

    /**
     * 验证短信发送的间隔
     *
     * @param token
     */
    private void validateSmsSendInterval(String token) {
        String timeStamp = this.verificationCodeCache.get(VERIFICATION_CODE_SMS_SEND_CACHE_NAME, token);
        Assert.isTrue(StringUtils.isEmpty(timeStamp), "短信发送间隔时间太短，请稍后再试");
    }

    @Override
    public void renderImage(String token, OutputStream outputStream) throws IOException {
        // 生成随机字串
        String value = this.getCode(token);
        // 生成图片
        int w = 100, h = 30;
        VerificationCodeUtils.outputImage(w, h, outputStream, value);
    }

    @Override
    public boolean validate(String token, String code, String subject) {
        return this.validate(token, code, subject, true);
    }

    @Override
    public boolean validate(String token, String code, String subject, boolean ignoreCase) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        if (StringUtils.isNotEmpty(subject)) {
            token = subject + "@" + token;
        }
        String value = this.verificationCodeCache.get(VERIFICATION_CODE_CACHE_NAME, token);

        if (StringUtils.isEmpty(value)) {
            return false;
        }

        return ignoreCase ? code.equalsIgnoreCase(value) : code.equals(value);
    }

    public String getCode(String token) {
        return this.verificationCodeCache.get(VERIFICATION_CODE_CACHE_NAME, token);
    }

    /**
     * 放入缓存
     *
     * @param token
     * @param code
     */
    private void putCache(String token, String code, long expire) {
        verificationCodeCache.set(VERIFICATION_CODE_CACHE_NAME, token, code, expire);
    }

    /**
     * 删除缓存
     *
     * @param token
     */
    private void evictCache(String token) {
        verificationCodeCache.remove(VERIFICATION_CODE_CACHE_NAME, token);
    }

    /**
     * 更新缓存
     *
     * @param token
     * @param code
     * @param expire
     */
    private void updateCache(String token, String code, long expire) {
        this.evictCache(token);
        verificationCodeCache.set(VERIFICATION_CODE_CACHE_NAME, token, code, expire);
    }
}