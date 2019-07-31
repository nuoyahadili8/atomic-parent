package com.atomic.hadoop.oozie.controller;

import com.atomic.common.annotation.Log;
import com.atomic.common.core.controller.BaseController;
import com.atomic.common.core.domain.AjaxResult;
import com.atomic.common.core.domain.R;
import com.atomic.common.core.page.TableDataInfo;
import com.atomic.common.enums.BusinessType;
import com.atomic.common.utils.poi.ExcelUtil;
import com.atomic.hadoop.oozie.domain.OozieSubsystem;
import com.atomic.hadoop.oozie.service.IOozieSubsystemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 子系统 信息操作处理
 * 
 * @author atomic
 * @date 2019-05-19
 */
@Controller
@RequestMapping("/oozie/oozieSubsystem")
public class OozieSubsystemController extends BaseController {
    private String prefix = "oozie/oozieSubsystem";
	
	@Autowired
	private IOozieSubsystemService oozieSubsystemService;
	
	@RequiresPermissions("oozie:oozieSubsystem:view")
	@GetMapping()
	public String oozieSubsystem()
	{
	    return prefix + "/oozieSubsystem";
	}
	
	/**
	 * 查询子系统列表
	 */
	@RequiresPermissions("oozie:oozieSubsystem:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(OozieSubsystem oozieSubsystem) {
		startPage();
        List<OozieSubsystem> list = oozieSubsystemService.selectOozieSubsystemList(oozieSubsystem);
		return getDataTable(list);
	}

	@RequiresPermissions("oozie:oozieSubsystem:list")
	@RequestMapping("getOozieSubSystemList")
	@ResponseBody
	public R getOozieSubSystemList(){
		OozieSubsystem oozieSubsystem = new OozieSubsystem();
		List<OozieSubsystem> list = oozieSubsystemService.selectOozieSubsystemList(oozieSubsystem);
		return R.ok().put("data",list);
	}
	
	
	/**
	 * 导出子系统列表
	 */
	@RequiresPermissions("oozie:oozieSubsystem:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OozieSubsystem oozieSubsystem) {
    	List<OozieSubsystem> list = oozieSubsystemService.selectOozieSubsystemList(oozieSubsystem);
        ExcelUtil<OozieSubsystem> util = new ExcelUtil<OozieSubsystem>(OozieSubsystem.class);
        return util.exportExcel(list, "oozieSubsystem");
    }
	
	/**
	 * 新增子系统
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存子系统
	 */
	@RequiresPermissions("oozie:oozieSubsystem:add")
	@Log(title = "子系统", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(OozieSubsystem oozieSubsystem) {
		return toAjax(oozieSubsystemService.insertOozieSubsystem(oozieSubsystem));
	}

	/**
	 * 修改子系统
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
		OozieSubsystem oozieSubsystem = oozieSubsystemService.selectOozieSubsystemById(id);
		mmap.put("oozieSubsystem", oozieSubsystem);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存子系统
	 */
	@RequiresPermissions("oozie:oozieSubsystem:edit")
	@Log(title = "子系统", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(OozieSubsystem oozieSubsystem) {
		return toAjax(oozieSubsystemService.updateOozieSubsystem(oozieSubsystem));
	}
	
	/**
	 * 删除子系统
	 */
	@RequiresPermissions("oozie:oozieSubsystem:remove")
	@Log(title = "子系统", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(oozieSubsystemService.deleteOozieSubsystemByIds(ids));
	}
	
}
