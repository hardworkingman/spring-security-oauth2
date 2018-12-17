package com.oauth2.demo.service;

import com.oauth2.demo.client.entity.Client;
import com.oauth2.demo.client.service.impl.ClientServiceImpl;
import com.oauth2.demo.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于数据库可缓存的客户端服务
 */
@Service
@Slf4j
public class DatabaseCachableClientDetailsService implements ClientDetailsService, ClientRegistrationService {

    private static final String OAUTH_CLIENT_DETAILS_CACHE = "oauth_client_details";

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Cacheable(value = OAUTH_CLIENT_DETAILS_CACHE, key = "#clientId")
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<Client> clientOptional = Optional.of(clientService.getClientById(clientId));
        return clientOptional.map(entityToDomain).<ClientRegistrationException>orElseThrow(() -> new NoSuchClientException("Client ID not found"));
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        addClientDetailsWithCache(clientDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = OAUTH_CLIENT_DETAILS_CACHE, key = "clientDetails.clientId")
    public void addClientDetailsWithCache(ClientDetails clientDetails) {
        if (clientService.getClientById(clientDetails.getClientId()) != null) {
            throw new ClientAlreadyExistsException("客户端ID已存在");
        }
        Client client = Client.builder()
                .clientId(clientDetails.getClientId())
                .clientSecret(clientDetails.getClientSecret())
                .accessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds())
                .refreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds()).build();
        client.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes().stream().collect(Collectors.joining(",")));
        client.setRegisteredRedirectUris(clientDetails.getRegisteredRedirectUri().stream().collect(Collectors.joining(",")));
        client.setScope(clientDetails.getScope().stream().collect(Collectors.joining(",")));
        client.setAutoApproveScopes(clientDetails.getScope().stream().filter((scope) -> clientDetails.isAutoApprove(scope)).collect(Collectors.joining(",")));
        clientService.insert(client);
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        updateClientDetailsWithCache(clientDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = OAUTH_CLIENT_DETAILS_CACHE, key = "#clientDetails.clientId")
    public void updateClientDetailsWithCache(ClientDetails clientDetails) {
        Optional<Client> clientOptional = Optional.of(clientService.getClientById(clientDetails.getClientId()));
        clientOptional.orElseThrow(() -> new NoSuchClientException("没有找到Client ID"));

        Client client = Client.builder()
//                .clientId(clientDetails.getClientId())
                .clientSecret(clientDetails.getClientSecret())
                .accessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds())
                .refreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds()).build();
        client.setClientId(clientOptional.get().getClientId());

        client.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes().stream().collect(Collectors.joining(",")));
        client.setRegisteredRedirectUris(clientDetails.getRegisteredRedirectUri().stream().collect(Collectors.joining(",")));
        client.setScope(clientDetails.getScope().stream().collect(Collectors.joining(",")));
        client.setAutoApproveScopes(clientDetails.getScope().stream().filter((scope) -> clientDetails.isAutoApprove(scope)).collect(Collectors.joining(",")));

        clientService.insert(client);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        Optional<Client> clientOptional = Optional.of(clientService.getClientById(clientId));
        clientOptional.orElseThrow(() -> new NoSuchClientException("Client ID not found"));
        clientOptional.get().setClientSecret(passwordEncoder.encode(secret));
        clientService.update(clientOptional.get());
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        clientService.deleteById(clientId);
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return clientService.getAll().stream().map(entityToDomain).collect(Collectors.toList());
    }

    private final Function<? super Client, ? extends BaseClientDetails> entityToDomain = entity -> {

        BaseClientDetails clientDetails = new BaseClientDetails(entity.getClientId(), entity.getResourceIds(), entity.getScope(), entity.getAuthorizedGrantTypes(), entity.getAuthorities(), entity.getRegisteredRedirectUris());
        clientDetails.setClientSecret(entity.getClientSecret());
        if (entity.getAccessTokenValiditySeconds() != null) {
            clientDetails.setAccessTokenValiditySeconds(entity.getAccessTokenValiditySeconds());
        }
        if (entity.getRefreshTokenValiditySeconds() != null) {
            clientDetails.setRefreshTokenValiditySeconds(entity.getRefreshTokenValiditySeconds());
        }

        String information = entity.getAdditionalInformation();
        if (StringUtils.isNotBlank(information)) {
            try {
                Map map = JsonUtils.jsonToBean(information, Map.class);
                clientDetails.setAdditionalInformation(map);
            } catch (Exception e) {
                log.warn("Could not decode JSON for additional information: " + clientDetails, e);
            }
        }
        if (entity.getAutoApproveScopes() != null) {
            clientDetails.setAutoApproveScopes(org.springframework.util.StringUtils.commaDelimitedListToSet(entity.getAutoApproveScopes()));
        }

        return clientDetails;
    };
}
