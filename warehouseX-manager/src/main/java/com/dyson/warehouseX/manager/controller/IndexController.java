package com.dyson.warehouseX.manager.controller;


import com.dyson.model.dto.system.LoginDto;
import com.dyson.model.entity.system.SysUser;
import com.dyson.model.vo.common.Result;
import com.dyson.model.vo.common.ResultCodeEnum;
import com.dyson.model.vo.system.LoginVo;
import com.dyson.warehouseX.manager.service.SysUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    //用户退出
    @GetMapping(value = "/logout")
    public  Result logout(@RequestHeader(name = "token")String token){
        sysUserService.logout(token);
        return Result.build(null,ResultCodeEnum.SUCCESS);

    }

    //获取当前登录用户信息
    @GetMapping(value ="/getUserInfo")
    public Result getUserInfo(@RequestHeader(name = "token")String token){
        //从请求头获取token
        //根据token查询redis获取用户信息
        SysUser sysUser=sysUserService.getUserInfo(token);
        return  Result.build(sysUser,ResultCodeEnum.SUCCESS);
        //用户信息返回
    }

    //用户登录
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto){
        LoginVo loginVo=sysUserService.login(loginDto);
        return  Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

}
