package com.oauth2.demo.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "error")
@Slf4j
public class TokenErrorController extends BasicErrorController {
    private static final String PATH = "/error";

    public TokenErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        Map<String, Object> resultMap = new LinkedHashMap<>();
        HttpStatus status = getStatus(request);
        resultMap.put("code", status.value());
        resultMap.put("message", body.get("message"));
        resultMap.put("data", null);
        return new ResponseEntity<>(resultMap, status);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
