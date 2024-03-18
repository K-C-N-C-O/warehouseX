package com.dyson.warehouseX.manager.service;

import com.dyson.model.dto.system.SysRoleDto;
import com.dyson.model.entity.system.SysRole;
import com.github.pagehelper.PageInfo;

public interface SysRoleService {
    PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit);

    void saveSysRole(SysRole sysRole);
}
