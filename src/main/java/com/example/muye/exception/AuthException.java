package com.example.muye.exception;

/**
 * 认证异常：用户名已存在、用户名不存在、密码错误等
 */
public class AuthException extends BusinessException {
    public AuthException(String message) {
        super(message);
    }
}