package com.example.muye.exception;

/**
 * 业务异常基类，所有自定义异常均继承此类
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}