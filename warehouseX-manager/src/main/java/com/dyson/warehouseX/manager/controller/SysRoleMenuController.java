package com.dyson.warehouseX.manager.controller;

import com.dyson.model.dto.system.AssignMenuDto;
import com.dyson.model.vo.common.Result;
import com.dyson.model.vo.common.ResultCodeEnum;
import com.dyson.warehouseX.manager.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/system/sysRoleMenu")
public class SysRoleMenuController {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    //查询所有菜单以及职位分配过的菜单列表
    @GetMapping(value = "/findSysRoleMenuByRoleId/{roleId}")
    public Result findSysRoleMenuByRoleId(@PathVariable("roleId") Long roleId){
        Map<String,Object> map=sysRoleMenuService.findSysRoleMenuByRoleId(roleId);
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }

    //保存职位分配菜单
    @PostMapping(value = "/doAssign")
    public Result doAssign(@RequestBody AssignMenuDto assignMenuDto){

        sysRoleMenuService.doAssign(assignMenuDto);
        return Result.build(null,ResultCodeEnum.SUCCESS);


    }


}
