package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@XStreamAlias("decision")
public class OwDecision implements OwChoiceNode {

	@XStreamAsAttribute
	private String name;
	@XStreamAlias("switch")
	private OwSwitch Switch;

	public OwDecision() {
		super();
	}

	public OwDecision(FlowNode flowNode) {
		if (flowNode.getAttribute() == null) {
			try {
				throw new Exception(flowNode.getText() + "未配置");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		name = flowNode.getText();
		Switch = new OwSwitch();

		List<String> tos = flowNode.getTo();
		List<OwCase> cases = new ArrayList<OwCase>();
		Object atts = flowNode.getAttribute();
		JSONObject attObj = JSON.parseObject(atts.toString());

		int defaultIndex = Integer.parseInt(attObj.get("defaultCase").toString());
		OwDefault defaultP = new OwDefault();
		defaultP.setTo(tos.get(defaultIndex));
		Switch.setDEFAULT(defaultP);

		for (Entry<String, Object> entry : attObj.entrySet()) {
			if (!"defaultCase".equalsIgnoreCase(entry.getKey()) && !("case" + defaultIndex).equals(entry.getKey())) {
				OwCase scase = new OwCase();
				int index = Integer.parseInt(entry.getKey().substring(4));
				scase.setTo(tos.get(index));
				scase.setCASE(entry.getValue().toString());
				cases.add(scase);
			}
		}
		Switch.setCases(cases);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OwSwitch getSwitch() {
		return Switch;
	}

	public void setSwitch(OwSwitch switch1) {
		Switch = switch1;
	}

	public static List<OwDecision> parseXml(List<Element> dElms) {
		List<OwDecision> odecisions = new ArrayList<OwDecision>();
		for (Element ele : dElms) {
			odecisions.add(OwDecision.parseXml(ele));
		}
		return odecisions;
	}

	private static OwDecision parseXml(Element ele) {
		OwDecision odecision = new OwDecision();
		odecision.name = ele.attributeValue("name");
		Element eSwitch = ele.element("switch");
		odecision.Switch = OwSwitch.parseXml(eSwitch);

		return odecision;
	}

	public FlowNode toFlowNode() {
		FlowNode fn = new FlowNode();
		fn.setText(name);
		fn.setType("4");
		List<String> tos = new ArrayList<String>();
		for (OwCase scase : Switch.getCases()) {
			tos.add(scase.getTo());
		}
		tos.add(Switch.getDEFAULT().getTo());

		fn.setTo(tos);
		fn.setAttribute(toAttributes());
		return fn;
	}

	private Object toAttributes() {
		JSONObject attObj = new JSONObject();
		for (OwCase scase : Switch.getCases()) {
			attObj.put(scase.getTo(), scase.getCASE());
		}
		attObj.put(Switch.getDEFAULT().getTo(), "default");

		return attObj.toJSONString();
	}

	public String valid() {
		if (Switch == null) {
			return "条件分支节点的后续路径不能为空！";
		}

		return Switch.valid();
	}
}
