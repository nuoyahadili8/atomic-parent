package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwAbstractActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwConfiguration;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.dom4j.Element;

import java.util.*;
import java.util.Map.Entry;

@XStreamAlias("ftp")
public class OwFtp extends OwAbstractActionNode implements OwActionNode {

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:ftp-action:0.1";
	@XStreamImplicit(itemFieldName = "arg")
	private List<String> args = new ArrayList<String>();
	@XStreamImplicit(itemFieldName = "file")
	private List<String> files = new ArrayList<String>();
	@XStreamOmitField
	private static Map<String, String> map = new HashMap<String, String>();
	@XStreamOmitField
	private static Set<String> argSet;
	static {
		argSet = new HashSet<String>();
		argSet.add("ip");
		argSet.add("port");
		argSet.add("user");
		argSet.add("pwd");
		argSet.add("type");
		argSet.add("mode");
		argSet.add("direction");
		argSet.add("remote_dir");
		argSet.add("hdfs_dir");
		argSet.add("file_name");
	}

	public OwFtp() {
		super();
	}

	public OwFtp(FlowNode flowNode) throws Exception {
		super(flowNode);

		OwConfiguration config = super.getConfiguration();
		OwProperty prop1 = new OwProperty();
		prop1.setName("mapred.mapper.new-api");
		prop1.setValue("false");
		OwProperty prop2 = new OwProperty();
		prop2.setName("mapred.reducer.new-api");
		prop2.setValue("false");

		config.getPropertys().add(prop1);
		config.getPropertys().add(prop2);

		Object atts = flowNode.getAttribute();
		JSONObject attObj = JSON.parseObject(atts.toString());
		for (Entry<String, Object> entry : attObj.entrySet()) {
			if (argSet.contains(entry.getKey())) {
				String arg = entry.getKey() + ":" + entry.getValue();
				args.add(arg);
			}
		}
	}

	public static OwFtp parseXml(Element element) {
		OwFtp ftp = new OwFtp();
		ftp.parseXmls(element);

		for (Object se : element.elements("arg")) {
			Element see = (Element) se;
			String param = see.getText();
			ftp.args.add(param);
			if (param.contains("remote_dir:")) {
				map.put("remote_dir", param);
			}
			if (param.contains("file_name:")) {
				map.put("file_name", param);
			}
			if (param.contains("ip:")) {
				map.put("ip", param);
			}
			if (param.contains("type:")) {
				map.put("type", param);
			}
			if (param.contains("hdfs_dir:")) {
				map.put("hdfs_dir", param);
			}
			if (param.contains("mode:")) {
				map.put("mode", param);
			}
			if (param.contains("port:")) {
				map.put("port", param);
			}
			if (param.contains("pwd:")) {
				map.put("pwd", param);
			}
			if (param.contains("user:")) {
				map.put("user", param);
			}
			if (param.contains("direction:")) {
				map.put("direction", param);
			}
		}
		List<Element> fileEs = element.elements("file");
		if (fileEs != null) {
			ftp.files = new ArrayList<String>();
			for (Element fileE : fileEs) {
				ftp.files.add(fileE.getTextTrim());
			}
		}

		return ftp;
	}

	public Object toAttributes() {
		JSONObject attObj = new JSONObject();
		for (String arg : args) {
			String[] kvs = arg.split(":");
			attObj.put(kvs[0], kvs[1]);
		}

		return attObj.toJSONString();
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

	public static Map<String, String> getMap() {
		return map;
	}

	public static void setMap(Map<String, String> map) {
		OwFtp.map = map;
	}
}
