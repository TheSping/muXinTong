package com.example.muye.common;

import com.example.muye.exception.AssetNotFoundException;
import com.example.muye.exception.AuthException;
import com.example.muye.exception.BlockchainException;
import com.example.muye.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器，将各类异常统一转换为 Result 格式返回
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return Result.error(msg);
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleAuth(AuthException ex) {
        return Result.error(401, ex.getMessage());
    }

    @ExceptionHandler(AssetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> handleNotFound(AssetNotFoundException ex) {
        return Result.error(404, ex.getMessage());
    }

    @ExceptionHandler(BlockchainException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Result<?> handleBlockchain(BlockchainException ex) {
        return Result.error(502, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBusiness(BusinessException ex) {
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleUnknown(Exception ex) {
        return Result.error(500, "服务器内部错误: " + ex.getMessage());
    }
}