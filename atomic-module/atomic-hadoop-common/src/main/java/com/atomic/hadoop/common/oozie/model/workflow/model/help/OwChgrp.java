package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class OwChgrp extends OwAbstractPrepare{
	@XStreamAsAttribute
	public String group;
	
	@XStreamAlias("dir-files")
	@XStreamAsAttribute
	public String dir_files;
	
	public OwChgrp(String path,String group,String dir_files) {
		super(path);
		this.group = group;
		this.dir_files = dir_files;
	}
	public OwChgrp(String[] chs){
		super(chs[0]);
		this.group = chs[1];
		this.dir_files = chs[2];
	}
	public static List<OwChgrp> parseXml(List<Element> mes) {
		List<OwChgrp> chgrp =new ArrayList<OwChgrp>();
		for(Element me:mes){
			OwChgrp mk = new OwChgrp(me.attributeValue("path"),
					me.attributeValue("group"),me.attributeValue("dir-files"));
			chgrp.add(mk);
		}
		return chgrp;
	}
	
}
