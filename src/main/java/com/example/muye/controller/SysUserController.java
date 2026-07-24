package com.example.muye.controller;

import com.example.muye.common.Result;
import com.example.muye.dto.UserAuthDTO;
import com.example.muye.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户认证接口：注册与登录
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody UserAuthDTO req) {
        Map<String, Object> data = new HashMap<>();
        String msg = userService.register(req);
        data.put("message", msg);
        return Result.success(data);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody UserAuthDTO req) {
        Map<String, Object> data = new HashMap<>();
        String token = userService.login(req);
        data.put("token", token);
        return Result.success("登录成功", data);
    }
}