package com.atomic.web.controller.system;

import java.util.List;

import com.atomic.common.core.domain.R;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.atomic.common.config.Global;
import com.atomic.common.core.controller.BaseController;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.system.domain.SysMenu;
import com.atomic.system.domain.SysUser;
import com.atomic.system.service.ISysMenuService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页 业务处理
 *
 * @author atomic
 */
@Controller
public class SysIndexController extends BaseController {
    @Autowired
    private ISysMenuService menuService;

    /**
     * 系统首页
      */
    @GetMapping("/index")
    public String index(ModelMap mmap) {
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单 默认展示“系统设置”子系统下的菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user,9999);
        List<SysMenu> sysMenus = menuService.selectSystemMenuByUser(user);
        mmap.put("sysMenus",sysMenus);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        return "index";
    }

    @GetMapping("/getMenuBySubSys/{parentId}")
    public String getMenuBySubSys(@PathVariable("parentId") int parentId,ModelMap mmap){
        SysUser user = ShiroUtils.getSysUser();
        List<SysMenu> menus = menuService.selectMenusByUser(user,parentId);
        List<SysMenu> sysMenus = menuService.selectSystemMenuByUser(user);
        mmap.put("sysMenus",sysMenus);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        return "index";
    }

    @GetMapping("/getSubSysMenuById/{parentId}")
    @ResponseBody
    public R getSubSysMenuById(@PathVariable("parentId") int parentId){
        SysUser user = ShiroUtils.getSysUser();
        List<SysMenu> menus = menuService.selectMenusByUser(user,parentId);
        return R.ok().put("data",menus);
    }

    /**
     * 系统介绍
     */
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "main";
    }

}
