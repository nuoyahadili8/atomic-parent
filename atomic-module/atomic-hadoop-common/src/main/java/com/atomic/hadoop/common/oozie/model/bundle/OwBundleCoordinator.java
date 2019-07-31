package com.atomic.hadoop.common.oozie.model.bundle;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class OwBundleCoordinator {

	@XStreamAsAttribute
	private String name;
	
	@XStreamAlias("app-path")
	private String app_path;
	
	@XStreamAlias("configuration")
	private OwBundleCoordinatorConfiguration owBundleCoordinatorConfiguration;
	
	
	public static List<OwBundleCoordinator> parseXml(List<Element> propEs) {
		List<OwBundleCoordinator>  props =new ArrayList<OwBundleCoordinator> ();
		for(Element e:propEs){
			OwBundleCoordinator op = new OwBundleCoordinator();
			op.name = e.elementText("name");
			props.add(op);
		}
		return props;
	}

	public OwBundleCoordinatorConfiguration getOwBundleCoordinatorConfiguration() {
		return owBundleCoordinatorConfiguration;
	}

	public void setOwBundleCoordinatorConfiguration(
			OwBundleCoordinatorConfiguration owBundleCoordinatorConfiguration) {
		this.owBundleCoordinatorConfiguration = owBundleCoordinatorConfiguration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApp_path() {
		return app_path;
	}

	public void setApp_path(String app_path) {
		this.app_path = app_path;
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
		OwBundleCoordinator other = (OwBundleCoordinator) obj;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "OwBundleCoordinator [name=" + name + "]";
	}
	
}
