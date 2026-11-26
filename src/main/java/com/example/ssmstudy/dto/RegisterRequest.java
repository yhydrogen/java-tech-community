package com.example.ssmstudy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "[A-Za-z0-9_]+", message = "用户名只能包含英文字母、数字和下划线")
    @Size(min = 3, max = 64, message = "用户名长度必须为3到64")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 72, message = "密码长度必须为8到72")
    private String password;

    public RegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
