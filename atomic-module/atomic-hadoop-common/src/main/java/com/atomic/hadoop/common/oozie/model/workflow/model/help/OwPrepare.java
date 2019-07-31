package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.List;

public class OwPrepare {
	@XStreamImplicit(itemFieldName = "delete")
	private List<OwDelete> deletes;
	
	@XStreamImplicit(itemFieldName = "mkdir")
	private List<OwMkdir> mkdirs;


	public static OwPrepare parseXml(Element element) {
		OwPrepare pre =new OwPrepare();
		List<Element> des = element.elements("delete");
		if(des!=null && !des.isEmpty()){
			pre.deletes = OwDelete.parseXml(des);
		}
		List<Element> mes = element.elements("mkdir");
		if(mes!=null && !mes.isEmpty()){
			pre.mkdirs = OwMkdir.parseXml(mes);
		}
		
		return pre;
	}


	public List<OwDelete> getDeletes() {
		return deletes;
	}


	public void setDeletes(List<OwDelete> deletes) {
		this.deletes = deletes;
	}


	public List<OwMkdir> getMkdirs() {
		return mkdirs;
	}


	public void setMkdirs(List<OwMkdir> mkdirs) {
		this.mkdirs = mkdirs;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deletes == null) ? 0 : deletes.hashCode());
		result = prime * result + ((mkdirs == null) ? 0 : mkdirs.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OwPrepare other = (OwPrepare) obj;
		if (deletes == null) {
			if (other.deletes != null)
				return false;
		} else if (!deletes.equals(other.deletes))
			return false;
		if (mkdirs == null) {
			if (other.mkdirs != null)
				return false;
		} else if (!mkdirs.equals(other.mkdirs))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "OwPrepare [deletes=" + deletes + ", mkdirs=" + mkdirs + "]";
	}
}
