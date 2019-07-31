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

@XStreamAlias("hive2")
public class OwHive2 extends OwAbstractActionNode implements OwActionNode {

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:hive2-action:0.1";
	@XStreamAlias("jdbc-url")
	private String jdbc_url;
	private String password;
	protected String script;
	@XStreamImplicit(itemFieldName = "param")
	protected List<String> params;
	@XStreamImplicit(itemFieldName = "argument")
	private List<String> arguments;
	@XStreamImplicit(itemFieldName = "file")
	protected List<String> files;
	@XStreamImplicit(itemFieldName = "archive")
	private List<String> archives;

	public OwHive2() {
		super();
	}

	public OwHive2(FlowNode flowNode) {
		super(flowNode);
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);

		if (attJson.getString("script").length() != 0) {
			script = attJson.getString("script");
		}

		if (attJson.getString("jdbc-url").length() != 0) {
			jdbc_url = attJson.getString("jdbc-url");
		}
		if (attJson.getString("password").length() != 0) {
			password = attJson.getString("password");
		}

		String paramSs = attJson.getString("param");
		if (paramSs != null && !paramSs.trim().isEmpty()) {
			params = new ArrayList<String>();
			String[] paramStrs = paramSs.split("[;]");
			for (String paramStr : paramStrs) {
				params.add(paramStr);
			}
		}

		String argumentSs = attJson.getString("arguments");
		if (argumentSs != null && !argumentSs.trim().isEmpty()) {
			arguments = new ArrayList<String>();
			String[] argumentStrs = argumentSs.split(";");
			for (String argumentStr : argumentStrs) {
				arguments.add(argumentStr);
			}
		}

		String fileStrs = attJson.getString("files");
		if (fileStrs != null && !fileStrs.trim().isEmpty()) {
			String[] filesStrs = fileStrs.split(";");
			files = new ArrayList<String>();
			for (String fileStr : filesStrs)
				files.add(fileStr);
		}

		String archiveStrs = attJson.getString("archives");
		if (archiveStrs != null && !archiveStrs.trim().isEmpty()) {
			String[] archivesStrs = archiveStrs.split(";");
			archives = new ArrayList<String>();
			for (String archStr : archivesStrs)
				archives.add(archStr);
		}
	}

	public static OwHive2 parseXml(Element ele) {
		OwHive2 hive2 = new OwHive2();
		hive2.parseXmls(ele);
		List<Element> paramEs = ele.elements("param");
		if (paramEs != null && paramEs.size() > 0) {
			hive2.params = new ArrayList<String>();
			for (Element paramE : paramEs) {
				hive2.params.add(paramE.getTextTrim());
			}
		}

		hive2.jdbc_url = ele.elementText("jdbc-url");
		hive2.password = ele.elementText("password");
		hive2.script = ele.elementText("script");

		return hive2;
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
		if (password != null) {
			attObj.put("password", password);
		}
		if (jdbc_url != null) {
			attObj.put("jdbc-url", jdbc_url);
		}
		if (script != null) {
			attObj.put("script", script);
		}
		if (params != null && !params.isEmpty()) {
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

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getJdbc_url() {
		return jdbc_url;
	}

	public void setJdbc_url(String jdbc_url) {
		this.jdbc_url = jdbc_url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
