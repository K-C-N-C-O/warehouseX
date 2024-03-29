package com.dyson.warehouseX.utils;

import com.dyson.model.entity.system.SysUser;

public class AuthContextUtil {

    private static final ThreadLocal<SysUser> threadLocal=new ThreadLocal<>();

    public static void set(SysUser sysUser){
        threadLocal.set(sysUser);
    }

    public static SysUser get(){
        return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }



}
