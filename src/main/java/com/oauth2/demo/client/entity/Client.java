package com.oauth2.demo.client.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client implements Serializable {
    private static final long serialVersionUID = 979634719050473153L;
    private String clientId;
    private String clientSecret;
    private String scope;
    private String resourceIds;
    private String authorizedGrantTypes;
    private String registeredRedirectUris;
    private String autoApproveScopes;
    private String authorities;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private String additionalInformation;
}
