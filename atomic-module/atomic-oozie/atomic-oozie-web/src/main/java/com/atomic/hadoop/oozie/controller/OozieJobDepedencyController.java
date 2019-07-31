package com.atomic.hadoop.oozie.controller;

import com.atomic.common.annotation.Log;
import com.atomic.common.core.controller.BaseController;
import com.atomic.common.core.domain.AjaxResult;
import com.atomic.common.core.domain.R;
import com.atomic.common.core.page.TableDataInfo;
import com.atomic.common.enums.BusinessType;
import com.atomic.common.utils.poi.ExcelUtil;
import com.atomic.framework.shiro.service.IdGen;
import com.atomic.hadoop.oozie.domain.OozieJob;
import com.atomic.hadoop.oozie.domain.OozieJobDepedency;
import com.atomic.hadoop.oozie.service.IOozieJobDepedencyService;
import com.atomic.hadoop.oozie.service.IOozieJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 作业依赖关系 信息操作处理
 * 
 * @author atomic
 * @date 2019-05-23
 */
@Controller
@RequestMapping("/oozie/oozieJobDepedency")
public class OozieJobDepedencyController extends BaseController {
    private String prefix = "oozie/oozieJobDepedency";
	
	@Autowired
	private IOozieJobDepedencyService oozieJobDepedencyService;
	@Autowired
	private IOozieJobService oozieJobService;
	
	@GetMapping()
	public String oozieJobDepedency()
	{
	    return prefix + "/oozieJobDepedency";
	}
	
	/**
	 * 查询作业依赖关系列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(OozieJobDepedency oozieJobDepedency) {
		startPage();
        List<OozieJobDepedency> list = oozieJobDepedencyService.selectOozieJobDepedencyList(oozieJobDepedency);
		return getDataTable(list);
	}

	@PostMapping("/up")
	@ResponseBody
	public TableDataInfo up(OozieJobDepedency oozieJobDepedency) {
		startPage();
		List<OozieJobDepedency> list = oozieJobDepedencyService.selectOozieJobDepedencyList(oozieJobDepedency);
		return getDataTable(list);
	}

	@PostMapping("/down")
	@ResponseBody
	public TableDataInfo down(OozieJobDepedency oozieJobDepedency) {
		startPage();
		List<OozieJobDepedency> list = oozieJobDepedencyService.selectOozieJobDepedencyList(oozieJobDepedency);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出作业依赖关系列表
	 */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OozieJobDepedency oozieJobDepedency) {
    	List<OozieJobDepedency> list = oozieJobDepedencyService.selectOozieJobDepedencyList(oozieJobDepedency);
        ExcelUtil<OozieJobDepedency> util = new ExcelUtil(OozieJobDepedency.class);
        return util.exportExcel(list, "oozieJobDepedency");
    }
	
	/**
	 * 新增作业依赖关系
	 */
	@GetMapping("/add")
	public String add() {
	    return prefix + "/add";
	}
	
	@RequestMapping("/addDepedency/{jobId}")
	public String addDepedency(@PathVariable("jobId") String jobId, ModelMap mmap){
		OozieJob oozieJob = oozieJobService.selectOozieJobById(jobId);
		mmap.put("oozieJob",oozieJob);
		return prefix + "/add";
	}

	@RequestMapping("/queryDepedencyJobList")
	@ResponseBody
	public R queryDepedencyJobList(OozieJob oozieJob){
		List<OozieJob> oozieJobs = oozieJobService.queryDepedencyJobList(oozieJob);
		return R.ok().put("data",oozieJobs);
	}
	
	/**
	 * 新增保存作业依赖关系
	 */
	@Log(title = "作业依赖关系", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(OozieJobDepedency oozieJobDepedency) {
		OozieJob oozieJob = new OozieJob();
		oozieJob.setJobId(oozieJobDepedency.getJobId());
		String depedendcyIdStr=oozieJobDepedency.getDepedencyId();
		String[] depedendcyIds = depedendcyIdStr.split(",");
		for (String depedencyId : depedendcyIds){
			OozieJob job = oozieJobService.selectOozieJobById(depedencyId);
			OozieJobDepedency depedencyJob = new OozieJobDepedency();
			depedencyJob.setJobId(oozieJobDepedency.getJobId());
			depedencyJob.setSubsystemId(job.getSubsystemId());
			depedencyJob.setCycle(job.getCycle());
			depedencyJob.setDepedencyId(job.getJobId());
			depedencyJob.setDepedencyNameEn(job.getNameEn());
			depedencyJob.setDepedencyNameCn(job.getNameCn());
			depedencyJob.setId(IdGen.getCounter(OozieJobDepedency.class.getSimpleName()));
			depedencyJob.setEnable("on");
			depedencyJob.setYarnQueue(job.getHadoopTenant().getYarnQueue());
			oozieJobDepedencyService.insertOozieJobDepedency(depedencyJob);
		}

		return toAjax(1);
	}

	@RequestMapping("/changeEnable")
	@ResponseBody
	public AjaxResult changeEnable(OozieJobDepedency oozieJobDepedency){
		return toAjax(oozieJobDepedencyService.updateOozieJobDepedency(oozieJobDepedency));
	}

	/**
	 * 修改作业依赖关系
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		OozieJobDepedency oozieJobDepedency = oozieJobDepedencyService.selectOozieJobDepedencyById(id);
		mmap.put("oozieJobDepedency", oozieJobDepedency);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存作业依赖关系
	 */
	@Log(title = "作业依赖关系", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(OozieJobDepedency oozieJobDepedency) {
		return toAjax(oozieJobDepedencyService.updateOozieJobDepedency(oozieJobDepedency));
	}
	
	/**
	 * 删除作业依赖关系
	 */
	@Log(title = "作业依赖关系", businessType = BusinessType.DELETE)
	@PostMapping("/removeDependencyJobs")
	@ResponseBody
	public R removeDependencyJobs(String dependecyJobs){
		oozieJobDepedencyService.deleteOozieJobDepedencyByIds(dependecyJobs.split(","));
		return R.ok().put("data","success");
	}


}
