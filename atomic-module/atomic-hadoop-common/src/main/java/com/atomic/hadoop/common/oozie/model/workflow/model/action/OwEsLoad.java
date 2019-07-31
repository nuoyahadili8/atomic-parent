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
import org.dom4j.Element;

import java.util.List;

@XStreamAlias("es")
public class OwEsLoad extends OwAbstractActionNode implements OwActionNode {
	private static String INPUT_DIR = "input.dir";
	private static String SPLIT_STR = "split.str";
	private static String ES_IN_JSON = "es.in.json";
	private static String FIELDS_LIST = "fields.list";
	private static String ES_NODE = "es.nodes";
	private static String ES_RESOURCE = "es.resource";

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:es-action:0.1";

	public OwEsLoad() {
		super();
	}

	public OwEsLoad(FlowNode flowNode) {
		super(flowNode);

		OwConfiguration config = super.getConfiguration();
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);

		OwProperty propP1 = new OwProperty();
		propP1.setName("input.dir");
		propP1.setValue(attJson.getString(INPUT_DIR));
		config.getPropertys().add(propP1);

		OwProperty propP2 = new OwProperty();
		propP2.setName("split.separator");
		propP2.setValue(attJson.getString(SPLIT_STR));
		config.getPropertys().add(propP2);

		OwProperty propP3 = new OwProperty();
		propP3.setName("in.json");
		propP3.setValue(attJson.getString(ES_IN_JSON));
		config.getPropertys().add(propP3);

		OwProperty propP4 = new OwProperty();
		propP4.setName("fields.list");
		propP4.setValue(attJson.getString(FIELDS_LIST));
		config.getPropertys().add(propP4);

		OwProperty propP5 = new OwProperty();
		propP5.setName("es.nodes");
		propP5.setValue(attJson.getString(ES_NODE));
		config.getPropertys().add(propP5);

		OwProperty propP6 = new OwProperty();
		propP6.setName("es.resource");
		propP6.setValue(attJson.getString(ES_RESOURCE));
		config.getPropertys().add(propP6);

	}

	public static OwEsLoad parseXml(Element element) {
		OwEsLoad esload = new OwEsLoad();
		esload.parseXmls(element);

		return esload;
	}

	public Object toAttributes() {
		JSONObject attObj = super.toAttributesJSONOject();

		if (this.configuration != null) {
			List<OwProperty> props = this.configuration.getPropertys();
			if (props != null) {
				for (OwProperty prop : props) {
					if ("input.dir".equals(prop.getName())) {
						attObj.put(INPUT_DIR, prop.getValue());
					} else if ("split.separator".equals(prop.getName())) {
						attObj.put(SPLIT_STR, prop.getValue());
					} else if ("in.json".equals(prop.getName())) {
						attObj.put(ES_IN_JSON, prop.getValue());
					} else if ("fields.list".equals(prop.getName())) {
						attObj.put(FIELDS_LIST, prop.getValue());
					} else if ("es.nodes".equals(prop.getName())) {
						attObj.put(ES_NODE, prop.getValue());
					} else if ("es.resource".equals(prop.getName())) {
						attObj.put(ES_RESOURCE, prop.getValue());
					}
				}
			}
		}
		return attObj.toJSONString();
	}
}
