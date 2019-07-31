package com.atomic.hadoop.oozie.controller;

import com.alibaba.fastjson.JSONObject;
import com.atomic.common.core.controller.BaseController;
import com.atomic.common.core.domain.AjaxResult;
import com.atomic.common.core.domain.R;
import com.atomic.common.core.page.TableDataInfo;
import com.atomic.common.utils.DateUtils;
import com.atomic.hadoop.common.oozie.model.WorkflowActionVo;
import com.atomic.hadoop.common.oozie.model.WorkflowJobVo;
import com.atomic.hadoop.common.oozie.utils.OozieClientUtils;
import com.atomic.hadoop.oozie.domain.OozieJob;
import com.atomic.hadoop.oozie.log.domain.WfJobsVw;
import com.atomic.hadoop.oozie.log.service.IWfJobsVwService;
import com.atomic.hadoop.oozie.service.IOozieJobService;
import com.atomic.hadoop.oozie.service.IOozieTaskToolService;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import lombok.extern.slf4j.Slf4j;
import org.apache.oozie.client.WorkflowAction;
import org.apache.oozie.client.WorkflowJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/7/16/016 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Controller
@RequestMapping("/oozie/watchJob")
@Slf4j
public class OozieJobWatchController extends BaseController {
    private String prefix = "oozie/oozieJobLog";

    @Autowired
    private IOozieTaskToolService oozieTaskToolService;

    @Autowired
    private IOozieJobService oozieJobService;

    @Autowired
    private OozieClientUtils oozieClientUtils;

    @Autowired
    private IWfJobsVwService wfJobsVwService;

    @RequestMapping("/runJob")
    @ResponseBody
    public AjaxResult runJob(HttpServletRequest request){
        String jobId = request.getParameter("jobId");
        String startTime = request.getParameter("startTime");
        System.out.println(jobId + ">>>>>>>>" + startTime);
        return toAjax(oozieTaskToolService.runJob(jobId,startTime,"coordinator"));
    }

    @RequestMapping("/toWatchLogPage/{jobId}")
    public String toWatchLogPage(@PathVariable("jobId") String jobId, ModelMap mmap){
        mmap.put("jobId",jobId);
        return prefix + "/oozieJobLog";
    }

    @RequestMapping("/jobLog/{jobId}")
    @ResponseBody
    public TableDataInfo jobLog(@PathVariable("jobId") String jobId){
        OozieJob oozieJob = oozieJobService.selectOozieJobById(jobId);
        startPage();
        WfJobsVw wfJobsVw = new WfJobsVw();
        wfJobsVw.setAppName(oozieJob.getNameEn());
        wfJobsVw.setUserName(oozieJob.getHadoopTenant().getTenant());
        List<WfJobsVw> wfJobsVws = wfJobsVwService.selectWfJobsVwList(wfJobsVw);
        return getDataTable(wfJobsVws);
    }

    @RequestMapping("runWatch")
    public String runWatch(HttpServletRequest request,ModelMap mmap){
        String jobId = request.getParameter("jobId");
        String workflowId = request.getParameter("workflowId");
        OozieJob oozieJob = oozieJobService.selectOozieJobById(jobId);
        JSONObject jobRunFlowJson = oozieJobService.getJobRunFlowJson(workflowId, oozieJob);
        mmap.put("appName",oozieJob.getNameEn());
        mmap.put("jobId",jobId);
        mmap.put("workflowId",workflowId);
        mmap.put("scale",1);
        mmap.put("flowjson",jobRunFlowJson.toJSONString());
        return prefix + "/runWatch";
    }

    @RequestMapping("getFlowRunJsonByJobId")
    @ResponseBody
    public R getFlowRunJsonByJobId(HttpServletRequest request){
        String jobId = request.getParameter("jobId");
        String workflowId = request.getParameter("workflowId");
        OozieJob oozieJob = oozieJobService.selectOozieJobById(jobId);
        JSONObject jobRunFlowJson = oozieJobService.getJobRunFlowJson(workflowId, oozieJob);
        return R.ok().put("data",jobRunFlowJson.toJSONString());
    }

    @RequestMapping("toInfoAction")
    public String toInfoAction(HttpServletRequest request,ModelMap mmap){
        String jobId = request.getParameter("jobId");
        String workflowId = request.getParameter("workflowId");
        String actionId = request.getParameter("actionId");
        OozieJob oozieJob = oozieJobService.selectOozieJobById(jobId);
        HadoopPlatform hadoopPlatform = oozieJob.getHadoopPlatform();
        WorkflowAction workflowAction = oozieJobService.getWorkflowActionByActionId(actionId, hadoopPlatform.getOozieUrl());
        WorkflowActionVo actionVo = new WorkflowActionVo(workflowAction);
        if (actionVo.getExternalChildIDs() != null && !actionVo.getExternalChildIDs().isEmpty() && actionVo.getConsoleUrl() != null) {
            actionVo.setExternalChildIDs(actionVo.getConsoleUrl().substring(0, actionVo.getConsoleUrl().length() - 19) + actionVo.getExternalChildIDs().substring(4) + "/");
        }
        String startTime = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",actionVo.getStartTime());
        String endTime = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",actionVo.getEndTime());
        mmap.put("actionVo",actionVo);
        mmap.put("startTime",startTime);
        mmap.put("endTime",endTime);
        mmap.put("rsUrl",hadoopPlatform.getRmUrl());
        mmap.put("hsUrl",hadoopPlatform.getHsUrl());
        return prefix + "/actionInfo";
    }
}
