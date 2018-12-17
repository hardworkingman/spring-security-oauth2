package com.oauth2.demo.handler;

import com.oauth2.demo.exception.CustomOauthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        if (e instanceof InvalidTokenException) {
            InvalidTokenException invalidTokenException = (InvalidTokenException) e;
            if ("Token was not recognised".equals(invalidTokenException.getMessage())) {
                return ResponseEntity
                        .status(invalidTokenException.getHttpErrorCode())
                        .body(new CustomOauthException("用户名或密码错误。"));
            }
        }
        if (e instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new CustomOauthException(oAuth2Exception.getMessage()));
        } else if (e instanceof DisabledException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(new CustomOauthException(e.getMessage()));
        } else {
            return ResponseEntity
                    .status(500)
                    .body(new CustomOauthException(e.getMessage()));
        }
    }
}
