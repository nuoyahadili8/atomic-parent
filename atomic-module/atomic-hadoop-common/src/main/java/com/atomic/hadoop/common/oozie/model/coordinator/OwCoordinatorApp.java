package com.atomic.hadoop.common.oozie.model.coordinator;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("coordinator-app")
public class OwCoordinatorApp {
	
	public OwCoordinatorApp(){
		
	}
	
	public OwCoordinatorApp(String coordinatorName, String cronExpression, String startTime, String endTime, String timeZone) {
		this.name = coordinatorName;
		this.frequency = cronExpression;
		this.start = startTime;
		this.end = endTime;
		this.timezone = timeZone;
	}

	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private String frequency;
	
	@XStreamAsAttribute
	private String start;
	
	@XStreamAsAttribute
	private String end;
	
	@XStreamAsAttribute
	private String timezone="GMT+08:00";
	
	@XStreamAsAttribute
	private String xmlns = "uri:oozie:coordinator:0.2";
	
	private OwCoordinatorParameters parameters;
	
	private OwCoordinatorControls controls;
	
	private OwCoordinatorDatasets datasets;
	
	@XStreamAlias("input-events")
	private OwCoordinatorInputEvents input_events;
	
	@XStreamAlias("output-events")
	private OwCoordinatorOutputEvents output_events;
	
	@XStreamImplicit(itemFieldName = "action")
	private List<OwCoordinatorAction> actions = new ArrayList<OwCoordinatorAction>();
	
	public String toXml() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		return xstream.toXML(this);
	}
	
	public void parseXml(String coordinatorXml){
		SAXReader reader = new SAXReader();  
		Document document;
		try {
			document = reader.read(new StringReader(coordinatorXml));
			Element rootElm = document.getRootElement();
			this.name=rootElm.attributeValue("name");
			this.frequency=rootElm.attributeValue("frequency");
			this.start=rootElm.attributeValue("start");
			this.end=rootElm.attributeValue("end");
			this.timezone=rootElm.attributeValue("timezone");
			this.xmlns=rootElm.attributeValue("xmlns");
			this.controls=OwCoordinatorControls.parseXml(rootElm.element("controls"));
			this.datasets=OwCoordinatorDatasets.parseXml(rootElm.element("datasets"));
			this.input_events=OwCoordinatorInputEvents.parseXml(rootElm.element("input-events"));
			this.output_events=OwCoordinatorOutputEvents.parseXml(rootElm.element("output-events"));
			List<Element> workflowModels=rootElm.element("action").elements("workflow");
			for(Element workflowModel:workflowModels){
				OwCoordinatorAction action=OwCoordinatorAction.parseXml(workflowModel);
				this.actions.add(action);
			}
		}catch (DocumentException e) {
			e.printStackTrace();
		}  
	}
	
	public static void main(String[] args) throws IOException {
		OwCoordinatorApp app=new OwCoordinatorApp();
		String xml=FileUtils.readFileToString(new File("D:/Documents/oozieweb/upload/zip/7/BOSS15_DCHNGROUPINFO/coordinator.xml"));
		app.parseXml(xml);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public List<OwCoordinatorAction> getActions() {
		return actions;
	}

	public void setActions(List<OwCoordinatorAction> actions) {
		this.actions = actions;
	}

	public OwCoordinatorControls getControls() {
		return controls;
	}

	public void setControls(OwCoordinatorControls controls) {
		this.controls = controls;
	}

	public OwCoordinatorDatasets getDatasets() {
		return datasets;
	}

	public void setDatasets(OwCoordinatorDatasets datasets) {
		this.datasets = datasets;
	}

	public OwCoordinatorInputEvents getInput_events() {
		return input_events;
	}

	public void setInput_events(OwCoordinatorInputEvents input_events) {
		this.input_events = input_events;
	}

	public OwCoordinatorOutputEvents getOutput_events() {
		return output_events;
	}

	public void setOutput_events(OwCoordinatorOutputEvents output_events) {
		this.output_events = output_events;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
		result = prime * result
				+ ((controls == null) ? 0 : controls.hashCode());
		result = prime * result
				+ ((datasets == null) ? 0 : datasets.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result
				+ ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result
				+ ((input_events == null) ? 0 : input_events.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((output_events == null) ? 0 : output_events.hashCode());
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result
				+ ((timezone == null) ? 0 : timezone.hashCode());
		result = prime * result + ((xmlns == null) ? 0 : xmlns.hashCode());
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

		OwCoordinatorApp other = (OwCoordinatorApp) obj;
		if (actions == null) {
			if (other.actions != null){
				return false;
			}
		} else if (!actions.equals(other.actions)){
			return false;
		}

		if (controls == null) {
			if (other.controls != null){
				return false;
			}
		} else if (!controls.equals(other.controls)){
			return false;
		}
		if (datasets == null) {
			if (other.datasets != null){
				return false;
			}
		} else if (!datasets.equals(other.datasets)){
			return false;
		}

		if (end == null) {
			if (other.end != null){
				return false;
			}
		} else if (!end.equals(other.end)){
			return false;
		}
		if (frequency == null) {
			if (other.frequency != null){
				return false;
			}
		} else if (!frequency.equals(other.frequency)){
			return false;
		}

		if (input_events == null) {
			if (other.input_events != null){
				return false;
			}
		} else if (!input_events.equals(other.input_events)){
			return false;
		}

		if (name == null) {
			if (other.name != null){
				return false;
			}
		} else if (!name.equals(other.name)){
			return false;
		}
		if (output_events == null) {
			if (other.output_events != null){
				return false;
			}
		} else if (!output_events.equals(other.output_events)){
			return false;
		}
		if (parameters == null) {
			if (other.parameters != null){
				return false;
			}
		} else if (!parameters.equals(other.parameters)){
			return false;
		}
		if (start == null) {
			if (other.start != null){
				return false;
			}
		} else if (!start.equals(other.start)){
			return false;
		}

		if (timezone == null) {
			if (other.timezone != null){
				return false;
			}
		} else if (!timezone.equals(other.timezone)){
			return false;
		}
		if (xmlns == null) {
			if (other.xmlns != null){
				return false;
			}
		} else if (!xmlns.equals(other.xmlns)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OwCoordinatorApp [name=" + name + ", frequency=" + frequency
				+ ", start=" + start + ", end=" + end + ", timezone="
				+ timezone + ", xmlns=" + xmlns + ", parameters=" + parameters
				+ ", controls=" + controls + ", datasets=" + datasets
				+ ", input_events=" + input_events + ", output_events="
				+ output_events + ", actions=" + actions + "]";
	}

	public OwCoordinatorParameters getParameters() {
		return parameters;
	}

	public void setParameters(OwCoordinatorParameters parameters) {
		this.parameters = parameters;
	}

	
}
