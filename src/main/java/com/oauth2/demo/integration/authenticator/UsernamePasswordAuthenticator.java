package com.oauth2.demo.integration.authenticator;

import com.oauth2.demo.integration.IntegrationAuthentication;
import com.oauth2.demo.user.entity.AuthUser;
import com.oauth2.demo.user.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 默认登录处理
 **/
@Component
@Primary
public class UsernamePasswordAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public AuthUser authenticate(IntegrationAuthentication integrationAuthentication) {
        return userService.findByUsername(integrationAuthentication.getUsername());
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {

    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return StringUtils.isEmpty(integrationAuthentication.getAuthType());
    }
}