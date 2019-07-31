package com.atomic.hadoop.oozie.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atomic.common.core.text.Convert;
import com.atomic.framework.shiro.service.IdGen;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.common.config.OozieClientConfig;
import com.atomic.hadoop.common.hdfs.HadoopConstants;
import com.atomic.hadoop.common.hdfs.HdfsUtils;
import com.atomic.hadoop.common.oozie.model.CoordinatorVo;
import com.atomic.hadoop.common.oozie.model.EventVo;
import com.atomic.hadoop.common.oozie.model.WorkflowActionVo;
import com.atomic.hadoop.common.oozie.model.WorkflowJobVo;
import com.atomic.hadoop.common.oozie.model.coordinator.*;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowApp;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwWorkflowApp;
import com.atomic.hadoop.common.oozie.utils.OozieClientUtils;
import com.atomic.hadoop.oozie.config.OozieConstants;
import com.atomic.hadoop.oozie.config.OozieJobParamsConstants;
import com.atomic.hadoop.oozie.domain.OozieHiveSql;
import com.atomic.hadoop.oozie.domain.OozieJob;
import com.atomic.hadoop.oozie.domain.OozieJobDepedency;
import com.atomic.hadoop.oozie.domain.OoziePackageTemplate;
import com.atomic.hadoop.oozie.mapper.OozieJobMapper;
import com.atomic.hadoop.oozie.service.IOozieHiveSqlService;
import com.atomic.hadoop.oozie.service.IOozieJobService;
import com.atomic.hadoop.oozie.service.IOoziePackageTemplateService;
import com.atomic.hadoop.tenant.config.annotation.ProjectScope;
import com.atomic.hadoop.tenant.domain.HadoopDefaultTenant;
import com.atomic.hadoop.tenant.domain.HadoopProject;
import com.atomic.hadoop.tenant.mapper.HadoopProjectMapper;
import com.atomic.hadoop.tenant.service.IHadoopDefaultTenantService;
import com.atomic.hadoop.tenant.service.IHadoopProjectService;
import com.github.pagehelper.util.StringUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.oozie.client.WorkflowAction;
import org.apache.oozie.client.WorkflowJob;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * 作业 服务层实现
 * 
 * @author atomic
 * @date 2019-05-20
 */
@Service
public class OozieJobServiceImpl implements IOozieJobService {
	@Autowired
	private OozieJobMapper oozieJobMapper;

	@Autowired
	private IHadoopProjectService hadoopProjectService;

	@Autowired
	private OozieJobDepedencyServiceImpl oozieJobDepedencyService;

	@Autowired
	private OozieClientConfig oozieClientConfig;

	@Autowired
	private IHadoopDefaultTenantService hadoopDefaultTenantService;

	@Autowired
	private IOoziePackageTemplateService ooziePackageTemplateService;

	@Autowired
	private IOozieHiveSqlService oozieHiveSqlService;

	@Autowired
	private OozieClientUtils oozieClientUtils;

	/**
     * 查询作业信息
     * 
     * @param jobId 作业ID
     * @return 作业信息
     */
    @Override
	public OozieJob selectOozieJobById(String jobId) {
	    return oozieJobMapper.selectOozieJobById(jobId);
	}
	
	/**
     * 查询作业列表
     * 
     * @param oozieJob 作业信息
     * @return 作业集合
     */
	@ProjectScope(tableAlias = "t2")
	@Override
	public List<OozieJob> selectOozieJobList(OozieJob oozieJob) {
	    return oozieJobMapper.selectOozieJobList(oozieJob);
	}

	@Override
	public List<OozieJob> queryDepedencyJobList(OozieJob oozieJob) {
		return oozieJobDepedencyService.getDepedencyList(oozieJob);
	}

	/**
     * 新增作业
     * 
     * @param oozieJob 作业信息
     * @return 结果
     */
	@Override
	public int insertOozieJob(OozieJob oozieJob) {
		String oozieJobId= IdGen.getCounter(OozieJob.class.getSimpleName());
		oozieJob.setJobId(oozieJobId);
		oozieJob.setOnlineTime(new Date());
		oozieJob.setOnlineUser(ShiroUtils.getLoginName());
		oozieJob.setEnable("0");
		if (HadoopConstants.CYCLE_DAY.equals(oozieJob.getCycle())){
			oozieJob.setTimeWindow(HadoopConstants.CRON_DAY);
		}else if (HadoopConstants.CYCLE_MONTH.equals(oozieJob.getCycle())){
			oozieJob.setTimeWindow(HadoopConstants.CRON_MONTH);
		}else {
			oozieJob.setTimeWindow(HadoopConstants.CRON_HOUR);
		}
		Properties p= OozieJobParamsConstants.getDefaultsParams();
		oozieJob.setParameter(JSON.toJSONString(p));
		HadoopProject hadoopProject = hadoopProjectService.selectHadoopProjectById(oozieJob.getProjectId());
		oozieJob.setPlatformId(hadoopProject.getPlatformId());
		return oozieJobMapper.insertOozieJob(oozieJob);
	}
	
	/**
     * 修改作业
     * 
     * @param oozieJob 作业信息
     * @return 结果
     */
	@Override
	public int updateOozieJob(OozieJob oozieJob) {
		oozieJob.setUpdateBy(ShiroUtils.getLoginName());
		oozieJob.setUpdateTime(new Date());
		HadoopProject hadoopProject = hadoopProjectService.selectHadoopProjectById(oozieJob.getProjectId());
		oozieJob.setPlatformId(hadoopProject.getPlatformId());
	    return oozieJobMapper.updateOozieJob(oozieJob);
	}

	/**
     * 删除作业对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOozieJobByIds(String ids) {
		return oozieJobMapper.deleteOozieJobByIds(Convert.toStrArray(ids));
	}

	@Override
	public String getCoordinatorXml(OozieJob oozieJob) {
		OozieJob oozieJobNew=oozieJobMapper.selectOozieJobById(oozieJob.getJobId());
		CoordinatorVo coordinatorVo = new CoordinatorVo();
		coordinatorVo.setId(oozieJob.getJobId());
		coordinatorVo.setNameEn(oozieJob.getNameEn());
		coordinatorVo.setNameCn(oozieJob.getNameCn());
		coordinatorVo.setQueue(oozieJobNew.getHadoopTenant().getYarnQueue());
		coordinatorVo.setXmlns(oozieJob.getNote());
		coordinatorVo.setFrequency(oozieJob.getTimeWindow());
		coordinatorVo.setCycle(oozieJob.getCycle());
		coordinatorVo.setStartTime("${starttime}");
		coordinatorVo.setEndTime("${endtime}");
		coordinatorVo.setControlTimeout("-1");
		coordinatorVo.setSubSystemNameEn(oozieJobNew.getSubSystemNameEn());
		coordinatorVo.setParams(oozieJob.getParameter());
//		coordinatorVo.setControlConcurrency("1");
//		coordinatorVo.setControlExecution(controlExecution);
		coordinatorVo.setControlThrottle("-1");
		String appPath = oozieJobNew.getHadoopPlatform().getDefaultFs()+"/user/atomic/oozie-app/"+oozieJobNew.getHadoopTenant().getTenant()+"/workflow/"+oozieJobNew.getOoziePackageTemplate().getNameEn();
		coordinatorVo.setAppPath(appPath);
		return coordinatorVoToXml(coordinatorVo);
	}

	@Transactional
	@Override
	public void upLoadToHdfs(Configuration config,OozieJob oozieJob) throws IOException, InterruptedException {
		String coordinatorXml = oozieJob.getCoordinatorXml();
		HdfsUtils hdfsUtils = new HdfsUtils(config);
		HadoopDefaultTenant defaultTenant = hadoopDefaultTenantService.selectHadoopDefaultTenantByPlatformId(oozieJob.getPlatformId());
		String defaultFs = defaultTenant.getHadoopPlatform().getDefaultFs();
		String workflowHdfsParentPath = defaultFs + oozieClientConfig.getOozieAppRootHdfs()+defaultTenant.getTenant()+ HadoopConstants.FILE_SEPARATOR + OozieConstants.WORKFLOW + HadoopConstants.FILE_SEPARATOR + oozieJob.getNameEn() + HadoopConstants.FILE_SEPARATOR;
		String coordinatorHdfsParentPath = defaultFs + oozieClientConfig.getOozieAppRootHdfs()+defaultTenant.getTenant()+ HadoopConstants.FILE_SEPARATOR + OozieConstants.COORDINATOR + HadoopConstants.FILE_SEPARATOR + oozieJob.getNameEn() + HadoopConstants.FILE_SEPARATOR;
		String workflowXmlFileName = OozieConstants.WORKFLOW_XML;
		String coordinatorXmlFileName = OozieConstants.COORDINATOR_XML;

		OozieJob oozieJob1 = selectOozieJobById(oozieJob.getJobId());
		String workflowXml = oozieJob1.getOoziePackageTemplate().getWorkflowXml();
		//上传workflow.xml
		hdfsUtils.writeContentToHdfs(defaultTenant.getTenant(),workflowHdfsParentPath,workflowXmlFileName,workflowXml);
		//上传coordinator.xml
		hdfsUtils.writeContentToHdfs(defaultTenant.getTenant(),coordinatorHdfsParentPath,coordinatorXmlFileName,coordinatorXml);

		//上传DAG相关脚本信息
		OozieHiveSql oozieHiveSql = new OozieHiveSql();
		oozieHiveSql.setTaskId(oozieJob.getPackageId());
		List<OozieHiveSql> oozieHiveSqls = oozieHiveSqlService.selectOozieHiveSqlList(oozieHiveSql);
		for (OozieHiveSql x : oozieHiveSqls){
			String fullPath = defaultFs + x.getHsqlPath();
			String fileName = fullPath.substring(fullPath.lastIndexOf("/")+1);
			fullPath = fullPath.substring(0,fullPath.lastIndexOf("/"));
			hdfsUtils.writeContentToHdfs(defaultTenant.getTenant(),fullPath,fileName,x.getHsql());
		}
		updateOozieJob(oozieJob);
	}

	@Override
	public String getFlowJsonFromXml(String flowXml) {
		OwWorkflowApp wfApp = new OwWorkflowApp();
		wfApp.parseXml(flowXml);
		FlowApp app = new FlowApp(wfApp);
		return app.toFlowJson();
	}

	public String coordinatorVoToXml(CoordinatorVo coordinatorVo){
		OwCoordinatorApp wkApp = new OwCoordinatorApp();
		wkApp.setName(coordinatorVo.getNameEn());
		wkApp.setFrequency(coordinatorVo.getFrequency());
		wkApp.setStart(coordinatorVo.getStartTime());
		wkApp.setEnd(coordinatorVo.getEndTime());
		wkApp.setTimezone("GMT+08:00");
		OwCoordinatorControls controls = new OwCoordinatorControls();
		if (!StringUtil.isEmpty(coordinatorVo.getControlConcurrency())) {
			controls.setConcurrency(coordinatorVo.getControlConcurrency());
		}
		if (!StringUtil.isEmpty(coordinatorVo.getControlExecution())) {
			controls.setExecution(coordinatorVo.getControlExecution());
		}
		if (!StringUtil.isEmpty(coordinatorVo.getControlThrottle())) {
			controls.setThrottle(coordinatorVo.getControlThrottle());
		}
		if (!StringUtil.isEmpty(coordinatorVo.getControlTimeout())) {
			controls.setTimeout(coordinatorVo.getControlTimeout());
		}
		if (!controls.equals(null)) {
			wkApp.setControls(controls);
		}
		EventVo eventVo = new EventVo();
		eventVo.setCoordinatorId(coordinatorVo.getId());

		OozieJobDepedency oozieJobDepedency = new OozieJobDepedency();
		oozieJobDepedency.setJobId(coordinatorVo.getId());
		oozieJobDepedency.setEnable("on");
		List<OozieJobDepedency> oozieJobDepedencies = oozieJobDepedencyService.selectOozieJobDepedencyList(oozieJobDepedency);
		OwCoordinatorOutputEvents outEvents = new OwCoordinatorOutputEvents();
		OwCoordinatorInputEvents inEvents = null;
		OwCoordinatorDatasets datasets = new OwCoordinatorDatasets();
		if (oozieJobDepedencies !=null && oozieJobDepedencies.size()>0){
			inEvents = new OwCoordinatorInputEvents();
			List<OwCoordinatorDataIn> data_ins = new ArrayList<>();
			inEvents.setData_in(data_ins);
			for (OozieJobDepedency depedency : oozieJobDepedencies){
				OwCoordinatorDataIn data_in = new OwCoordinatorDataIn();
				data_in.setName(depedency.getDepedencyNameEn()+"_IN");
				data_in.setDataset(depedency.getDepedencyNameEn()+"_TRIGGER");
				data_in.setInstance("${coord:current(0)}");
				data_ins.add(data_in);

				OwCoordinatorDataset owDataset = new OwCoordinatorDataset();
				owDataset.setName(depedency.getDepedencyNameEn()+"_TRIGGER");
				owDataset.setDone_flag("");
				if ("D".equals(coordinatorVo.getCycle())){
					owDataset.setFrequency("${coord:days(1)}");
					owDataset.setUri_template("${nameNode}/user/oozie/flag/"+coordinatorVo.getSubSystemNameEn() + "/" +coordinatorVo.getNameEn()+"/D${YEAR}${MONTH}${DAY}");
				}else if ("M".equals(coordinatorVo.getCycle())){
					owDataset.setFrequency("${coord:months(1)}");
					owDataset.setUri_template("${nameNode}/user/oozie/flag/"+coordinatorVo.getSubSystemNameEn() + "/" +coordinatorVo.getNameEn()+"/M${YEAR}${MONTH}01");
				}else {
					owDataset.setUri_template("${nameNode}/user/oozie/flag/"+coordinatorVo.getSubSystemNameEn() + "/" +coordinatorVo.getNameEn()+"/H${YEAR}${MONTH}${DAY}${HOUR}");
					owDataset.setFrequency("${coord:hours(1)}");
				}

				owDataset.setInitial_instance("2016-07-01T00:00+0800");
				owDataset.setTimezone("GMT+08:00");
				owDataset.setUri_template("${nameNode}/user/oozie/flag/"+depedency.getSubsystemNameEn() + "/" +depedency.getDepedencyNameEn()+"${YEAR}${MONTH}${DAY}");
				datasets.getDatasets().add(owDataset);
			}
		}
		OwCoordinatorDataOut data_out = new OwCoordinatorDataOut();
		data_out.setName(coordinatorVo.getNameEn()+"_OUT");
		data_out.setDataset(coordinatorVo.getNameEn()+"_TRIGGER");
		data_out.setInstance("${coord:current(0)}");
		List<OwCoordinatorDataOut> data_outs = new ArrayList();
		data_outs.add(data_out);
		outEvents.setData_out(data_outs);
		OwCoordinatorDataset outDataset = new OwCoordinatorDataset();
		outDataset.setDone_flag("");
		outDataset.setName(coordinatorVo.getNameEn()+"_TRIGGER");
		if ("D".equals(coordinatorVo.getCycle())){
			outDataset.setFrequency("${coord:days(1)}");
			outDataset.setUri_template("${nameNode}/user/oozie/flag/"+coordinatorVo.getSubSystemNameEn() + "/" +coordinatorVo.getNameEn()+"/D${YEAR}${MONTH}${DAY}");
		}else if ("M".equals(coordinatorVo.getCycle())){
			outDataset.setFrequency("${coord:months(1)}");
			outDataset.setUri_template("${nameNode}/user/oozie/flag/"+coordinatorVo.getSubSystemNameEn() + "/" +coordinatorVo.getNameEn()+"/M${YEAR}${MONTH}01");
		} else{
			outDataset.setFrequency("${coord:hours(1)}");
			outDataset.setUri_template("${nameNode}/user/oozie/flag/"+coordinatorVo.getSubSystemNameEn() + "/" +coordinatorVo.getNameEn()+"/H${YEAR}${MONTH}${DAY}${HOUR}");
		}

		outDataset.setInitial_instance("2016-07-01T00:00+0800");
		outDataset.setTimezone("GMT+08:00");
//		outDataset.setUri_template("${nameNode}/user/oozie/flag/"+coordinatorVo.getSubSystemNameEn() + "/" +coordinatorVo.getNameEn()+"${YEAR}${MONTH}${DAY}");
		datasets.getDatasets().add(outDataset);


		if (inEvents != null && !inEvents.getData_in().isEmpty()) {
			wkApp.setInput_events(inEvents);
		}
		if (outEvents != null && !outEvents.getData_out().isEmpty()) {
			wkApp.setOutput_events(outEvents);
		}
		wkApp.setDatasets(datasets);

		OwCoordinatorConfiguration configuration = new OwCoordinatorConfiguration();
		String params = coordinatorVo.getParams();
		if (StrUtil.isNotEmpty(params)){
			Map<String,Object> map = (Map<String, Object>) JSON.parse(params);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				OwCoordinatorProperty property = new OwCoordinatorProperty();
				property.setName(entry.getKey());
				property.setValue((String) entry.getValue());
				configuration.getPropertys().add(property);
			}
		}
		OwCoordinatorProperty jobStracker = new OwCoordinatorProperty();
		jobStracker.setName("jobTracker");
		jobStracker.setValue("${jobTracker}");
		configuration.getPropertys().add(jobStracker);
		OwCoordinatorProperty nameNode = new OwCoordinatorProperty();
		nameNode.setName("nameNode");
		nameNode.setValue("${nameNode}");
		configuration.getPropertys().add(nameNode);

		OwCoordinatorFlow flow = new OwCoordinatorFlow();
		flow.setConfiguration(configuration);
		flow.setApp_path(coordinatorVo.getAppPath());
		OwCoordinatorAction action = new OwCoordinatorAction();
		action.setWorkflow(flow);
		wkApp.getActions().add(action);
		return wkApp.toXml();
	}

	@Override
	public JSONObject getJobRunFlowJson(String workflowId,OozieJob oozieJob) {
		String oozieUrl = oozieJob.getHadoopPlatform().getOozieUrl();
		String workflowXml = oozieClientUtils.getOozieJobDefinition(workflowId,oozieUrl);
		String flowJsonFromXml = getFlowJsonFromXml(workflowXml);
		WorkflowJob workflowJob = oozieClientUtils.getWorkflowJob(workflowId, oozieUrl);
		WorkflowJobVo jobVo = new WorkflowJobVo(workflowJob);
		String flowJson = flowJsonFromXml.replaceAll("  ", " ").replaceAll("'", "&#39;");
		JSONObject jobJson = JSON.parseObject(flowJson);
		JSONArray jarr = jobJson.getJSONArray("node");
		for (WorkflowActionVo actionVo : jobVo.getActions()) {
			for (int i = 0; i < jarr.size(); i++) {
				JSONObject jo = jarr.getJSONObject(i);
				if (actionVo.getName().equals(jo.getString("text"))) {
					jo.put("transition", actionVo.getTransition());
					break;
				} else {
					if (":START:".equalsIgnoreCase(actionVo.getType()) && "start".equals(jo.getString("text"))) {
						jo.put("transition", actionVo.getTransition());
						break;
					}
				}
			}

			if ("fail".equals(actionVo.getTransition())) {
				for (int i = 0; i < jarr.size(); i++) {
					JSONObject jo = jarr.getJSONObject(i);
					if (actionVo.getName().equals(jo.getString("text"))) {
						JSONObject lObj = new JSONObject();
						lObj.put("node1id", jo.getInteger("nodeid"));
						lObj.put("node2id", 9999);
						JSONArray linkarray = jobJson.getJSONArray("linkarray");
						linkarray.add(lObj);
						break;
					}
				}
			}
			if ("fail".equals(actionVo.getName())) {
				JSONObject failObj = new JSONObject();
				failObj.put("nodeid", 9999);
				failObj.put("x", 200);
				failObj.put("y", 400);
				failObj.put("type", "fail");
				failObj.put("text", "fail");

				JSONObject attribute = new JSONObject();

				attribute.put("actionId", actionVo.getId());
				attribute.put("status", "KILLED");

				failObj.put("attribute", attribute);

				jarr.add(failObj);
				continue;
			}

			for (int i = 0; i < jarr.size(); i++) {
				JSONObject jo = jarr.getJSONObject(i);
				String type = jo.getString("type");
				if (":START:".equalsIgnoreCase(actionVo.getType()) && "start".equals(type)) {
					JSONObject Aarray = new JSONObject();
					Aarray.put("status", actionVo.getStatus());
					Aarray.put("actionId", actionVo.getId());
					jo.put("attribute", Aarray);
					break;
				} else if (actionVo.getName().equals(jo.getString("text"))) {
					JSONObject Aarray;
					if ("end".equals(type)) {
						Aarray = new JSONObject();
						Aarray.put("status", actionVo.getStatus());
						Aarray.put("actionId", actionVo.getId());
						jo.put("attribute", Aarray);
					} else {

						try {
							Aarray = new JSONObject();
							Aarray.put("status", actionVo.getStatus());
							Aarray.put("actionId", actionVo.getId());
							jo.put("attribute", Aarray);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					break;
				}
			}
		}

		for (int i = 0; i < jarr.size(); i++) {
			JSONObject jo = jarr.getJSONObject(i);
			JSONObject Aarray;
			try {
				Aarray = jo.getJSONObject("attribute");
				if (Aarray == null) {
					Aarray = new JSONObject();
					Aarray.put("status", "NOTRUNNING");
					jo.put("attribute", Aarray);
				} else {
					String statusStr = Aarray.getString("status");
					if (statusStr == null) {
						Aarray.put("status", "NOTRUNNING");
					}
				}
			} catch (Exception e) {
				Aarray = new JSONObject();
				Aarray.put("status", "NOTRUNNING");
				jo.put("attribute", Aarray);
			}

		}

		return jobJson;
	}

	@Override
	public WorkflowAction getWorkflowActionByActionId(String actionId, String oozieServerUrl) {
		return oozieClientUtils.getWorkflowActionByActionId(actionId,oozieServerUrl);
	}

	public static void main(String[] args) {
		String json = "{\"aa\":\"${coord:formatTime(coord:dateOffset(coord:nominalTime(), -400, 'DAY'), 'yyyyMMdd')}\",\"bb\":2}";
		Map<String,Object> map = (Map<String, Object>) JSON.parse(json);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}


}
