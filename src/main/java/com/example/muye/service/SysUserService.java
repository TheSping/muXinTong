package com.example.muye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.muye.dto.UserAuthDTO;
import com.example.muye.entity.SysUser;
import com.example.muye.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Service
public class SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    // 密码加密盐值 (增加破解难度)
    private final String SALT = "MuxinTong2026_!@#";

    /**
     * 注册逻辑
     */
    public String register(UserAuthDTO dto) {
        // 1. 检查账号是否已存在
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", dto.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("该用户名已被注册！");
        }

        // 2. 密码加密 (MD5 + 盐)
        String encryptPassword = DigestUtils.md5DigestAsHex((dto.getPassword() + SALT).getBytes());

        // 3. 存入数据库
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(encryptPassword);
        user.setPhone(dto.getPhone());
        user.setRole("USER"); // 默认普通用户

        userMapper.insert(user);
        return "注册成功！";
    }

    /**
     * 登录逻辑
     */
    public String login(UserAuthDTO dto) {
        // 1. 按用户名查出用户
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", dto.getUsername());
        SysUser user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new RuntimeException("用户名不存在！");
        }

        // 2. 校验密码
        String encryptPassword = DigestUtils.md5DigestAsHex((dto.getPassword() + SALT).getBytes());
        if (!user.getPassword().equals(encryptPassword)) {
            throw new RuntimeException("密码错误！");
        }

        // 3. 登录成功，生成 Token 通行证 (这里先用简单的 UUID 模拟，后续可升级为 JWT)
        String token = "TOKEN_" + UUID.randomUUID().toString().replace("-", "");

        return token;
    }
}