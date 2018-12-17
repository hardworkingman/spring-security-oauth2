package com.oauth2.demo.user.mapper;

import com.oauth2.demo.user.entity.AuthUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    static final String SQL_SELECT_FIELDS = "id, username, password, salt, nickName, avatarUrl, realName, birthday, sex, email, mobile, wxid, status, createTime, updateTime";

    @Insert("insert into sys_user (username, password, salt, nickName, avatarUrl, realName, birthday, sex, email, mobile, wxid, status, createTime, updateTime) "
            + " values(#{username}, #{password}, #{salt}, #{nickName}, #{avatarUrl}, #{realName}, #{birthday}, #{sex}, #{email}, #{mobile}, #{wxid}, #{status}, #{createTime}, #{updateTime}")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addUser(AuthUser user);

    @Insert("insert into sys_user (username,password,salt,mobile,status,createTime,updateTime) "
            + " values(#{username},#{password},#{salt},#{mobile},#{status},#{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int regUser(AuthUser user);

    @Update("update sys_user set password = #{password} where id = #{userId}")
    int changePassword(@Param("userId") Long userId, @Param("password") String password);

    @Update("update sys_user set status = '1' where id = #{userId} and status = '3' ")
    int passwordHasChanged(@Param("userId") Long userId);

    @Select("SELECT " + SQL_SELECT_FIELDS + " FROM `sys_user` where id = #{id}")
    AuthUser getUserById(@Param("id") Long id);

    @Select("SELECT " + SQL_SELECT_FIELDS + " FROM `sys_user` where username = #{username}")
    AuthUser getUserByUsername(@Param("username") String username);

    @Select("SELECT " + SQL_SELECT_FIELDS + " FROM `sys_user` where mobile = #{mobile}")
    AuthUser getUserByMobile(@Param("mobile") String mobile);

    @Insert(value = "insert into jobseeker (id) values(#{userId})")
    int addJobSeeker(@Param("userId") Long userId);

    @Insert(value = "insert into hrinfo (id) values(#{id})")
    int addHRInfo(@Param("id") Long id);

    @Update(value = "update sys_user set avatarUrl = #{avatarUrl}, realName = #{realName},sex = #{sex},birthday = #{birthday},email = #{email},mobile = #{mobile}, wxid = #{wxid} where id = #{id}")
    int updateUserInfo(AuthUser hlhloUser);

}
