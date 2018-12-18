package com.oauth2.demo.service;

import com.oauth2.demo.integration.IntegrationAuthentication;
import com.oauth2.demo.integration.IntegrationAuthenticationContext;
import com.oauth2.demo.integration.authenticator.IntegrationAuthenticator;
import com.oauth2.demo.user.entity.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private List<IntegrationAuthenticator> authenticators;

    @Autowired(required = false)
    public void setAuthenticators(List<IntegrationAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        IntegrationAuthentication integrationAuthentication = IntegrationAuthenticationContext.get();
        // 判断是否是集成登录
        if (integrationAuthentication == null) {
            integrationAuthentication = new IntegrationAuthentication();
        }
        integrationAuthentication.setUsername(username);
        AuthUser authUser = authenticate(integrationAuthentication);

        if (authUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        setAuthorize(authUser);
        authUser.setEnabled(true);
        authUser.setAccountNonLocked(true);
        return authUser;
    }

    /**
     * 设置授权信息
     *
     * @param user
     */
    private void setAuthorize(AuthUser user) {
        // TODO 设置角色权限信息
    }

    private AuthUser authenticate(IntegrationAuthentication integrationAuthentication) {
        if (this.authenticators != null) {
            for (IntegrationAuthenticator authenticator :authenticators) {
                if (authenticator.support(integrationAuthentication)) {
                    return authenticator.authenticate(integrationAuthentication);
                }
            }
        }
        return null;
    }
}
