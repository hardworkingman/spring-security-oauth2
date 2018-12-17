package com.oauth2.demo.user.service.impl;

import com.oauth2.demo.user.entity.AuthUser;
import com.oauth2.demo.user.mapper.UserMapper;
import com.oauth2.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public AuthUser findByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}
