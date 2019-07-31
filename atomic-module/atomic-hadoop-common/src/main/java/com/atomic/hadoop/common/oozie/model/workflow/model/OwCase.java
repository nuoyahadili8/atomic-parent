package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.dom4j.Element;

@XStreamAlias("case")
@XStreamConverter(OwCaseConverter.class)
public class OwCase {
	private String CASE;
	
	@XStreamAsAttribute
	private String to;
	public String getCASE() {
		return CASE;
	}
	public void setCASE(String cASE) {
		CASE = cASE;
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
		result = prime * result + ((CASE == null) ? 0 : CASE.hashCode());
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
		OwCase other = (OwCase) obj;
		if (CASE == null) {
			if (other.CASE != null)
				return false;
		} else if (!CASE.equals(other.CASE))
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
		return "OwCase [CASE=" + CASE + ", to=" + to + "]";
	}
	public static OwCase parseXml(Element casee) {
		OwCase oCase =new OwCase();
		oCase.setCASE(casee.getTextTrim());
		oCase.setTo(casee.attributeValue("to"));
		return oCase;
	}
}
