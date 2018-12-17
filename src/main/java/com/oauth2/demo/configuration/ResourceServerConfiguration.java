package com.oauth2.demo.configuration;

import com.oauth2.demo.customer.AuthExceptionEntryPoint;
import com.oauth2.demo.customer.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests().antMatchers("/v2/api-docs","/sms/token").permitAll().anyRequest().authenticated()
            .and().logout().permitAll()
            .and().formLogin().permitAll()
            .and().exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()).authenticationEntryPoint(new AuthExceptionEntryPoint());
    }
}