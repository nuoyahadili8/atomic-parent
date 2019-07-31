package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.List;

public class OwConfiguration {

	@XStreamImplicit(itemFieldName = "property")
	private List<OwProperty> propertys;

	public List<OwProperty> getPropertys() {
		return propertys;
	}

	public void setPropertys(List<OwProperty> propertys) {
		this.propertys = propertys;
	}

	public static OwConfiguration parseXml(Element element) {
		OwConfiguration oc = new OwConfiguration();
		List<Element> propEs = element.elements("property");
		oc.propertys = OwProperty.parseXml(propEs);
		return oc;
	}
}
