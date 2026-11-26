package com.example.ssmstudy.mapper;

import com.example.ssmstudy.model.User;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper {
    @Select("SELECT id, username, passwordhash FROM app_user WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);
    @Insert("INSERT INTO app_user (username, passwordhash) VALUES (#{username}, #{passwordHash})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}
