package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class OwMove{
	@XStreamAsAttribute
	public String source;
	@XStreamAsAttribute
	public String target;
	
	public OwMove(String source,String target) {
		this.source = source;
		this.target = target;
	}

	public OwMove(String mvArg) {
		if(mvArg!=null){
			String[] mvas = mvArg.split(":");
			this.source = mvas[0];
			this.target = mvas[1];
		}
	}
	public static List<OwMove> parseXml(List<Element> mes) {
		List<OwMove> move =new ArrayList<OwMove>();
		for(Element me:mes){
			OwMove mk = new OwMove(me.attributeValue("source"),me.attributeValue("target"));
			move.add(mk);
		}
		return move;
	}
	
}
