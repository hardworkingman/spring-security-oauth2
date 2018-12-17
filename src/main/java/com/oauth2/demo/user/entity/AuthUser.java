package com.oauth2.demo.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class AuthUser implements UserDetails, CredentialsContainer, OAuth2User {

    private static final long serialVersionUID = 6102818663428470955L;
    private Long id;
    private String username;
    private String password;
    private String salt;
    private String nickName;
    private String avatarUrl;
    private String realName;
    private Date birthday;
    private String sex;
    private String email;
    private String mobile;
    private String wxid;
    private String status;
    private Date createTime;
    private Date updateTime;
    private boolean accountNonLocked;
    private boolean enabled;
    private Collection<String> resources = new ArrayList<>();
    private Collection<String> roles = new ArrayList<>();
    @JsonIgnore
    private Collection<SimpleGrantedAuthority> authorities;
    @JsonIgnore
    private Map<String, Object> attributes;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
            this.attributes.put("id", this.getId());
            this.attributes.put("username", this.getUsername());
            this.attributes.put("password", this.getPassword());
            this.attributes.put("salt", this.getSalt());
            this.attributes.put("nickName", this.getNickName());
            this.attributes.put("avatarUrl", this.getAvatarUrl());
            this.attributes.put("realName", this.getRealName());
            this.attributes.put("birthday", this.getBirthday());
            this.attributes.put("sex", this.getSex());
            this.attributes.put("email", this.getEmail());
            this.attributes.put("mobile", this.getMobile());
            this.attributes.put("wxid", this.getWxid());
            this.attributes.put("status", this.getStatus());
            this.attributes.put("createTime", this.getCreateTime());
            this.attributes.put("updateTime", this.getUpdateTime());
            this.attributes.put("accountNonLocked", this.isAccountNonLocked());
            this.attributes.put("enabled", this.isEnabled());
        }
        return attributes;
    }
}
