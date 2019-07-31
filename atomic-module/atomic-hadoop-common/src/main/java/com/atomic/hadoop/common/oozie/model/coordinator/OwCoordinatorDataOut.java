package com.atomic.hadoop.common.oozie.model.coordinator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("data-out")
public class OwCoordinatorDataOut {
	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private String dataset;
	
	private String instance;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataset == null) ? 0 : dataset.hashCode());
		result = prime * result
				+ ((instance == null) ? 0 : instance.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());

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
		OwCoordinatorDataOut other = (OwCoordinatorDataOut) obj;
		if (dataset == null) {
			if (other.dataset != null){
				return false;
			}
		} else if (!dataset.equals(other.dataset)){
			return false;
		}
		if (instance == null) {
			if (other.instance != null){
				return false;
			}
		} else if (!instance.equals(other.instance)){
			return false;
		}
		if (name == null) {
			if (other.name != null){
				return false;
			}
		} else if (!name.equals(other.name)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OwCoordinatorDataOut [name=" + name + ", dataset=" + dataset
				+ ", instance=" + instance + "]";
	}
	
	
}
