package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("fork")
public class OwFork  implements OwChoiceNode{
	@XStreamAsAttribute
	private String name;
	
	@XStreamImplicit(itemFieldName = "path")
	private List<OwForkTransition> paths;
	public OwFork(FlowNode flowNode) {
		name = flowNode.getText();
		paths = new ArrayList<OwForkTransition>();
		for(String to:flowNode.getTo()){
			OwForkTransition tran = new OwForkTransition();
			tran.setStart(to);
			paths.add(tran);
		}
	}
	public OwFork() {
		super();
	}
	public FlowNode toFlowNode() {
		FlowNode fn =new FlowNode();
		fn.setText(name);
		fn.setType("5");
		List<String> tos=new ArrayList<String>();
		for(OwForkTransition path:paths){
			tos.add(path.getStart());
		}
		fn.setTo(tos);
		return fn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<OwForkTransition> getPaths() {
		return paths;
	}
	public void setPaths(List<OwForkTransition> paths) {
		this.paths = paths;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((paths == null) ? 0 : paths.hashCode());
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

		OwFork other = (OwFork) obj;
		if (name == null) {
			if (other.name != null){
				return false;
			}
		} else if (!name.equals(other.name)){
			return false;
		}
		if (paths == null) {
			if (other.paths != null){
				return false;
			}
		} else if (!paths.equals(other.paths)){
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "OwFork [name=" + name + ", paths=" + paths + "]";
	}
	public static List<OwFork> parseXml(List<Element> fElms) {
		List<OwFork> oforks = new ArrayList<OwFork>();
		for(Element ele:fElms){
			oforks.add(OwFork.parseXml(ele));
		}
		return oforks;
	}
	private static OwFork parseXml(Element ele) {
		OwFork ofork =new OwFork();
		ofork.name = ele.attributeValue("name");
		ofork.paths = new ArrayList<OwForkTransition>();
		List<Element> peles= ele.elements("path");
		for(Element pele:peles){
			OwForkTransition ftr =OwForkTransition.parseXml(pele);
			ofork.paths.add(ftr);
		}
		return ofork;
	}
	public String valid() {
		if(this.paths==null || this.paths.isEmpty()){
			return "并行节点后续路径不可为空";
		}
		if(this.paths.size()<2){
			return "并行节点后续路径至少为两条";
		}
		return null;
	}

}
