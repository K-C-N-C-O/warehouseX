package com.dyson.warehouseX.manager.service;

import com.dyson.model.dto.system.LoginDto;
import com.dyson.model.entity.system.SysUser;
import com.dyson.model.vo.system.LoginVo;

public interface SysUserService {
    //用户登录
    LoginVo login(LoginDto loginDto);

    SysUser getUserInfo(String token);

    void logout(String token);
}
