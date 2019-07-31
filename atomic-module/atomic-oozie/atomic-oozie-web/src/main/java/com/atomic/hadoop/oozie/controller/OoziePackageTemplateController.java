package com.atomic.hadoop.oozie.controller;

import cn.hutool.core.util.StrUtil;
import com.atomic.common.annotation.Log;
import com.atomic.common.core.controller.BaseController;
import com.atomic.common.core.domain.AjaxResult;
import com.atomic.common.core.domain.R;
import com.atomic.common.core.page.TableDataInfo;
import com.atomic.common.enums.BusinessType;
import com.atomic.common.utils.poi.ExcelUtil;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.oozie.domain.OoziePackageTemplate;
import com.atomic.hadoop.oozie.service.IOoziePackageTemplateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包模板 信息操作处理
 *
 * @author atomic
 * @date 2019-05-12
 */
@Controller
@RequestMapping("/oozie/ooziePackageTemplate")
public class OoziePackageTemplateController extends BaseController {
    private String prefix = "oozie/ooziePackageTemplate";

    @Autowired
    private IOoziePackageTemplateService ooziePackageTemplateService;

    @RequiresPermissions("oozie:ooziePackageTemplate:view")
    @GetMapping()
    public String ooziePackageTemplate(ModelMap mmap) {
        mmap.put("currentUserName", ShiroUtils.getLoginName());
        return prefix + "/ooziePackageTemplate";
    }

    @RequestMapping("/list/{projectId}")
    public String toPackageListTable(@PathVariable("projectId") String projectId, ModelMap mmap) {
        mmap.put("projectId", projectId);
        mmap.put("currentUserName", ShiroUtils.getLoginName());
        return prefix + "/ooziePackageTemplate";
    }

    /**
     * 查询任务包列表
     */
    @RequiresPermissions("oozie:ooziePackageTemplate:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OoziePackageTemplate ooziePackageTemplate) {
        startPage();
        List<OoziePackageTemplate> list = ooziePackageTemplateService.selectOoziePackageTemplateList(ooziePackageTemplate);
        return getDataTable(list);
    }

    @RequiresPermissions("oozie:ooziePackageTemplate:list")
    @PostMapping("/selectPackageWithNotUsedOfCommonPackage")
    @ResponseBody
    public TableDataInfo selectPackageWithNotUsedOfCommonPackage(OoziePackageTemplate ooziePackageTemplate) {
        startPage();
        List<OoziePackageTemplate> list = ooziePackageTemplateService.selectPackageWithNotUsedOfCommonPackage(ooziePackageTemplate);
        return getDataTable(list);
    }

    @RequiresPermissions("oozie:ooziePackageTemplate:list")
    @RequestMapping("/selectPackageById")
    @ResponseBody
    public R selectPackageById(String packageId){
        OoziePackageTemplate ooziePackageTemplate = ooziePackageTemplateService.selectOoziePackageTemplateById(packageId);
        return R.ok().put("data",ooziePackageTemplate);
    }


    /**
     * 导出任务包列表
     */
    @RequiresPermissions("oozie:ooziePackageTemplate:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OoziePackageTemplate ooziePackageTemplate) {
        List<OoziePackageTemplate> list = ooziePackageTemplateService.selectOoziePackageTemplateList(ooziePackageTemplate);
        ExcelUtil<OoziePackageTemplate> util = new ExcelUtil<OoziePackageTemplate>(OoziePackageTemplate.class);
        return util.exportExcel(list, "ooziePackageTemplate");
    }

    /**
     * 新增任务包
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 根据指定项目工程添加任务包
     * @param projectId
     * @param mmap
     * @return
     */
    @RequestMapping("/addByProjectId/{projectId}")
    public String addByProjectId(@PathVariable("projectId") String projectId,ModelMap mmap){
        mmap.put("projectId",projectId);
        return prefix + "/add";
    }

    /**
     * 新增保存任务包
     */
    @RequiresPermissions("oozie:ooziePackageTemplate:add")
    @Log(title = "任务包", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OoziePackageTemplate ooziePackageTemplate) {
        return toAjax(ooziePackageTemplateService.insertOoziePackageTemplate(ooziePackageTemplate));
    }

    /**
     * 修改任务包
     */
    @GetMapping("/edit/{packageId}")
    public String edit(@PathVariable("packageId") String packageId, ModelMap mmap) {
        OoziePackageTemplate ooziePackageTemplate = ooziePackageTemplateService.selectOoziePackageTemplateById(packageId);
        mmap.put("ooziePackageTemplate", ooziePackageTemplate);
        return prefix + "/edit";
    }

    /**
     * 修改保存包模板
     */
    @RequiresPermissions("oozie:ooziePackageTemplate:edit")
    @Log(title = "任务包", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OoziePackageTemplate ooziePackageTemplate) {
        return toAjax(ooziePackageTemplateService.updateOoziePackageTemplate(ooziePackageTemplate));
    }

    /**
     * 删除包模板
     */
    @RequiresPermissions("oozie:ooziePackageTemplate:remove")
    @Log(title = "任务包", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(ooziePackageTemplateService.deleteOoziePackageTemplateByIds(ids));
    }

    @PostMapping("checkPackageNameUnique")
    @ResponseBody
    public String checkPackageNameUnique(OoziePackageTemplate ooziePackageTemplate){
        List<OoziePackageTemplate> ooziePackageTemplates = ooziePackageTemplateService.selectOoziePackageTemplateList(ooziePackageTemplate);
        if (ooziePackageTemplates != null){
            int count = ooziePackageTemplates.size();
            if (count > 0){
                return "1";
            }
        }
        return "0";
    }

    /**
     * 文本框输入自动填充
     * @param request
     * @return
     */
    @RequestMapping("autocompletePackageName")
    @ResponseBody
    public List<Map<String, Object>> autocompletePackageName(HttpServletRequest request){
        String projectId = request.getParameter("projectId");
        String query = request.getParameter("term");
        OoziePackageTemplate ooziePackageTemplate = new OoziePackageTemplate();
        ooziePackageTemplate.setNameEn(query);
        if (StrUtil.isNotEmpty(projectId) && !"null".equals(projectId)){
            ooziePackageTemplate.setProjectId(Integer.parseInt(projectId));
        }
        List<OoziePackageTemplate> ooziePackageTemplates = ooziePackageTemplateService.selectOoziePackageTemplateList(ooziePackageTemplate);
        List<Map<String, Object>> suggestions = new ArrayList<>();
        for (OoziePackageTemplate packageTemplate:ooziePackageTemplates){
            Map<String, Object> map = new HashMap(2);
            map.put("value",packageTemplate.getNameEn());
            map.put("label",packageTemplate.getNameEn());
            suggestions.add(map);
        }
        return suggestions;
    }

    /**
     * 更新任务包为检出状态
     * @param ooziePackageTemplate
     * @return
     */
    @RequestMapping("updatePackageStatus")
    @ResponseBody
    public R updatePackageStatus(OoziePackageTemplate ooziePackageTemplate){
        ooziePackageTemplate.setDetection(ShiroUtils.getLoginName());
        int result = ooziePackageTemplateService.updateOoziePackageTemplate(ooziePackageTemplate);
        return R.ok().put("data",result);
    }

}
