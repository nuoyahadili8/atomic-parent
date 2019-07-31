package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.dom4j.Element;

@XStreamAlias("end")
public class OwEnd implements OwNode{
	@XStreamAsAttribute
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		OwEnd other = (OwEnd) obj;
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
		return "OwEnd [name=" + name + "]";
	}

	public static OwEnd parseXml(Element element) {
		OwEnd end =new OwEnd();
		end.name = element.attributeValue("name");
		return end;
	}

	public FlowNode toFlowNode() {
		FlowNode fn =new FlowNode();
		fn.setNodeid(2);
		fn.setText("end");
		fn.setType("end");
		return fn;
	}
}
