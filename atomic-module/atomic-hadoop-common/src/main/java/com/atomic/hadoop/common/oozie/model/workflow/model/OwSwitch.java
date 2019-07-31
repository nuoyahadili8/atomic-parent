package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("switch")
public class OwSwitch {
	
	@XStreamImplicit(itemFieldName = "case")
	private List<OwCase> cases;
	
	@XStreamAlias("default")
	private OwDefault DEFAULT;
	public List<OwCase> getCases() {
		return cases;
	}
	public void setCases(List<OwCase> cases) {
		this.cases = cases;
	}
	public OwDefault getDEFAULT() {
		return DEFAULT;
	}
	public void setDEFAULT(OwDefault dEFAULT) {
		DEFAULT = dEFAULT;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DEFAULT == null) ? 0 : DEFAULT.hashCode());
		result = prime * result + ((cases == null) ? 0 : cases.hashCode());
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
		OwSwitch other = (OwSwitch) obj;
		if (DEFAULT == null) {
			if (other.DEFAULT != null)
				return false;
		} else if (!DEFAULT.equals(other.DEFAULT))
			return false;
		if (cases == null) {
			if (other.cases != null)
				return false;
		} else if (!cases.equals(other.cases))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "OwSwitch [cases=" + cases + ", DEFAULT=" + DEFAULT + "]";
	}
	public String valid() {
		if (DEFAULT == null)
			return "条件分支节点必须存在默认的后续节点！";
		if(cases==null ||cases.isEmpty()){
			return "条件分支节点必须存在至少两个后续节点！";
		}
		return null;
	}
	
	public static OwSwitch parseXml(Element eSwitch) {
		OwSwitch oSwitch = new OwSwitch();
		OwDefault oDefault = OwDefault.parseXml(eSwitch.element("default"));
		oDefault.setTo(eSwitch.elementTextTrim("default"));
		oSwitch.setDEFAULT(oDefault);
		
		List<OwCase> oCases=new ArrayList<OwCase>();
		
		List<Element> casees= eSwitch.elements("case");
		for(Element casee:casees){
			OwCase oCase = OwCase.parseXml(casee);
			oCases.add(oCase);
		}
		oSwitch.setCases(oCases);
		
		return oSwitch;
	}
}
