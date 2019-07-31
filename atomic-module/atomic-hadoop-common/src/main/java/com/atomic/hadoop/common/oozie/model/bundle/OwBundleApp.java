package com.atomic.hadoop.common.oozie.model.bundle;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("bundle-app")
public class OwBundleApp {
	
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	private String xmlnsxsi = "http://www.w3.org/2001/XMLSchema-instance";
	@XStreamAsAttribute
	private String xmlns = "uri:oozie:bundle:0.2";
	
	@XStreamAlias("controls")
	private OwBundleControls owBundleControls;
	
	@XStreamImplicit(itemFieldName = "coordinator")
	private List<OwBundleCoordinator> owBundleCoordinators=new ArrayList<OwBundleCoordinator>();
	
	public OwBundleApp(){}
	
	public OwBundleApp(String name){
		this.name=name;
	}
	
	public String toXml() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		return xstream.toXML(this);
	}
	
	public static void main(String[] args){
		System.out.println(new OwBundleApp("bundle01").toXml().replace("xmlnsxsi", "xmlns:xsi"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public OwBundleControls getOwBundleControls() {
		return owBundleControls;
	}

	public void setOwBundleControls(OwBundleControls owBundleControls) {
		this.owBundleControls = owBundleControls;
	}

	public String getXmlnsxsi() {
		return xmlnsxsi;
	}

	public void setXmlnsxsi(String xmlnsxsi) {
		this.xmlnsxsi = xmlnsxsi;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	
	public List<OwBundleCoordinator> getOwBundleCoordinators() {
		return owBundleCoordinators;
	}

	public void setOwBundleCoordinators(
			List<OwBundleCoordinator> owBundleCoordinators) {
		this.owBundleCoordinators = owBundleCoordinators;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}


}
