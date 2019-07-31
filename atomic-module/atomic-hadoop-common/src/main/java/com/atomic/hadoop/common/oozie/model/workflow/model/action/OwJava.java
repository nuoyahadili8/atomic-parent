package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwAbstractActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwDelete;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwMkdir;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("java")
public class OwJava extends OwAbstractActionNode implements OwActionNode {

	@XStreamAlias("main-class")
	private String main_class;
	@XStreamAlias("java-opts")
	private String java_opts;
	@XStreamImplicit(itemFieldName = "java-opt")
	private List<String> java_optss = new ArrayList<String>();
	@XStreamImplicit(itemFieldName = "arg")
	private List<String> args = new ArrayList<String>();
	@XStreamAlias("file")
	private String file;
	@XStreamAlias("archive")
	private String archive;
	@XStreamAlias("capture-output")
	private String capture_output;

	public OwJava() {

	}

	public OwJava(FlowNode flowNode) {
		super(flowNode);
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		main_class = attJson.getString("main-class");
		file = attJson.getString("file");

		if (file != null && file.trim().isEmpty()) {
			file = null;
		}

		archive = attJson.getString("archive");
		java_opts = attJson.getString("java-opts");
		String java_optss_Str = attJson.getString("java-opt");
		if (java_optss_Str != null && !java_optss_Str.trim().isEmpty()) {
			String[] java_optss_Strs = java_optss_Str.split("[;]");
			for (String java_optssStr : java_optss_Strs) {
				java_optss.add(java_optssStr);
			}
		}
		String args_Str = attJson.getString("arg");
		if (args_Str != null && !args_Str.trim().isEmpty()) {
			String[] args_Strs = args_Str.split("[;]");
			for (String argStr : args_Strs) {
				args.add(argStr);
			}
		}

		String capture_Str = attJson.getString("capture-output");
		if (capture_Str != null) {
			capture_Str = capture_Str.trim();
			if (("Y").equalsIgnoreCase(capture_Str)) {
				capture_output = "";
			} else {
				capture_output = null;
			}
		}
	}

	public static OwJava parseXml(Element ele) {
		OwJava java = new OwJava();
		java.parseXmls(ele);
		List<Element> argEs = ele.elements("arg");
		if (argEs != null && argEs.size() > 0) {
			java.args = new ArrayList<String>();
			for (Element paramE : argEs) {
				java.args.add(paramE.getTextTrim());
			}
		}

		List<Element> java_optssEs = ele.elements("java-opt");
		if (java_optssEs != null && java_optssEs.size() > 0) {
			java.java_optss = new ArrayList<String>();
			for (Element paramE : java_optssEs) {
				java.java_optss.add(paramE.getTextTrim());
			}
		}
		java.main_class = ele.elementText("main-class");
		java.file = ele.elementText("file");
		java.java_opts = ele.elementText("java-opts");
		java.archive = ele.elementText("archive");

		if (ele.element("capture-output") == null) {
			java.capture_output = null;
		} else {
			java.capture_output = "";
		}
		return java;
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

		if (main_class != null) {
			attObj.put("main-class", main_class);
		}
		if (java_opts != null) {
			attObj.put("java-opts", java_opts);
		}
		if (archive != null) {
			attObj.put("archive", archive);
		}
		if (capture_output != null) {
			attObj.put("capture-output", "Y");
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
		if (java_optss != null && !java_optss.isEmpty()) {
			String pas = "";
			for (String param : java_optss) {
				if (!pas.isEmpty()) {
					pas += ";";
				}
				pas += param;
			}
			attObj.put("java-opt", pas);
		}
		return attObj.toJSONString();
	}

	public String getMain_class() {
		return main_class;
	}

	public void setMain_class(String main_class) {
		this.main_class = main_class;
	}

	public String getArchive() {
		return archive;
	}

	public void setArchive(String archive) {
		this.archive = archive;
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

	public List<String> getJava_optss() {
		return java_optss;
	}

	public void setJava_optss(List<String> java_optss) {
		this.java_optss = java_optss;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getCapture_output() {
		return capture_output;
	}

	public void setCapture_output(String capture_output) {
		this.capture_output = capture_output;
	}
}
