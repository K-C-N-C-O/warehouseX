package com.dyson.warehouseX.manager.service.impl;

import com.dyson.model.dto.system.AssignMenuDto;
import com.dyson.model.entity.system.SysMenu;
import com.dyson.warehouseX.manager.mapper.SysMenuMapper;
import com.dyson.warehouseX.manager.mapper.SysRoleMenuMapper;
import com.dyson.warehouseX.manager.service.SysMenuService;
import com.dyson.warehouseX.manager.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {
        //查询所有菜单
        List<SysMenu> sysMenuList = sysMenuService.findNodes();
        //查询职业分配过的菜单列表
        List<Long> roleMenuIds = sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId);

        Map<String, Object> map = new HashMap<>();
        map.put("sysMenuList", sysMenuList);
        map.put("roleMenuIds", roleMenuIds);
        return map;
    }

    //分配菜单
    @Override
    public void doAssign(AssignMenuDto assignMenuDto) {
        //删除职位分配过的菜单数据
        sysRoleMenuMapper.deleteByRoleId(assignMenuDto.getRoleId());
        //保存分配数据
        List<Map<String, Number>> menuInfo = assignMenuDto.getMenuIdList();
        if(menuInfo!=null && menuInfo.size()>0){
            sysRoleMenuMapper.doAssign(assignMenuDto);
        }



    }
}
