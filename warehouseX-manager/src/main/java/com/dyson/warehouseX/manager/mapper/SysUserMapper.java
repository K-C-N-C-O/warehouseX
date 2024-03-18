package com.dyson.warehouseX.manager.mapper;

import com.dyson.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {
    SysUser selectUserInfoByUserName(String userName);
}
