package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import cn.hutool.core.util.StrUtil;
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

@XStreamAlias("shell")
public class OwShell extends OwAbstractActionNode implements OwActionNode {

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:shell-action:0.3";
	private String exec;
	@XStreamImplicit(itemFieldName = "argument")
	private List<String> arguments;
	@XStreamImplicit(itemFieldName = "env-var")
	private List<String> env_vars;
	@XStreamImplicit(itemFieldName = "file")
	private List<String> files;
	@XStreamImplicit(itemFieldName = "archive")
	private List<String> archives;
	@XStreamAlias("capture-output")
	private String capture_output;

	public OwShell() {
		super();
	}

	public OwShell(FlowNode flowNode) {
		if (flowNode.getAttribute() == null) {
			try {
				throw new Exception(flowNode.getText() + "未配置");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		exec = attJson.getString("exec");
		String argumentss = attJson.getString("argument");
		if (argumentss != null) {
			String[] argumentArr = argumentss.split(";");
			arguments = new ArrayList<String>();
			for (String arg : argumentArr)
				arguments.add(arg);
		}
		String envvars = attJson.getString("env-var");
		if (envvars != null) {
			String[] envvs = envvars.split(";");
			env_vars = new ArrayList<String>();
			for (String env : envvs)
				env_vars.add(env);
		}
		String fileStrs = attJson.getString("file");
		if (StrUtil.isNotEmpty(fileStrs)) {
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

	public static OwShell parseXml(Element ele) {
		OwShell owSh = new OwShell();
		owSh.parseXmls(ele);

		owSh.exec = ele.elementText("exec");

		List<Element> argEs = ele.elements("argument");
		if (argEs != null) {
			owSh.arguments = new ArrayList<String>();
			for (Element argE : argEs) {
				owSh.arguments.add(argE.getTextTrim());
			}
		}
		List<Element> envEs = ele.elements("env-var");
		if (envEs != null) {
			owSh.env_vars = new ArrayList<String>();
			for (Element envE : envEs) {
				owSh.env_vars.add(envE.getTextTrim());
			}
		}
		List<Element> fileEs = ele.elements("file");
		if (fileEs != null) {
			owSh.files = new ArrayList<String>();
			for (Element fileE : fileEs) {
				owSh.files.add(fileE.getTextTrim());
			}
		}
		List<Element> archiveEs = ele.elements("archive");
		if (archiveEs != null) {
			owSh.archives = new ArrayList<String>();
			for (Element archiveE : archiveEs) {
				owSh.archives.add(archiveE.getTextTrim());
			}
		}

		if (ele.element("capture-output") == null) {
			owSh.capture_output = null;
		} else {
			owSh.capture_output = "";
		}

		return owSh;
	}

	public Object toAttributes() {
		JSONObject attObj = new JSONObject();
		attObj.put("exec", exec);

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

		if (env_vars != null) {
			String envStr = "";
			for (String env : env_vars) {
				if (!envStr.isEmpty()) {
					envStr += ";";
				}
				envStr += env;
			}
			attObj.put("env-var", envStr);
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

		if (capture_output != null) {
			attObj.put("capture-output", "Y");
		}

		return attObj.toJSONString();
	}
}
