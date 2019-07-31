package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("start")
public class OwStart implements OwNode{
	@XStreamAsAttribute
	private String to;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		OwStart other = (OwStart) obj;
		if (to == null) {
			if (other.to != null){
				return false;
			}
		} else if (!to.equals(other.to)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OwStart [to=" + to + "]";
	}

	public static OwStart parseXml(Element element) {
		OwStart start =new OwStart();
		start.to = element.attributeValue("to");
		return start;
	}

	public FlowNode toFlowNode() {
		FlowNode fn =new FlowNode();
		fn.setNodeid(1);
		fn.setText("start");
		fn.setType("start");
		List<String> tos=new ArrayList<String>();
		tos.add(to);
		fn.setTo(tos);
		return fn;
	}
}
