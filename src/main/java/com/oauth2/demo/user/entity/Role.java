package com.oauth2.demo.user.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Role {
    private Long id;

    private String name;

    private String value;

    private String tips;

    private Date createTime;

    private Date updateTime;

    private Integer status;
}
