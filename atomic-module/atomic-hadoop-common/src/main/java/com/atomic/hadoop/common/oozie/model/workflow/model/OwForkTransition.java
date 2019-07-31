package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.dom4j.Element;

public class OwForkTransition {
	@XStreamAsAttribute
	private String start;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		OwForkTransition other = (OwForkTransition) obj;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OwForkTransition [start=" + start + "]";
	}

	public static OwForkTransition parseXml(Element pele) {
		OwForkTransition oft =new OwForkTransition();
		oft.start = pele.attributeValue("start");
		return oft;
	}
}
