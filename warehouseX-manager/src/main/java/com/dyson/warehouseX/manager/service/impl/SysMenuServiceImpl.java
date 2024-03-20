package com.dyson.warehouseX.manager.service.impl;

import com.dyson.model.entity.system.SysMenu;
import com.dyson.model.entity.system.SysUser;
import com.dyson.model.vo.common.ResultCodeEnum;
import com.dyson.model.vo.system.SysMenuVo;
import com.dyson.warehouseX.common.exception.PException;
import com.dyson.warehouseX.manager.mapper.SysMenuMapper;
import com.dyson.warehouseX.manager.mapper.SysRoleMenuMapper;
import com.dyson.warehouseX.manager.service.SysMenuService;
import com.dyson.warehouseX.manager.utils.MenuHelper;
import com.dyson.warehouseX.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private  SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        //查询所有菜单，返回list
        List<SysMenu> sysMenuList=sysMenuMapper.findAll();
        if(CollectionUtils.isEmpty(sysMenuList)){
            return null;
        }
        //调用工具类，把返回list封装成要求的数据格式
        List<SysMenu> treeList=MenuHelper.buildTree(sysMenuList);
        return treeList;

    }

    @Override
    public void save(SysMenu sysMenu) {

        sysMenuMapper.save(sysMenu);

        //新添加子菜单，将父菜单isHalf设为半开状态1
        updateSysRoleMenu(sysMenu);
    }

    private void updateSysRoleMenu(SysMenu sysMenu) {
        //获取父菜单
        SysMenu parentMenu=sysMenuMapper.selectParentMenu(sysMenu.getParentId());
        if(parentMenu!=null){
            //is_half=1
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getId());
            // 递归调用
            updateSysRoleMenu(parentMenu);
        }


    }

    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.update(sysMenu);
    }

    @Override
    public void removeById(Long id) {
        // 先查询是否存在子菜单，如果存在不允许进行删除
        int count = sysMenuMapper.selectCountById(id);
        if (count > 0) {
            throw new PException(ResultCodeEnum.NODE_ERROR);
        }
        sysMenuMapper.delete(id);
    }


    //查询用户可以操作的菜单
    @Override
    public List<SysMenuVo> findMenusByUserId() {
        //获取当前登录用户id
        SysUser sysUser=AuthContextUtil.get();
        Long userId=sysUser.getId();
        //根据用户id查询可以操作菜单
        List<SysMenu> list=sysMenuMapper.findMenusByUserId(userId);
        //封装
        List<SysMenu> sysMenuList = MenuHelper.buildTree(list);
        List<SysMenuVo> sysMenuVos=this.buildMenus(sysMenuList);
        return sysMenuVos;
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
