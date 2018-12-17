package com.oauth2.demo.provider;

import com.oauth2.demo.user.entity.AuthUser;
import com.oauth2.demo.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service("authProvider")
@Slf4j
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("MyAuthenticationProvider：authenticate");
        // TODO 获取登录的用户名，密码
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // TODO 根据用户名查询数据库用户信息
        AuthUser authUser = userDetailsService.loadUserByUsername(username);
        if (authUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        if (!authUser.isEnabled()) {
            throw new DisabledException("用户已被禁用");
        }
        String salt = authUser.getSalt();
        String checkPassword = StringUtils.join(password, salt);
        // TODO 验证密码
        if (!passwordEncoder.matches(checkPassword, authUser.getPassword())) {
            throw new InvalidGrantException("用户名或密码错误");
        }
        Collection<SimpleGrantedAuthority> authorities = authUser.getAuthorities();
        return new UsernamePasswordAuthenticationToken(authUser, password, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.equals(aClass);
    }
}
