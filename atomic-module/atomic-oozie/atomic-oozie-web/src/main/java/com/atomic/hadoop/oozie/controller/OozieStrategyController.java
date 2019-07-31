package com.atomic.hadoop.oozie.controller;

import com.atomic.common.annotation.Log;
import com.atomic.common.core.controller.BaseController;
import com.atomic.common.core.domain.AjaxResult;
import com.atomic.common.core.page.TableDataInfo;
import com.atomic.common.enums.BusinessType;
import com.atomic.common.utils.poi.ExcelUtil;
import com.atomic.hadoop.oozie.domain.OozieStrategy;
import com.atomic.hadoop.oozie.service.IOozieStrategyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 策略通道 信息操作处理
 * 
 * @author atomic
 * @date 2019-06-06
 */
@Controller
@RequestMapping("/oozie/oozieStrategy")
public class OozieStrategyController extends BaseController {
    private String prefix = "oozie/oozieStrategy";
	
	@Autowired
	private IOozieStrategyService oozieStrategyService;
	
	@RequiresPermissions("oozie:oozieStrategy:view")
	@GetMapping()
	public String oozieStrategy()
	{
	    return prefix + "/oozieStrategy";
	}
	
	/**
	 * 查询策略通道列表
	 */
	@RequiresPermissions("oozie:oozieStrategy:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(OozieStrategy oozieStrategy) {
		startPage();
        List<OozieStrategy> list = oozieStrategyService.selectOozieStrategyList(oozieStrategy);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出策略通道列表
	 */
	@RequiresPermissions("oozie:oozieStrategy:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OozieStrategy oozieStrategy) {
    	List<OozieStrategy> list = oozieStrategyService.selectOozieStrategyList(oozieStrategy);
        ExcelUtil<OozieStrategy> util = new ExcelUtil<OozieStrategy>(OozieStrategy.class);
        return util.exportExcel(list, "oozieStrategy");
    }
	
	/**
	 * 新增策略通道
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存策略通道
	 */
	@RequiresPermissions("oozie:oozieStrategy:add")
	@Log(title = "策略通道", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(OozieStrategy oozieStrategy) {
		return toAjax(oozieStrategyService.insertOozieStrategy(oozieStrategy));
	}

	/**
	 * 修改策略通道
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
		OozieStrategy oozieStrategy = oozieStrategyService.selectOozieStrategyById(id);
		mmap.put("oozieStrategy", oozieStrategy);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存策略通道
	 */
	@RequiresPermissions("oozie:oozieStrategy:edit")
	@Log(title = "策略通道", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(OozieStrategy oozieStrategy) throws Exception {
		return toAjax(oozieStrategyService.updateOozieStrategy(oozieStrategy));
	}
	
	/**
	 * 删除策略通道
	 */
	@RequiresPermissions("oozie:oozieStrategy:remove")
	@Log(title = "策略通道", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids) throws Exception {
		return toAjax(oozieStrategyService.deleteOozieStrategyByIds(ids));
	}

	/**
	 * 策略通道状态修改
	 */
	@PostMapping("/changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(OozieStrategy job) throws Exception {
		OozieStrategy newJob = oozieStrategyService.selectOozieStrategyById(job.getId());
		newJob.setStatus(job.getStatus());
		return toAjax(oozieStrategyService.updateOozieStrategy(newJob));
	}
	
}
