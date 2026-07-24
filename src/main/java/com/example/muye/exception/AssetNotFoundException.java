package com.example.muye.exception;

/**
 * 资产不存在异常
 */
public class AssetNotFoundException extends BusinessException {
    public AssetNotFoundException(String message) {
        super(message);
    }
}