package com.dyson.warehouseX.manager.service;


import com.dyson.model.dto.system.AssignMenuDto;

import java.util.Map;

public interface SysRoleMenuService {
    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);

    //为职位分配菜单
    void doAssign(AssignMenuDto assignMenuDto);
}
