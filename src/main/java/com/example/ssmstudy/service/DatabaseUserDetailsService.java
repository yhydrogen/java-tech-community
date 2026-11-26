package com.example.ssmstudy.service;


import com.example.ssmstudy.mapper.UserMapper;
import com.example.ssmstudy.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    public DatabaseUserDetailsService(UserMapper usermapper) {
        this.userMapper = usermapper;
    }
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles("USER")
                .build();
    }
}
