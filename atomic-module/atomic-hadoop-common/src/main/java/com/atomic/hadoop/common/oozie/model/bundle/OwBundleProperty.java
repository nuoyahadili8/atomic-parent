package com.atomic.hadoop.common.oozie.model.bundle;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;


public class OwBundleProperty {

	private String name;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		OwBundleProperty other = (OwBundleProperty) obj;
		if (name == null) {
			if (other.name != null){
				return false;
			}
		} else if (!name.equals(other.name)){
			return false;
		}
		if (value == null) {
			if (other.value != null){
				return false;
			}
		} else if (!value.equals(other.value)){
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "OwProperty [name=" + name + ", value=" + value + "]";
	}
	public static List<OwBundleProperty> parseXml(List<Element> propEs) {
		List<OwBundleProperty>  props =new ArrayList<OwBundleProperty> ();
		for(Element e:propEs){
			OwBundleProperty op = new OwBundleProperty();
			op.name = e.elementText("name");
			op.value = e.elementText("value");
			props.add(op);
		}
		return props;
	}
}
