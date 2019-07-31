package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class OwMkdir extends OwAbstractPrepare{

	public OwMkdir(String path) {
		super(path);
	}

	@Override
	public String toString() {
		return "OwMkdir [path=" + path + "]";
	}

	public static List<OwMkdir> parseXml(List<Element> mes) {
		List<OwMkdir> mkdirs =new ArrayList<OwMkdir>();
		for(Element me:mes){
			OwMkdir mk = new OwMkdir(me.attributeValue("path"));
			mkdirs.add(mk);
		}
		return mkdirs;
	}
	
}
