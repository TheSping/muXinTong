package com.example.muye.controller;

import com.example.muye.dto.UserAuthDTO;
import com.example.muye.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    // 1. 注册接口
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody UserAuthDTO req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String msg = userService.register(req);
            result.put("code", 200);
            result.put("message", msg);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("message", e.getMessage());
        }
        return result;
    }

    // 2. 登录接口
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody UserAuthDTO req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String token = userService.login(req);
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("token", token); // 前端需要把这个 token 存在 localStorage 里
        } catch (Exception e) {
            result.put("code", 400);
            result.put("message", e.getMessage());
        }
        return result;
    }
}