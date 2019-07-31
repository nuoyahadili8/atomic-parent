package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("ssh")
public class OwSsh implements OwActionNode {

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:ssh-action:0.2";
	private String host;
	private String command;
	@XStreamImplicit(itemFieldName = "arg")
	private List<String> arg_s;
	@XStreamImplicit(itemFieldName = "args")
	private List<String> args_s;
	@XStreamAlias("capture-output")
	private String capture_output;

	public OwSsh() {
		super();
	}

	public OwSsh(FlowNode flowNode) {
		if (flowNode.getAttribute() == null) {
			try {
				throw new Exception(flowNode.getText() + "未配置");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		host = attJson.getString("host");
		command = attJson.getString("command");

		String argsArrs = attJson.getString("args");
		if (argsArrs != null) {
			String[] argsArr = argsArrs.split(";");
			args_s = new ArrayList<String>();
			for (String args : argsArr)
				args_s.add(args);
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

	public static OwSsh parseXml(Element ele) {
		OwSsh owSh = new OwSsh();

		owSh.host = ele.elementText("host");
		owSh.command = ele.elementText("command");

		List<Element> argsEs = ele.elements("args");
		if (argsEs != null) {
			owSh.args_s = new ArrayList<String>();
			for (Element argsE : argsEs) {
				owSh.args_s.add(argsE.getTextTrim());
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
		attObj.put("host", host);
		attObj.put("command", command);

		if (arg_s != null) {
			String argStr = "";
			for (String arg : arg_s) {
				if (!argStr.isEmpty()) {
					argStr += ";";
				}
				argStr += arg;
			}
			attObj.put("arg", argStr);
		}
		if (args_s != null) {
			String argsStr = "";
			for (String args : args_s) {
				if (!argsStr.isEmpty()) {
					argsStr += ";";
				}
				argsStr += args;
			}
			attObj.put("args", argsStr);
		}
		if (capture_output != null) {
			attObj.put("capture-output", "Y");
		}
		return attObj.toJSONString();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getArgs_s() {
		return args_s;
	}

	public void setArgs_s(List<String> args_s) {
		this.args_s = args_s;
	}
}
