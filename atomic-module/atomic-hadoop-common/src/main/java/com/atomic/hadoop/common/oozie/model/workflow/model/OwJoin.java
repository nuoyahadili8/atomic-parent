package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("join")
public class OwJoin  implements OwChoiceNode{
	public OwJoin(FlowNode flowNode) {
		name = flowNode.getText();
		to = flowNode.getTo().get(0);
	}
	public OwJoin() {
		super();
	}
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String to;
	
	public static List<OwJoin> parseXml(List<Element> jElms) {
		List<OwJoin> ojoins = new ArrayList<OwJoin>();
		for(Element ele:jElms){
			ojoins.add(OwJoin.parseXml(ele));
		}
		return ojoins;
	}
	private static OwJoin parseXml(Element ele) {
		OwJoin ojoin = new OwJoin();
		ojoin.name = ele.attributeValue("name");
		ojoin.to = ele.attributeValue("to");
		return ojoin;
	}
	public FlowNode toFlowNode() {
		FlowNode fn =new FlowNode();
		fn.setText(name);
		fn.setType("6");
		List<String> tos=new ArrayList<String>();
		tos.add(to);
		fn.setTo(tos);
		
		return fn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OwJoin other = (OwJoin) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "OwJoin [name=" + name + ", to=" + to + "]";
	}
	public String valid() {
		if(this.to==null){
			return "汇集节点后续路径不可为空！";
		}
		return null;
	}
}
