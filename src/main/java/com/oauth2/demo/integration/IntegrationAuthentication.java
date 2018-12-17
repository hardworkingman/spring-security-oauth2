package com.oauth2.demo.integration;

import lombok.Data;

import java.util.Map;

@Data
public class IntegrationAuthentication {

    private String authType;
    private String username;
    private Map<String,String[]> authParameters;
    private String unionId;
    private String openId;

    public String getAuthParameter(String paramter){
        String[] values = this.authParameters.get(paramter);
        if(values != null && values.length > 0){
            return values[0];
        }
        return null;
    }
}
