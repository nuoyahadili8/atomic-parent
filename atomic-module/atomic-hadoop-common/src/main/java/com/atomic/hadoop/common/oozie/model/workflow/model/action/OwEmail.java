package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

@XStreamAlias("email")
public class OwEmail implements OwActionNode {

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:email-action:0.2";
	private String to;
	private String cc;
	private String subject;
	private String body;
	private String content_type = "text/html;charset=utf-8";

	public OwEmail() {
		super();
	}

	public OwEmail(FlowNode flowNode) {
		if (flowNode.getAttribute() == null) {
			try {
				throw new Exception(flowNode.getText() + "未配置");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		to = attJson.getString("to");
		cc = attJson.getString("cc");
		if (StringUtils.isEmpty(cc)) {
			cc = null;
		}
		subject = attJson.getString("theme");
		body = attJson.getString("body");
		// content_type = attJson.getString("content_type");
	}

	public static OwEmail parseXml(Element ele) {
		OwEmail owEm = new OwEmail();
		owEm.to = ele.elementText("to");
		owEm.cc = ele.elementText("cc");
		owEm.subject = ele.elementText("subject");
		owEm.body = ele.elementText("body");
		owEm.content_type = ele.elementText("content_type");
		return owEm;
	}

	public Object toAttributes() {
		JSONObject attObj = new JSONObject();
		if (to != null) {
			attObj.put("to", to);
		}
		if (cc != null) {
			attObj.put("cc", cc);
		}
		if (subject != null) {
			attObj.put("theme", subject);
		}
		if (body != null) {
			attObj.put("body", body);
		}
		if (content_type != null) {
			attObj.put("content_type", content_type);
		}
		return attObj.toJSONString();
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
}
