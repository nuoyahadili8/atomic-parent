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
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("sqoop")
public class OwSqoop extends OwAbstractActionNode implements OwActionNode {

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:sqoop-action:0.4";
	private String command;
	@XStreamImplicit(itemFieldName = "arg")
	private List<String> args;
	@XStreamImplicit(itemFieldName = "file")
	private List<String> files;
	@XStreamImplicit(itemFieldName = "archive")
	private List<String> archives;

	public OwSqoop() {
		super();
	}

	public OwSqoop(FlowNode flowNode) {
		super(flowNode);
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		String comstr = attJson.getString("command");
		if (comstr != null && !StringUtils.isEmpty(comstr.trim()))
			command = comstr.trim().replaceAll(";", " ");

		String argStrs = attJson.getString("arg");
		if (argStrs != null && !StringUtils.isEmpty(argStrs)) {
			String[] argsStrs = argStrs.split(";");
			args = new ArrayList<String>();
			for (String argStr : argsStrs)
				args.add(argStr);
		}

		String fileStrs = attJson.getString("file");
		if (fileStrs != null && !StringUtils.isEmpty(fileStrs)) {
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

	public static OwSqoop parseXml(Element ele) {
		OwSqoop owSq = new OwSqoop();
		owSq.parseXmls(ele);
		owSq.command = ele.elementText("command");
		List<Element> argsEs = ele.elements("arg");
		if (argsEs != null) {
			owSq.args = new ArrayList<String>();
			for (Element argsE : argsEs) {
				owSq.args.add(argsE.getTextTrim());
			}
		}
		List<Element> fileEs = ele.elements("file");
		if (fileEs != null) {
			owSq.files = new ArrayList<String>();
			for (Element fileE : fileEs) {
				owSq.files.add(fileE.getTextTrim());
			}
		}
		return owSq;
	}

	public Object toAttributes() {
		JSONObject attObj = new JSONObject();
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

		if (command != null && !StringUtils.isEmpty(command)) {
			attObj.put("command", command.replaceAll(" ", ";"));
		}
		if (args != null && !args.isEmpty()) {
			String arggs = "";
			for (String arg : args) {
				if (!arggs.isEmpty()) {
					arggs += ";";
				}
				arggs += arg;
			}
			attObj.put("arg", arggs);
		}
		if (files != null && !files.isEmpty()) {
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

	public List<String> getJob_xmls() {
		return job_xmls;
	}

	public void setJob_xmls(List<String> job_xmls) {
		this.job_xmls = job_xmls;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
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
