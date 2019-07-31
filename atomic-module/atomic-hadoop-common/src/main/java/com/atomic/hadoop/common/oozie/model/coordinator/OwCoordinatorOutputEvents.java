package com.atomic.hadoop.common.oozie.model.coordinator;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("output-events")
public class OwCoordinatorOutputEvents {

	@XStreamImplicit(itemFieldName = "data-out")
	private List<OwCoordinatorDataOut> data_out;

	public List<OwCoordinatorDataOut> getData_out() {
		return data_out;
	}

	public void setData_out(List<OwCoordinatorDataOut> data_out) {
		this.data_out = data_out;
	}
	
	public static OwCoordinatorOutputEvents parseXml(Element element){
		OwCoordinatorOutputEvents model=new OwCoordinatorOutputEvents();
		List<Element> dataout_elements=element.elements();
		List<OwCoordinatorDataOut> data_out_List=new ArrayList<OwCoordinatorDataOut>();
		for(Element dataout_element : dataout_elements){
			OwCoordinatorDataOut output=new OwCoordinatorDataOut();
			output.setName(dataout_element.attributeValue("name"));
			output.setDataset(dataout_element.attributeValue("dataset"));
			output.setInstance(dataout_element.element("instance").getText());
			data_out_List.add(output);
		}
		model.setData_out(data_out_List);
		return model;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((data_out == null) ? 0 : data_out.hashCode());
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
		OwCoordinatorOutputEvents other = (OwCoordinatorOutputEvents) obj;
		if (data_out == null) {
			if (other.data_out != null){
				return false;
			}
		} else if (!data_out.equals(other.data_out)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OwCoordinatorOutputEvents [data_out=" + data_out + "]";
	}

}
