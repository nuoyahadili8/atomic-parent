package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwAbstractActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwDelete;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwMkdir;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("distcp")
public class OwDistcp extends OwAbstractActionNode implements OwActionNode {
	private static String MAPREDUCE_JOB_HDFS_SERVERS = "oozie.launcher.mapreduce.job.hdfs-servers";
	private static String HADOOP_NAMENODE = "hadoop.nameNode";
	private static String nameNode;
	static {
//		CfgUtils cfg = new CfgUtils();
//		nameNode = cfg.getProperty(HADOOP_NAMENODE);
	}

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:distcp-action:0.2";
	@XStreamAlias("java-opts")
	private String java_opts;
	@XStreamImplicit(itemFieldName = "arg")
	private List<String> args = new ArrayList<String>();

	public OwDistcp() {

	}

	public OwDistcp(FlowNode flowNode) {
		super(flowNode);
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		java_opts = attJson.getString("java-opts");

		if (java_opts != null && java_opts.isEmpty()) {
			java_opts = null;
		}
		String args_Str = attJson.getString("arg");
		if (args_Str != null && !args_Str.trim().isEmpty()) {
			String[] args_Strs = args_Str.split("[;]");
			for (String argStr : args_Strs) {
				args.add(argStr);
			}
		}

		String distcp_type = attJson.getString("distcp_type");

		if ("1".equals(distcp_type)) {
			String remote_namenode = attJson.getString("remote_namenode");
			OwProperty prop = new OwProperty();
			prop.setName(MAPREDUCE_JOB_HDFS_SERVERS);
			prop.setValue(nameNode + "," + remote_namenode);
			super.getConfiguration().getPropertys().add(prop);
		}
	}

	public static OwDistcp parseXml(Element ele) {
		OwDistcp distcp = new OwDistcp();
		distcp.parseXmls(ele);
		List<Element> argEs = ele.elements("arg");
		if (argEs != null && argEs.size() > 0) {
			distcp.args = new ArrayList<String>();
			for (Element paramE : argEs) {
				distcp.args.add(paramE.getTextTrim());
			}
		}

		distcp.java_opts = ele.elementText("java-opts");
		if (distcp.java_opts != null && distcp.java_opts.isEmpty()) {
			distcp.java_opts = null;
		}
		return distcp;
	}

	public Object toAttributes() {
		JSONObject attObj = super.toAttributesJSONOject();
		if (super.getPrepare() != null) {
			if (super.getPrepare().getDeletes() != null) {
				String deletes = "";
				for (OwDelete owDel : super.getPrepare().getDeletes()) {
					if (!deletes.isEmpty()) {
						deletes += ";";
					}

					deletes += owDel.getPath();
				}
				attObj.put("delete", deletes);
			}
			if (super.getPrepare().getMkdirs() != null) {
				String mkdirs = "";
				for (OwMkdir owdMk : super.getPrepare().getMkdirs()) {
					if (!mkdirs.isEmpty()) {
						mkdirs += ";";
					}

					mkdirs += owdMk.getPath();
				}
				attObj.put("mkdir", mkdirs);
			}
		}

		if (java_opts != null) {
			attObj.put("java-opts", java_opts);
		}

		if (args != null && !args.isEmpty()) {
			String pas = "";
			for (String param : args) {
				if (!pas.isEmpty()) {
					pas += ";";
				}
				pas += param;
			}
			attObj.put("arg", pas);
		}

		if (this.configuration != null) {
			List<OwProperty> props = this.configuration.getPropertys();
			if (props != null) {
				for (OwProperty prop : props) {
					if (MAPREDUCE_JOB_HDFS_SERVERS.equals(prop.getName())) {
						attObj.put("distcp_type", "1");
						String[] hdfs_servers = prop.getValue().split(",");
						attObj.put("remote_namenode", hdfs_servers[1]);
					} else {
						attObj.put("distcp_type", "0");
					}
				}
			}
		}
		return attObj.toJSONString();
	}

	public String getJava_opts() {
		return java_opts;
	}

	public void setJava_opts(String java_opts) {
		this.java_opts = java_opts;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}
}
