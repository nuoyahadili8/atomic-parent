package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class OwTouchz extends OwAbstractPrepare{

	public OwTouchz(String path) {
		super(path);
	}

	@Override
	public String toString() {
		return "OwMkdir [path=" + path + "]";
	}

	public static List<OwTouchz> parseXml(List<Element> mes) {
		List<OwTouchz> mkdirs =new ArrayList<OwTouchz>();
		for(Element me:mes){
			OwTouchz mk = new OwTouchz(me.attributeValue("path"));
			mkdirs.add(mk);
		}
		return mkdirs;
	}
	
}
