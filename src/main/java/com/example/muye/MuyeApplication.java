package com.example.muye;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 牧信通后端服务启动入口
 */
@SpringBootApplication
@MapperScan("com.example.muye.mapper")
public class MuyeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuyeApplication.class, args);
    }

}