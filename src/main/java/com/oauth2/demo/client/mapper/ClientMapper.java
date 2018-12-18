package com.oauth2.demo.client.mapper;

import com.oauth2.demo.client.entity.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClientMapper {

    @Select("select client_id,client_secret,resource_ids,scope,authorized_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove " +
            " from oauth_client_details" +
            " where client_id = #{id}"
    )
    @Results({
            @Result(property = "registeredRedirectUris", column = "web_server_redirect_uri"),
            @Result(property = "accessTokenValiditySeconds", column = "access_token_validity"),
            @Result(property = "refreshTokenValiditySeconds", column = "refresh_token_validity"),
            @Result(property = "autoApproveScopes",column = "autoapprove")
    })
    Client getClientById(String id);

}
