package com.dyson.warehouseX.manager.service;

import com.dyson.model.dto.system.AssignRoleDto;
import com.dyson.model.dto.system.LoginDto;
import com.dyson.model.dto.system.SysUserDto;
import com.dyson.model.entity.system.SysUser;
import com.dyson.model.vo.system.LoginVo;
import com.github.pagehelper.PageInfo;

public interface SysUserService {
    //用户登录
    LoginVo login(LoginDto loginDto);

    SysUser getUserInfo(String token);

    void logout(String token);

    //用户操作
    PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);

    void saveSysUser(SysUser sysUser);

    void updateSysUser(SysUser sysUser);

    void deleteById(Long userId);

    void doAssign(AssignRoleDto assignRoleDto);
}
