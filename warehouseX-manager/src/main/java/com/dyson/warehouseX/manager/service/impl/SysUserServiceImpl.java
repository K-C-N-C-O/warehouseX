package com.dyson.warehouseX.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.dyson.model.dto.system.AssignRoleDto;
import com.dyson.model.dto.system.LoginDto;
import com.dyson.model.dto.system.SysUserDto;
import com.dyson.model.entity.system.SysUser;
import com.dyson.model.vo.common.ResultCodeEnum;
import com.dyson.model.vo.system.LoginVo;
import com.dyson.warehouseX.common.exception.PException;
import com.dyson.warehouseX.manager.mapper.SysRoleUserMapper;
import com.dyson.warehouseX.manager.mapper.SysUserMapper;
import com.dyson.warehouseX.manager.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

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


    //用户操作
    @Override
    public PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto) {
        PageHelper.startPage(pageNum,pageSize);
        List<SysUser> list=sysUserMapper.findByPage(sysUserDto);
        PageInfo<SysUser> pageInfo=new PageInfo<>(list);
        return null;
    }

    @Override
    public void saveSysUser(SysUser sysUser) {
        //判断用户名不能重复
        String username=sysUser.getUserName();
        SysUser dbSysUser = sysUserMapper.selectUserInfoByUserName(username);
        if(dbSysUser!=null){
            throw new PException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        //输入密码加密
        String md5_password=DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(md5_password);
        //设置status值
        sysUser.setStatus(1);

        sysUserMapper.save(sysUser);
    }

    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserMapper.update(sysUser);
    }

    @Override
    public void deleteById(Long userId) {
        sysUserMapper.delete(userId);
    }



    //用户分配角色
    @Override
    public void doAssign(AssignRoleDto assignRoleDto) {
        //1 根据userID删除用户之前分配的角色数据
        sysRoleUserMapper.deleteByUserId(assignRoleDto.getUserId());

        //2 重新分配新的数据
        List<Long> roleIdList=assignRoleDto.getRoleIdList();
        for(Long roleId:roleIdList){
            sysRoleUserMapper.doAssign(assignRoleDto.getUserId(),roleId);
        }



    }
}
