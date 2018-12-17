package com.oauth2.demo.integration.authenticator.sms;

import com.oauth2.demo.integration.AuthType;
import com.oauth2.demo.integration.IntegrationAuthentication;
import com.oauth2.demo.integration.authenticator.AbstractPreparableIntegrationAuthenticator;
import com.oauth2.demo.integration.authenticator.sms.event.SmsAuthenticateBeforeEvent;
import com.oauth2.demo.integration.authenticator.sms.event.SmsAuthenticateSuccessEvent;
import com.oauth2.demo.sms.service.impl.VerificationCodeServiceImpl;
import com.oauth2.demo.user.entity.AuthUser;
import com.oauth2.demo.user.service.impl.UserServiceImpl;
import com.oauth2.demo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

/**
 * 短信验证码集成认证
 **/
@Component
@Slf4j
public class SmsIntegrationAuthenticator extends AbstractPreparableIntegrationAuthenticator implements ApplicationEventPublisherAware {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private VerificationCodeServiceImpl verificationCodeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public AuthUser authenticate(IntegrationAuthentication integrationAuthentication) {
        //获取密码，实际值是验证码
        String password = integrationAuthentication.getAuthParameter("password");
        //获取用户名，实际值是手机号
        String username = integrationAuthentication.getUsername();
        //发布事件，可以监听事件进行自动注册用户
        this.applicationEventPublisher.publishEvent(new SmsAuthenticateBeforeEvent(integrationAuthentication));
        //通过手机号码查询用户
        log.info("手机验证码登录，根据用户名查询用户前。");
        AuthUser sysUserAuthentication = userService.findByUsername(username);
        log.info("手机验证码登录，根据用户名查询用户后。");
        if (sysUserAuthentication != null) {
            //将密码设置为验证码
            String salt = sysUserAuthentication.getSalt();
            String checkPassword = StringUtils.join(password,salt);
            sysUserAuthentication.setPassword(passwordEncoder.encode(checkPassword));
            //发布事件，可以监听事件进行消息通知
            this.applicationEventPublisher.publishEvent(new SmsAuthenticateSuccessEvent(integrationAuthentication));
        }
        return sysUserAuthentication;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        String smsToken = integrationAuthentication.getAuthParameter("sms_token");
        String smsCode = integrationAuthentication.getAuthParameter("password");
        String username = integrationAuthentication.getAuthParameter("username");
        boolean validate = verificationCodeService.validate(smsToken, smsCode, username);
        Result<Boolean> result = Result.buildSuccessData(validate);
          if (!result.getData()) {
            throw new OAuth2Exception("验证码错误或已过期。");
        }
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return AuthType.SMS_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
