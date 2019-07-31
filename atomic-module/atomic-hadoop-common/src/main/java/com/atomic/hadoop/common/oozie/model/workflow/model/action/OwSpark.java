package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwAbstractActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwDelete;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwMkdir;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("spark")
public class OwSpark extends OwAbstractActionNode implements OwActionNode {

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:spark-action:0.1";
	private String master;
	private String mode;
	private String name;
	@XStreamAlias("class")
	private String main_class;
	private String jar;
	@XStreamAlias("spark-opts")
	private String spark_opts;
	@XStreamImplicit(itemFieldName = "arg")
	private List<String> args = new ArrayList<String>();

	public OwSpark() {
		super();
	}

	public OwSpark(FlowNode flowNode) {
		super(flowNode);
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		master = attJson.getString("master");
		mode = attJson.getString("mode");
		name = attJson.getString("name");
		main_class = attJson.getString("class");
		jar = attJson.getString("jar");
		spark_opts = attJson.getString("spark-opts");

		String args_Str = attJson.getString("arg");
		if (args_Str != null && !args_Str.trim().isEmpty()) {
			String[] args_Strs = args_Str.split("[;]");
			for (String argStr : args_Strs) {
				args.add(argStr);
			}
		}
	}

	public static OwSpark parseXml(Element ele) {
		OwSpark spark = new OwSpark();
		spark.parseXmls(ele);
		List<Element> argEs = ele.elements("arg");
		if (argEs != null && argEs.size() > 0) {
			spark.args = new ArrayList<String>();
			for (Element paramE : argEs) {
				spark.args.add(paramE.getTextTrim());
			}
		}

		spark.master = ele.elementText("master");
		spark.mode = ele.elementText("mode");
		spark.name = ele.elementText("name");
		spark.main_class = ele.elementText("class");
		spark.jar = ele.elementText("jar");
		spark.spark_opts = ele.elementText("spark-opts");

		return spark;
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
		if (super.job_xmls != null && !super.job_xmls.isEmpty()) {
			String jxmls = "";
			for (String jxml : job_xmls) {
				if (!jxmls.isEmpty()) {
					jxmls += ";";
				}
				jxmls += jxml;
			}
			attObj.put("job-xml", jxmls);

		}
		if (master != null) {
			attObj.put("master", master);
		}
		if (mode != null) {
			attObj.put("mode", mode);
		}
		if (name != null) {
			attObj.put("name", name);
		}
		if (main_class != null) {
			attObj.put("class", main_class);
		}
		if (jar != null) {
			attObj.put("jar", jar);
		}
		if (spark_opts != null) {
			attObj.put("spark-opts", spark_opts);
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

		return attObj.toJSONString();
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMain_class() {
		return main_class;
	}

	public void setMain_class(String main_class) {
		this.main_class = main_class;
	}

	public String getJar() {
		return jar;
	}

	public void setJar(String jar) {
		this.jar = jar;
	}

	public String getSpark_opts() {
		return spark_opts;
	}

	public void setSpark_opts(String spark_opts) {
		this.spark_opts = spark_opts;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}
}
