package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwConfiguration;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwProperty;
import com.atomic.hadoop.common.oozie.service.IWorkflowService;
import com.atomic.hadoop.common.oozie.vo.TaskVo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("_owsubflow")
@XStreamAlias("sub-workflow")
public class OwSubflow implements OwActionNode {
	private static String WORKFLOW_NAME_PATTERN_STR = ".*/workflow/([a-z_A-Z]{1,39}).?";
	private static Pattern flowNamePattern = Pattern.compile(WORKFLOW_NAME_PATTERN_STR);
	private static IWorkflowService workflowService;
//	private static ITaskService taskService;

	@XStreamAlias("app-path")
	private String app_path;
	@XStreamAlias("propagate-configuration")
	private OwPropagate propagate;
	private OwConfiguration configuration;

	public OwSubflow() {
		super();
	}

	public OwSubflow(FlowNode flowNode) {
		if (flowNode.getAttribute() == null) {
			try {
				throw new Exception(flowNode.getText() + "未配置");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String att = flowNode.getAttribute().toString();
		System.out.println(att);
		JSONObject attJson = JSON.parseObject(att);

		String childprocessid = attJson.getString("childprocessid");
		app_path = ""; //workflowService.getAppPathByFlowId(childprocessid);

		OwProperty prop = new OwProperty();
		prop.setName("mapred.job.queue.name");
		prop.setValue("${queueName}");
		List<OwProperty> props = new ArrayList<OwProperty>();
		props.add(prop);
		configuration = new OwConfiguration();
		configuration.setPropertys(props);

		String propagate = attJson.getString("propagate");
		if ("Y".equalsIgnoreCase(propagate)) {
			this.propagate = new OwPropagate();
		}
		JSONArray paraNames = attJson.getJSONArray("paramName");
		if (paraNames != null) {
			JSONArray paraValues = attJson.getJSONArray("paramValue");
			for (int i = 0; i < paraNames.size(); i++) {
				String paraName = paraNames.getString(i);
				String paraValue = paraValues.getString(i);

				OwProperty propPara = new OwProperty();
				propPara.setName(paraName);
				propPara.setValue(paraValue);
				configuration.getPropertys().add(propPara);
			}
		} else {
			String paraName = attJson.getString("paramName");
			String paraValue = attJson.getString("paramValue");

			OwProperty propPara = new OwProperty();
			propPara.setName(paraName);
			propPara.setValue(paraValue);
			configuration.getPropertys().add(propPara);
		}

	}

	public static OwSubflow parseXml(Element ele) {
		OwSubflow owEm = new OwSubflow();
		owEm.app_path = ele.elementText("app-path");
		if (ele.element("propagate-configuration") != null) {
			owEm.propagate = new OwPropagate();
		}

		if (ele.element("configuration") != null) {
			owEm.configuration = OwConfiguration.parseXml(ele.element("configuration"));
		}
		return owEm;
	}

	public Object toAttributes() {
		JSONObject attObj = new JSONObject();
		if (app_path != null) {
			Matcher matcher = flowNamePattern.matcher(app_path);
			if (matcher.find()) {
				String flowNameEn = matcher.group(1);
				TaskVo taskVo = new TaskVo(); //taskService.getByNameEn(flowNameEn);

				attObj.put("childprocessid", taskVo.getId());
			}
		}
		if (propagate != null) {
			attObj.put("propagate", "Y");
		}
		if (configuration != null && configuration.getPropertys() != null && configuration.getPropertys().size() > 1) {

			for (OwProperty prop : configuration.getPropertys()) {
				if (!prop.getName().equals("mapred.job.queue.name")) {
					attObj.put(prop.getName(), prop.getValue());
				}
			}
		}
		return attObj.toJSONString();
	}
}
