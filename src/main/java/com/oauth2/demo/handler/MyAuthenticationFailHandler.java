package com.oauth2.demo.handler;

import com.oauth2.demo.util.JsonUtils;
import com.oauth2.demo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyAuthenticationFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("登录失败");
        //设置状态码
        httpServletResponse.setStatus(401);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        //将 登录失败 信息打包成json格式返回
        httpServletResponse.getWriter().write(JsonUtils.beanToJson(Result.buildFailure(401, "认证失败，请登录")));
    }
}
