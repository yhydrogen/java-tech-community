package com.example.ssmstudy.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.ssmstudy.mapper.UserMapper;
import com.example.ssmstudy.model.User;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }
    public User register(String username, String rawPassword){
        int length = rawPassword.getBytes(StandardCharsets.UTF_8).length;
        if(length > 72){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "密码的UTF-8编码不能超过72字节");
        }
        User existingUser = userMapper.selectByUsername(username);
        if(existingUser != null){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "用户名已存在");
        }

        return user;
    }

    public User getCurrentUser(String username){
        User currentuser = userMapper.selectByUsername(username);
        if(currentuser == null){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "当前用户不存在");
        }
        return currentuser;
    }
}
