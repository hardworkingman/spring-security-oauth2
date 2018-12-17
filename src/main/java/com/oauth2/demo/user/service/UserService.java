package com.oauth2.demo.user.service;

import com.oauth2.demo.user.entity.AuthUser;

public interface UserService {
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return
     */
    AuthUser findByUsername(String username);
}
