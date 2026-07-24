package com.example.muye.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户注册/登录请求参数
 */
@Data
public class UserAuthDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String phone;
}