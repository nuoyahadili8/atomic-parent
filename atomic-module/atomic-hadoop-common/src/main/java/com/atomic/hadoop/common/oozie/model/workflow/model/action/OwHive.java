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

@XStreamAlias("hive")
public class OwHive extends OwAbstractActionNode implements OwActionNode {

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:hive-action:0.5";
	protected String script;
	@XStreamImplicit(itemFieldName = "param")
	protected List<String> params;
	@XStreamImplicit(itemFieldName = "argument")
	private List<String> arguments;
	@XStreamImplicit(itemFieldName = "file")
	protected List<String> files;
	@XStreamImplicit(itemFieldName = "archive")
	private List<String> archives;

	public OwHive() {
		super();
	}

	public OwHive(FlowNode flowNode) {
		super(flowNode);
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		script = attJson.getString("script");
		String paramSs = attJson.getString("param");

		if (paramSs != null) {
			params = new ArrayList<String>();
			String[] paramStrs = paramSs.split("[;]");
			for (String paramStr : paramStrs) {
				params.add(paramStr);
			}
		}

		String fileStrs = attJson.getString("file");
		if (fileStrs != null) {
			String[] filesStrs = fileStrs.split(";");
			files = new ArrayList<String>();
			for (String fileStr : filesStrs)
				files.add(fileStr);
		}
		String archiveStrs = attJson.getString("archives");
		if (archiveStrs != null) {
			String[] archivesStrs = archiveStrs.split(";");
			archives = new ArrayList<String>();
			for (String archStr : archivesStrs)
				archives.add(archStr);
		}
	}

	public static OwHive parseXml(Element ele) {
		OwHive hive = new OwHive();
		hive.parseXmls(ele);
		List<Element> paramEs = ele.elements("param");
		if (paramEs != null && paramEs.size() > 0) {
			hive.params = new ArrayList<String>();
			for (Element paramE : paramEs) {
				hive.params.add(paramE.getTextTrim());
			}
		}

		hive.script = ele.elementText("script");
		return hive;
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
		if (script != null) {
			attObj.put("script", script);
		}
		if (params != null) {
			String pas = "";
			for (String param : params) {
				if (!pas.isEmpty()) {
					pas += ";";
				}
				pas += param;
			}
			attObj.put("param", pas);
		}
		if (files != null) {
			String fis = "";
			for (String file : files) {
				if (!fis.isEmpty()) {
					fis += ";";
				}
				fis += file;
			}
			attObj.put("file", fis);
		}
		return attObj.toJSONString();
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public List<String> getArchives() {
		return archives;
	}

	public void setArchives(List<String> archives) {
		this.archives = archives;
	}
}
