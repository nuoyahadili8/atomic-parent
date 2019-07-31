package com.atomic.hadoop.common.oozie.model.coordinator;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("action")
public class OwCoordinatorAction {
	
	public OwCoordinatorAction(){
		
	}
	
	private OwCoordinatorFlow workflow;
	
	public OwCoordinatorFlow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(OwCoordinatorFlow workflow) {
		this.workflow = workflow;
	}

	@Override
	public String toString() {
		return "OwAction [workflow=" + workflow + "]";
	}
	
	public static OwCoordinatorAction parseXml(Element element){
		OwCoordinatorAction action=new OwCoordinatorAction();
		OwCoordinatorFlow workflow=new OwCoordinatorFlow();
		workflow.setApp_path(element.elementText("app-path"));
		Element configurationsModel=element.element("configuration");
		List<Element> properties=configurationsModel.elements("property");
		OwCoordinatorConfiguration configuration=new OwCoordinatorConfiguration();
		List<OwCoordinatorProperty> propertys = new ArrayList<OwCoordinatorProperty>();
		for(Element propertyModel:properties){
			OwCoordinatorProperty property=new OwCoordinatorProperty();
			property.setName(propertyModel.elementText("name"));
			property.setValue(propertyModel.elementText("value"));
			propertys.add(property);
		}
		configuration.setPropertys(propertys);
		workflow.setConfiguration(configuration);
		action.setWorkflow(workflow);
		return action;
	}

}
