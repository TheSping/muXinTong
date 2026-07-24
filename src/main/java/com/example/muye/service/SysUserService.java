package com.example.muye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.muye.dto.UserAuthDTO;
import com.example.muye.entity.SysUser;
import com.example.muye.exception.AuthException;
import com.example.muye.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * 用户认证服务：注册与登录
 */
@Service
public class SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    private final String SALT = "MuxinTong2026_!@#";

    /**
     * 用户注册：校验用户名唯一性，MD5+盐加密后入库
     */
    public String register(UserAuthDTO dto) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", dto.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new AuthException("该用户名已被注册");
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((dto.getPassword() + SALT).getBytes());

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(encryptPassword);
        user.setPhone(dto.getPhone());
        user.setRole("USER");

        userMapper.insert(user);
        return "注册成功";
    }

    /**
     * 用户登录：校验用户名密码，返回 UUID 令牌
     */
    public String login(UserAuthDTO dto) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", dto.getUsername());
        SysUser user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new AuthException("用户名不存在");
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((dto.getPassword() + SALT).getBytes());
        if (!user.getPassword().equals(encryptPassword)) {
            throw new AuthException("密码错误");
        }

        return "TOKEN_" + UUID.randomUUID().toString().replace("-", "");
    }
}