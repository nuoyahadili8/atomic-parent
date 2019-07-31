package com.atomic.hadoop.oozie.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.atomic.common.annotation.Log;
import com.atomic.common.core.controller.BaseController;
import com.atomic.common.core.domain.AjaxResult;
import com.atomic.common.core.domain.R;
import com.atomic.common.core.page.TableDataInfo;
import com.atomic.common.enums.BusinessType;
import com.atomic.common.utils.poi.ExcelUtil;
import com.atomic.hadoop.common.config.OozieClientConfig;
import com.atomic.hadoop.oozie.domain.OozieJob;
import com.atomic.hadoop.oozie.domain.OozieJobParam;
import com.atomic.hadoop.oozie.domain.OozieSubsystem;
import com.atomic.hadoop.oozie.service.IOozieJobService;
import com.atomic.hadoop.oozie.service.IOoziePackageTemplateService;
import com.atomic.hadoop.oozie.service.IOozieSubsystemService;
import com.atomic.hadoop.common.hdfs.HadoopConstants;
import com.atomic.hadoop.tenant.domain.HadoopProject;
import com.atomic.hadoop.tenant.service.IHadoopDefaultTenantService;
import com.atomic.hadoop.tenant.service.IHadoopProjectService;
import com.atomic.hadoop.tenant.service.IHadoopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * 作业 信息操作处理
 * 
 * @author atomic
 * @date 2019-05-20
 */
@Controller
@RequestMapping("/oozie/oozieJob")
@Slf4j
public class OozieJobController extends BaseController {
    private String prefix = "oozie/oozieJob";
	
	@Autowired
	private IOozieJobService oozieJobService;

	@Autowired
	private IHadoopProjectService hadoopProjectService;
	@Autowired
	private IOozieSubsystemService oozieSubsystemService;
	@Autowired
	private OozieClientConfig oozieClientConfig;
	@Autowired
	private IHadoopService hadoopService;

	@RequiresPermissions("oozie:oozieJob:view")
	@GetMapping()
	public String oozieJob(ModelMap mmap) {
		List<HadoopProject> hadoopProjects = hadoopProjectService.selectHadoopProjectList(new HadoopProject());
		List<OozieSubsystem> oozieSubsystems = oozieSubsystemService.selectOozieSubsystemList(new OozieSubsystem());
		mmap.put("hadoopProjects",hadoopProjects);
		mmap.put("oozieSubsystems",oozieSubsystems);
		return prefix + "/oozieJob";
	}


	@RequestMapping("wizard")
	public String wizard(ModelMap mmap){
		List<HadoopProject> list = hadoopProjectService.selectHadoopProjectList(new HadoopProject());
		mmap.put("projectList",list);
		return prefix + "/oozieJobWizard";
	}

	@RequestMapping("checkOozieJobUnique")
	@ResponseBody
	public String checkOozieJobUnique(OozieJob oozieJob){
		List<OozieJob> oozieJobs = oozieJobService.selectOozieJobList(oozieJob);
		if (oozieJobs != null && oozieJobs.size() > 0){
			return "1";
		}else {
			return "0";
		}
	}

	@RequestMapping("autocompleteOozieJobNameEn")
	@ResponseBody
	public List<Map<String, Object>> autocompleteOozieJobNameEn(HttpServletRequest request){
		String query = request.getParameter("term");
		OozieJob oozieJob = new OozieJob();
		oozieJob.setNameEn(query);
		List<OozieJob> oozieJobs = oozieJobService.selectOozieJobList(oozieJob);
		List<Map<String, Object>> suggestions = new ArrayList<>();
		for (OozieJob job:oozieJobs){
			Map<String, Object> map = new HashMap(2);
			map.put("value",job.getNameEn());
			map.put("label",job.getNameEn());
			suggestions.add(map);
		}
		return suggestions;
	}
	
	/**
	 * 查询作业列表
	 */
	@RequiresPermissions("oozie:oozieJob:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(OozieJob oozieJob) {
		startPage();
        List<OozieJob> list = oozieJobService.selectOozieJobList(oozieJob);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出作业列表
	 */
	@RequiresPermissions("oozie:oozieJob:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OozieJob oozieJob) {
    	List<OozieJob> list = oozieJobService.selectOozieJobList(oozieJob);
        ExcelUtil<OozieJob> util = new ExcelUtil(OozieJob.class);
        return util.exportExcel(list, "oozieJob");
    }
	
	/**
	 * 新增作业
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存作业
	 */
	@RequiresPermissions("oozie:oozieJob:add")
	@Log(title = "作业", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(OozieJob oozieJob) {
		return toAjax(oozieJobService.insertOozieJob(oozieJob));
	}

	/**
	 * 修改作业
	 */
	@GetMapping("/edit/{jobId}")
	public String edit(@PathVariable("jobId") String jobId, ModelMap mmap) {
		OozieJob oozieJob = oozieJobService.selectOozieJobById(jobId);
		mmap.put("oozieJob", oozieJob);
		if (StrUtil.isNotEmpty(oozieJob.getParameter())){
			Map<String,Object> map = (Map<String, Object>) JSON.parse(oozieJob.getParameter());
			List<OozieJobParam> params = new ArrayList<>();
			for (Map.Entry<String,Object> entry : map.entrySet()) {
				OozieJobParam param = new OozieJobParam();
				param.setKey(entry.getKey());
				param.setValue(entry.getValue());
				params.add(param);
			}
			mmap.put("paramList", params);
		}
	    return prefix + "/edit";
	}

	@RequestMapping("/coordinatorXml/{jobId}")
	@ResponseBody
	public R coordinatorXml(@PathVariable("jobId") String jobId){
		OozieJob oozieJob = oozieJobService.selectOozieJobById(jobId);
		return R.ok().put("data",oozieJob.getCoordinatorXml());
	}

	/**
	 * 修改保存作业
	 */
	@RequiresPermissions("oozie:oozieJob:edit")
	@Log(title = "作业", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public R editSave(OozieJob oozieJob,HttpServletRequest request) throws IOException, InterruptedException {
		String coordinatorXml = oozieJobService.getCoordinatorXml(oozieJob);
		oozieJob.setCoordinatorXml(coordinatorXml);
		if (HadoopConstants.JOB_ENABLE.equals(oozieJob.getEnable())){
			oozieJob.setEnable("1");
		}else {
			oozieJob.setEnable("0");
		}

		// 有效任务 且开启hdfs上传oozie workflow.xml coordinator.xml文件
		if (HadoopConstants.JOB_ENABLE_ONE.equals(oozieJob.getEnable()) && oozieClientConfig.isUploadHDFS()){
			Configuration config;
			HttpSession session = request.getSession();
			if (session.getAttribute(HadoopConstants.HADOOP_CONFIGURATION) ==null){
				config = hadoopService.loginHadoop(oozieJob.getPlatformId());
				session.setAttribute(HadoopConstants.HADOOP_CONFIGURATION,config);
			}else {
				config = (Configuration) session.getAttribute(HadoopConstants.HADOOP_CONFIGURATION);
			}
			oozieJobService.upLoadToHdfs(config,oozieJob);
		}
		return R.ok().put("data","success");
	}
	
	/**
	 * 删除作业
	 */
	@RequiresPermissions("oozie:oozieJob:remove")
	@Log(title = "作业", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(oozieJobService.deleteOozieJobByIds(ids));
	}

	@RequestMapping("/toRunWindow/{jobId}")
	public String toRunWindow(@PathVariable("jobId") String jobId, ModelMap mmap){
		OozieJob oozieJob = oozieJobService.selectOozieJobById(jobId);
		mmap.put("oozieJob",oozieJob);
		String now;
		if (HadoopConstants.CYCLE_HOUR.equals(oozieJob.getCycle())){
			now = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
		}else if (HadoopConstants.CYCLE_MONTH.equals(oozieJob.getCycle())){
			now = DateUtil.format(new Date(), "yyyy-MM-dd");
			now = now.substring(0,8) + "01";
		}else {
			now = DateUtil.format(new Date(), "yyyy-MM-dd");
		}
		mmap.put("now",now);
		return prefix + "/runJob";
	}
}
