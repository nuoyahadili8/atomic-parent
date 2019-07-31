package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class OwChmod extends OwAbstractPrepare{
	@XStreamAsAttribute
	public String permissions;
	
	@XStreamAlias("dir-files")
	@XStreamAsAttribute
	public String dir_files;
	
	public OwChmod(String path,String permissions,String dir_files) {
		super(path);
		this.permissions = permissions;
		this.dir_files = dir_files;
	}
	public OwChmod(String[] chs){
		super(chs[0]);
		this.permissions = chs[1];
		this.dir_files = chs[2];
	}

	public static List<OwChmod> parseXml(List<Element> mes) {
		List<OwChmod> chmod=new ArrayList<OwChmod>();
		for(Element me:mes){
			OwChmod mk = new OwChmod(me.attributeValue("path"),
					me.attributeValue("permissions"),me.attributeValue("dir-files"));
			chmod.add(mk);
		}
		return chmod;
	}
	
}
