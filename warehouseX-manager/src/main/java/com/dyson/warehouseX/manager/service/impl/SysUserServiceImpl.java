package com.dyson.warehouseX.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.dyson.model.dto.system.LoginDto;
import com.dyson.model.entity.system.SysUser;
import com.dyson.model.vo.common.ResultCodeEnum;
import com.dyson.model.vo.system.LoginVo;
import com.dyson.warehouseX.common.exception.PException;
import com.dyson.warehouseX.manager.mapper.SysUserMapper;
import com.dyson.warehouseX.manager.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //用户登录
    @Override
    public LoginVo login(LoginDto loginDto) {
        //1.获取提交用户名，loginDto获取到
        String userName = loginDto.getUserName();
        //2.根据用户名查询数据库表
        SysUser sysUser = sysUserMapper.selectUserInfoByUserName(userName);
        //3.如果根据用户名查不到对应信息，用户不存在，返回错误信息
        if(sysUser==null){
            throw new PException(ResultCodeEnum.LOGIN_ERROR);

        }
        //4.如果根据用户名查询到用户信息，用户存在
        //5.获取输入的密码，比较输入的密码和数据库密码是否一致
        String db_password=sysUser.getPassword();
        String input_password=DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());//将输入密码加密MD5
        if(!input_password.equals((db_password))){
            throw new PException(ResultCodeEnum.LOGIN_ERROR);
        }

        //6.如果密码一致，登录成功，如果不一致失败
        //7.登陆成功，生成token
        String token=UUID.randomUUID().toString().replaceAll("-","");
        //8.把登录成功用户信息放到redis里去
        //key；token value；用户信息
        redisTemplate.opsForValue()
                .set("user:login"+token,JSON.toJSONString(sysUser),
                        7, TimeUnit.DAYS);

        //9.返回loginVo
        LoginVo loginVo=new LoginVo();
        loginVo.setToken(token);

        return loginVo;
    }

    @Override
    public SysUser getUserInfo(String token) {
        String userJson=redisTemplate.opsForValue().get("user:login"+token);
        SysUser sysUser=JSON.parseObject(userJson,SysUser.class);
        return sysUser;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login"+token);
    }
}
