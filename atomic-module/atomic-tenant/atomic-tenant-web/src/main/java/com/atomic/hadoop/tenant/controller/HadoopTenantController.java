package com.atomic.hadoop.tenant.controller;

import com.atomic.common.annotation.Log;
import com.atomic.common.core.controller.BaseController;
import com.atomic.common.core.domain.AjaxResult;
import com.atomic.common.core.domain.Ztree;
import com.atomic.common.core.page.TableDataInfo;
import com.atomic.common.enums.BusinessType;
import com.atomic.common.utils.StringUtils;
import com.atomic.common.utils.poi.ExcelUtil;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.domain.HadoopTenant;
import com.atomic.hadoop.tenant.service.IHadoopPlatformService;
import com.atomic.hadoop.tenant.service.IHadoopTenantService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Hadoop平台租户模型 信息操作处理
 *
 * @author atomic
 * @date 2019-04-20
 */
@Controller
@RequestMapping("/hadoop/hadoopTenant")
public class HadoopTenantController extends BaseController {
    private String prefix = "hadoop/hadoopTenant";

    @Autowired
    private IHadoopTenantService hadoopTenantService;

    @Autowired
    private IHadoopPlatformService hadoopPlatformService;

    @RequiresPermissions("hadoop:hadoopTenant:view")
    @GetMapping()
    public String hadoopTenant() {
        return prefix + "/hadoopTenantList";
    }

    /**
     * 查询Hadoop平台租户模型列表
     */
    @RequiresPermissions("hadoop:hadoopTenant:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HadoopTenant hadoopTenant) {
        startPage();
        List<HadoopTenant> list = hadoopTenantService.selectHadoopTenantList(hadoopTenant);
        return getDataTable(list);
    }


    /**
     * 导出Hadoop平台租户模型列表
     */
    @RequiresPermissions("hadoop:hadoopTenant:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HadoopTenant hadoopTenant) {
        List<HadoopTenant> list = hadoopTenantService.selectHadoopTenantList(hadoopTenant);
        ExcelUtil<HadoopTenant> util = new ExcelUtil<HadoopTenant>(HadoopTenant.class);
        return util.exportExcel(list, "hadoopTenant");
    }

    /**
     * 新增Hadoop平台租户模型
     */
    @GetMapping("/add")
    public String add(HttpServletRequest request,ModelMap mmap) {
        String platformId=request.getParameter("id");
        HadoopPlatform hadoopPlatform = null;
        if (!StringUtils.isEmpty(platformId)){
            hadoopPlatform = hadoopPlatformService.selectHadoopPlatformById(platformId);
        }
        mmap.put("hadoopPlatform",hadoopPlatform);
        return prefix + "/add";
    }

    /**
     * 新增保存Hadoop平台租户模型
     */
    @RequiresPermissions("hadoop:hadoopTenant:add")
    @Log(title = "Hadoop平台租户模型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HadoopTenant hadoopTenant) {
        String username = ShiroUtils.getLoginName();
        hadoopTenant.setCreateUser(username);
        hadoopTenant.setCreateTime(new Date());
        if ("on".equals(hadoopTenant.getIsEnable())){
            hadoopTenant.setIsEnable("1");
        }else{
            hadoopTenant.setIsEnable("0");
        }
        return toAjax(hadoopTenantService.insertHadoopTenant(hadoopTenant));
    }

    /**
     * 修改Hadoop平台租户模型
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        HadoopTenant hadoopTenant = hadoopTenantService.selectHadoopTenantById(id);
        mmap.put("hadoopTenant", hadoopTenant);
        return prefix + "/edit";
    }

    /**
     * 修改保存Hadoop平台租户模型
     */
    @RequiresPermissions("hadoop:hadoopTenant:edit")
    @Log(title = "Hadoop平台租户模型", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HadoopTenant hadoopTenant) {
        if ("on".equals(hadoopTenant.getIsEnable())){
            hadoopTenant.setIsEnable("1");
        }else{
            hadoopTenant.setIsEnable("0");
        }
        return toAjax(hadoopTenantService.updateHadoopTenant(hadoopTenant));
    }

    /**
     * 删除Hadoop平台租户模型
     */
    @RequiresPermissions("hadoop:hadoopTenant:remove")
    @Log(title = "Hadoop平台租户模型", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(hadoopTenantService.deleteHadoopTenantByIds(ids));
    }

    @RequestMapping("treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = hadoopPlatformService.selectPlatformTree(new HadoopPlatform());
        return ztrees;
    }

    /**
     * 选择hadoop集群平台树
     */
    @GetMapping("/selectPlatformTree/{platformId}")
    public String selectPlatformTree(@PathVariable("platformId") String platformId, ModelMap mmap) {
        mmap.put("platform", hadoopPlatformService.selectHadoopPlatformById(platformId));
        return prefix + "/tree";
    }

}
