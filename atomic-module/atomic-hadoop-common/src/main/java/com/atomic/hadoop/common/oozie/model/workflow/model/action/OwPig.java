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

@XStreamAlias("pig")
public class OwPig extends OwAbstractActionNode implements OwActionNode {

	private String script;
	@XStreamImplicit(itemFieldName = "param")
	private List<String> params;
	@XStreamImplicit(itemFieldName = "argument")
	private List<String> arguments;
	@XStreamImplicit(itemFieldName = "file")
	private List<String> files;
	@XStreamImplicit(itemFieldName = "archive")
	private List<String> archives;

	public OwPig() {
		super();
	}

	public OwPig(FlowNode flowNode) {
		super(flowNode);
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		script = attJson.getString("script");

		String paramss = attJson.getString("param");
		if (paramss != null) {
			String[] paramArr = paramss.split(";");
			params = new ArrayList<String>();
			for (String para : paramArr)
				params.add(para);
		}

		String argumentss = attJson.getString("argument");
		if (argumentss != null) {
			String[] argumentArr = argumentss.split(";");
			arguments = new ArrayList<String>();
			for (String arg : argumentArr)
				arguments.add(arg);
		}
		String fileStrs = attJson.getString("file");
		if (fileStrs != null) {
			String[] filesStrs = fileStrs.split(";");
			files = new ArrayList<String>();
			for (String fileStr : filesStrs)
				files.add(fileStr);
		}
		String archiveStrs = attJson.getString("archive");
		if (archiveStrs != null) {
			String[] archivesStrs = archiveStrs.split(";");
			archives = new ArrayList<String>();
			for (String archStr : archivesStrs)
				archives.add(archStr);
		}
	}

	public static OwPig parseXml(Element ele) {
		OwPig pig = new OwPig();
		pig.parseXmls(ele);

		pig.script = ele.elementText("script");

		List<Element> paraEs = ele.elements("param");
		if (paraEs != null) {
			pig.arguments = new ArrayList<String>();
			for (Element paraE : paraEs) {
				pig.arguments.add(paraE.getTextTrim());
			}
		}

		List<Element> argEs = ele.elements("argument");
		if (argEs != null) {
			pig.arguments = new ArrayList<String>();
			for (Element argE : argEs) {
				pig.arguments.add(argE.getTextTrim());
			}
		}

		List<Element> fileEs = ele.elements("file");
		if (fileEs != null) {
			pig.files = new ArrayList<String>();
			for (Element fileE : fileEs) {
				pig.files.add(fileE.getTextTrim());
			}
		}
		List<Element> archiveEs = ele.elements("archive");
		if (archiveEs != null) {
			pig.archives = new ArrayList<String>();
			for (Element archiveE : archiveEs) {
				pig.archives.add(archiveE.getTextTrim());
			}
		}
		return pig;
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
				attObj.put("delete", mkdirs);
			}
		}

		attObj.put("script", script);

		if (params != null) {
			String paramsStr = "";
			for (String param : params) {
				if (!paramsStr.isEmpty()) {
					paramsStr += ";";
				}
				paramsStr += param;
			}
			attObj.put("param", paramsStr);
		}

		if (arguments != null) {
			String argumentsStr = "";
			for (String argument : arguments) {
				if (!argumentsStr.isEmpty()) {
					argumentsStr += ";";
				}
				argumentsStr += argument;
			}
			attObj.put("argument", argumentsStr);
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

		if (archives != null) {
			String ais = "";
			for (String arch : archives) {
				if (!ais.isEmpty()) {
					ais += ";";
				}
				ais += arch;
			}
			attObj.put("archive", ais);
		}
		return attObj.toJSONString();
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
