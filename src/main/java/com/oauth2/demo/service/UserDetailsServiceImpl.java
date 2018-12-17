package com.oauth2.demo.service;

import com.oauth2.demo.user.entity.AuthUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = new AuthUser();
        user.setUsername("admin");
        user.setPassword("{bcrypt}$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq");
        user.setEnabled(true);
        return user;
    }
}
