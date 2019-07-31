package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("kill")
public class OwKill  implements OwChoiceNode{
	public OwKill(){
		super();
	}
	
	@XStreamAsAttribute
	private String name="fail";
	
	private String message="Workflow failed, error message[${wf:errorMessage(wf:lastErrorNode())}]";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public OwKill(String name, String message) {
		super();
		this.name = name;
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		OwKill other = (OwKill) obj;
		if (message == null) {
			if (other.message != null){
				return false;
			}
		} else if (!message.equals(other.message)){
			return false;
		}

		if (name == null) {
			if (other.name != null){
				return false;
			}
		} else if (!name.equals(other.name)){
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "OwKill [name=" + name + ", message=" + message + "]";
	}
	
	public static OwKill parseXml(Element ele){
		OwKill okill = new OwKill();
		okill.name = ele.attributeValue("name");
		okill.message = ele.attributeValue("message");
		return okill;
	}
	public static List<OwKill> parseXml(List<Element> kElms) {
		List<OwKill> okills = new ArrayList<OwKill>();
		for(Element ele:kElms){
			okills.add(OwKill.parseXml(ele));
		}
		return okills;
	}
	public FlowNode toFlowNode() {
		FlowNode fn =new FlowNode();
		fn.setText(name);
		fn.setType("kill");
		fn.setAttribute(toAttributes());
		return fn;
	}
	private Object toAttributes() {
		JSONObject attObj = new JSONObject();
		if(message!=null){
			attObj.put("message", message);
		}
		return attObj.toJSONString();
	}
}
