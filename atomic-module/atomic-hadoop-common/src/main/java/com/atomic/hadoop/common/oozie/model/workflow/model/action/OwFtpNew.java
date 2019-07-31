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
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.dom4j.Element;

import java.util.*;
import java.util.Map.Entry;

@XStreamAlias("ftpNew")
public class OwFtpNew extends OwAbstractActionNode implements OwActionNode {

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:ftpNew-action:0.1";
	@XStreamOmitField
	private List<String> args = new ArrayList<String>();
	@XStreamOmitField
	private static Set<String> argSet;
	@XStreamOmitField
	private static Map<String, String> map = new HashMap<String, String>();

	static {
		argSet = new HashSet<String>();
		argSet.add("name");
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

	public OwFtpNew() {
		super();
	}

	public OwFtpNew(FlowNode flowNode) {
		super(flowNode);
		OwConfiguration config = super.getConfiguration();

		Object atts = flowNode.getAttribute();
		JSONObject attObj = JSON.parseObject(atts.toString());

		for (Entry<String, Object> entry : attObj.entrySet()) {
			if (argSet.contains(entry.getKey())) {
				OwProperty prop3 = new OwProperty();
				prop3.setName("ftp." + entry.getKey());
				prop3.setValue((String) entry.getValue());
				config.getPropertys().add(prop3);
				String arg = entry.getKey() + ":" + entry.getValue();
				args.add(arg);
			}
		}
	}

	public static OwFtpNew parseXml(Element element) {
		OwFtpNew ftpNew = new OwFtpNew();
		ftpNew.parseXmls(element);

		for (OwProperty owProperty : ftpNew.getConfiguration().getPropertys()) {
			if (argSet.contains(owProperty.getName())) {
				String arg = owProperty.getName() + ":" + owProperty.getValue();
				ftpNew.getArgs().add(arg);
			}
		}

		return ftpNew;
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

	public static Map<String, String> getMap() {
		return map;
	}

	public static void setMap(Map<String, String> map) {
		OwFtpNew.map = map;
	}
}
