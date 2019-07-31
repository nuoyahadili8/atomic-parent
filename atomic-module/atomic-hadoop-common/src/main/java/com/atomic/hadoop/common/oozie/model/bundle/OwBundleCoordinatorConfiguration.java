package com.atomic.hadoop.common.oozie.model.bundle;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class OwBundleCoordinatorConfiguration {

	@XStreamImplicit(itemFieldName = "property")
	private List<OwBundleProperty> propertys = new ArrayList<OwBundleProperty>();

	public List<OwBundleProperty> getPropertys() {
		return propertys;
	}

	public void setPropertys(List<OwBundleProperty> propertys) {
		this.propertys = propertys;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((propertys == null) ? 0 : propertys.hashCode());
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
		OwBundleCoordinatorConfiguration other = (OwBundleCoordinatorConfiguration) obj;
		if (propertys == null) {
			if (other.propertys != null){
				return false;
			}
		} else if (!propertys.equals(other.propertys)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OwBundleCoordinatorConfiguration [propertys=" + propertys + "]";
	}

	public static OwBundleCoordinatorConfiguration parseXml(Element element) {
		OwBundleCoordinatorConfiguration oc =new OwBundleCoordinatorConfiguration();
		List<Element> propEs = element.elements("property");
		oc.propertys=OwBundleProperty.parseXml(propEs);
		return oc;
	}
}
