package com.example.muye.exception;

/**
 * 区块链交互异常：上链失败、查询失败等
 */
public class BlockchainException extends BusinessException {
    public BlockchainException(String message) {
        super(message);
    }

    public BlockchainException(String message, Throwable cause) {
        super(message, cause);
    }
}