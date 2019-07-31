package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.List;

public class OwParameters {
	@XStreamImplicit(itemFieldName = "property")
	private List<OwProperty> propertys;

	public static OwParameters parseXml(Element parasElm) {
		OwParameters owp = new OwParameters();
		List<Element> propEs = parasElm.elements("property");
		owp.propertys = OwProperty.parseXml(propEs);
		return owp;
	}

	public List<OwProperty> getPropertys() {
		return propertys;
	}

	public void setPropertys(List<OwProperty> propertys) {
		this.propertys = propertys;
	}
}
