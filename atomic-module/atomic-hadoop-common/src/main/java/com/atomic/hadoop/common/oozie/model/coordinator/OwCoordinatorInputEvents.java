package com.atomic.hadoop.common.oozie.model.coordinator;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("input-events")
public class OwCoordinatorInputEvents {
	
	@XStreamImplicit(itemFieldName = "data-in")
	private List<OwCoordinatorDataIn> data_in;

	public List<OwCoordinatorDataIn> getData_in() {
		return data_in;
	}

	public void setData_in(List<OwCoordinatorDataIn> data_in) {
		this.data_in = data_in;
	}
	
	public static OwCoordinatorInputEvents parseXml(Element element){
		if(element!=null){
			OwCoordinatorInputEvents model =new OwCoordinatorInputEvents();
			List<Element> datain_elements=element.elements();
			List<OwCoordinatorDataIn> data_inModel=new ArrayList<OwCoordinatorDataIn>();
			for(Element datain_element:datain_elements){
				OwCoordinatorDataIn dataInModel=new OwCoordinatorDataIn();
				dataInModel.setName(datain_element.attributeValue("name"));
				dataInModel.setDataset(datain_element.attributeValue("dataset"));
				dataInModel.setInstance(datain_element.element("instance").getText());
				data_inModel.add(dataInModel);
			}
			model.setData_in(data_inModel);
			return model;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data_in == null) ? 0 : data_in.hashCode());
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
		OwCoordinatorInputEvents other = (OwCoordinatorInputEvents) obj;
		if (data_in == null) {
			if (other.data_in != null){
				return false;
			}
		} else if (!data_in.equals(other.data_in)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OwCoordinatorInputEvents [data_in=" + data_in + "]";
	}
	
}
