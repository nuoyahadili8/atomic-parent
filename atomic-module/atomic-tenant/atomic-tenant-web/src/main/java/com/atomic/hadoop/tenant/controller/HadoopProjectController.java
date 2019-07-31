package com.atomic.hadoop.tenant.controller;

import com.atomic.common.annotation.Log;
import com.atomic.common.core.controller.BaseController;
import com.atomic.common.core.domain.AjaxResult;
import com.atomic.common.core.domain.R;
import com.atomic.common.core.domain.Ztree;
import com.atomic.common.core.page.TableDataInfo;
import com.atomic.common.enums.BusinessType;
import com.atomic.common.utils.poi.ExcelUtil;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.tenant.domain.HadoopProject;
import com.atomic.hadoop.tenant.domain.HadoopTenant;
import com.atomic.hadoop.tenant.service.IHadoopPlatformService;
import com.atomic.hadoop.tenant.service.IHadoopProjectDeptService;
import com.atomic.hadoop.tenant.service.IHadoopProjectService;
import com.atomic.hadoop.tenant.service.IHadoopTenantService;
import com.atomic.system.domain.SysUser;
import com.atomic.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工程项目 信息操作处理
 *
 * @author atomic
 * @date 2019-04-26
 */
@Controller
@RequestMapping("/hadoop/hadoopProject")
public class HadoopProjectController extends BaseController {
    private String prefix = "hadoop/hadoopProject";

    @Autowired
    private IHadoopProjectService hadoopProjectService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IHadoopPlatformService hadoopPlatformService;
    @Autowired
    private IHadoopTenantService hadoopTenantService;
    @Autowired
    private IHadoopProjectDeptService hadoopProjectDeptService;


    @RequiresPermissions("hadoop:hadoopProject:view")
    @GetMapping()
    public String hadoopProject() {
        return prefix + "/hadoopProject";
    }

    /**
     * 查询工程项目列表
     */
    @RequiresPermissions("hadoop:hadoopProject:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HadoopProject hadoopProject) {
        startPage();
        List<HadoopProject> list = hadoopProjectService.selectHadoopProjectList(hadoopProject);
        return getDataTable(list);
    }


    /**
     * 导出工程项目列表
     */
    @RequiresPermissions("hadoop:hadoopProject:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HadoopProject hadoopProject) {
        List<HadoopProject> list = hadoopProjectService.selectHadoopProjectList(hadoopProject);
        ExcelUtil<HadoopProject> util = new ExcelUtil<HadoopProject>(HadoopProject.class);
        return util.exportExcel(list, "hadoopProject");
    }

    /**
     * 新增工程项目
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存工程项目
     */
    @RequiresPermissions("hadoop:hadoopProject:add")
    @Log(title = "工程项目", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HadoopProject hadoopProject) {
        return toAjax(hadoopProjectService.insertHadoopProject(hadoopProject));
    }

    /**
     * 修改工程项目
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        HadoopProject hadoopProject = hadoopProjectService.selectHadoopProjectById(id);
        mmap.put("hadoopProject", hadoopProject);
        return prefix + "/edit";
    }

    /**
     * 修改保存工程项目
     */
    @RequiresPermissions("hadoop:hadoopProject:edit")
    @Log(title = "工程项目", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HadoopProject hadoopProject) {
        return toAjax(hadoopProjectService.updateHadoopProject(hadoopProject));
    }

    /**
     * 删除工程项目
     */
    @RequiresPermissions("hadoop:hadoopProject:remove")
    @Log(title = "工程项目", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(hadoopProjectService.deleteHadoopProjectByIds(ids));
    }

    @RequestMapping("/getManagerRoleUserByDeptId/{deptId}")
    @ResponseBody
    public R getManagerRoleUserByDeptId(@PathVariable("deptId") Long deptId) {
        List<SysUser> managerUsers = userService.selectUsersByDeptId(deptId);
        return R.ok().put("data", managerUsers);
    }

    @RequestMapping("checkProjectNameUnique")
    @ResponseBody
    public String checkProjectNameUnique(HadoopProject hadoopProject) {
        return hadoopProjectService.checkProjectNameUnique(hadoopProject);
    }

    /**
     * 选择hadoop集群平台树
     */
    @GetMapping("selectPlatformTree")
    public String selectPlatformTree() {
        return "hadoop/hadoopTenant/tree";
    }

    @RequestMapping("getTenant/{platformId}")
    @ResponseBody
    public R getTenant(@PathVariable("platformId") String platformId) {
        HadoopTenant tenant = new HadoopTenant();
        tenant.setPlatformId(platformId);
        List<HadoopTenant> hadoopTenants = hadoopTenantService.selectHadoopTenantList(tenant);
        return R.ok().put("data", hadoopTenants);
    }

    /**
     * 判断当前用户是否有对项目工程具有授权功能  1：表示有   0：表示没有
     *
     * @param projectId
     * @return
     */
    @RequestMapping("checkRight/{projectId}")
    @ResponseBody
    public R checkRight(@PathVariable("projectId") Integer projectId) {
        SysUser sysUser = ShiroUtils.getSysUser();
        if (sysUser.isAdmin()) {
            return R.ok().put("data", 1);
        }

        HadoopProject hadoopProject = hadoopProjectService.selectHadoopProjectById(projectId);
        Integer demanderUserId = hadoopProject.getDemanderUserId();
        if (Long.parseLong(demanderUserId + "") == sysUser.getUserId()) {
            return R.ok().put("data", 1);
        } else {
            return R.ok().put("data", 0);
        }
    }

    @RequestMapping("toScopePage/{projectId}")
    public String toScopePage(@PathVariable("projectId") Integer projectId, ModelMap mmap) {
        HadoopProject hadoopProject = hadoopProjectService.selectHadoopProjectById(projectId);
        mmap.put("hadoopProject", hadoopProject);
        return prefix + "/dataScope";
    }

    @PostMapping("authDataScope")
    @ResponseBody
    public R authDataScope(@RequestParam String deptIds, @RequestParam String projectId) {
        int result = hadoopProjectDeptService.saveOrUpdateHadoopProjectDept(deptIds,projectId);
        if (result>0){
            return R.ok().put("data", "success");
        }
        return R.ok().put("data", "error");
    }

    @RequestMapping("treeData")
    @ResponseBody
    public List<Ztree> treeData(@RequestParam Integer projectId){
        HadoopProject hadoopProject = hadoopProjectService.selectHadoopProjectById(projectId);
        return hadoopProjectService.projectDeptTreeData(hadoopProject);
    }


}
