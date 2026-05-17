package com.example.muye.dto;

import lombok.Data;

@Data
public class UserAuthDTO {
    private String username;
    private String password;
    private String phone; // 注册时可选填
}