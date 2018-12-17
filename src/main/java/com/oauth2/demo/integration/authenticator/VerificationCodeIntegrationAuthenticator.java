package com.oauth2.demo.integration.authenticator;

import com.oauth2.demo.integration.IntegrationAuthentication;
import com.oauth2.demo.sms.service.impl.VerificationCodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

/**
 * 集成验证码认证
 **/
@Component
public class VerificationCodeIntegrationAuthenticator extends UsernamePasswordAuthenticator {

    private final static String VERIFICATION_CODE_AUTH_TYPE = "vc";

    @Autowired
    private VerificationCodeServiceImpl verificationCodeService;

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        String vcToken = integrationAuthentication.getAuthParameter("vc_token");
        String vcCode = integrationAuthentication.getAuthParameter("vc_code");
        //验证验证码
        boolean isValid = verificationCodeService.validate(vcToken, vcCode, null);

        if (!isValid) {
            throw new OAuth2Exception("验证码错误");
        }
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return VERIFICATION_CODE_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }
}