package com.oauth2.demo.handler;

import com.oauth2.demo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(value = {InvalidTokenException.class, RuntimeException.class, Exception.class})
    public Result grantError(HttpServletRequest request, HttpServletResponse response, RuntimeException e) {
        Result result = Result.buildFailure(response.getStatus(), e.getMessage(), e.toString());
        log.error("捕捉到异常发生：\r\n请求地址：{}\r\n异常信息：\r\n{}", request.getRequestURL(), e.getMessage());
        return result;
    }
}
