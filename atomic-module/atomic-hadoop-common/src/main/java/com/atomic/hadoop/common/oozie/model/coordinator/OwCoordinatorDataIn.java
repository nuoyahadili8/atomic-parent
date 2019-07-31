package com.atomic.hadoop.common.oozie.model.coordinator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("data-in")
public class OwCoordinatorDataIn {
	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private String dataset;
	
	private String instance;
	
	@XStreamAlias("start-instance")
	private String start_instance;
	
	@XStreamAlias("end-instance")
	private String end_instance;

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

	public String getStart_instance() {
		return start_instance;
	}

	public void setStart_instance(String start_instance) {
		this.start_instance = start_instance;
	}

	public String getEnd_instance() {
		return end_instance;
	}

	public void setEnd_instance(String end_instance) {
		this.end_instance = end_instance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataset == null) ? 0 : dataset.hashCode());
		result = prime * result
				+ ((end_instance == null) ? 0 : end_instance.hashCode());
		result = prime * result
				+ ((instance == null) ? 0 : instance.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((start_instance == null) ? 0 : start_instance.hashCode());
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
		OwCoordinatorDataIn other = (OwCoordinatorDataIn) obj;
		if (dataset == null) {
			if (other.dataset != null){
				return false;
			}
		} else if (!dataset.equals(other.dataset)){
			return false;
		}
		if (end_instance == null) {
			if (other.end_instance != null){
				return false;
			}
		} else if (!end_instance.equals(other.end_instance)){
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
		if (start_instance == null) {
			if (other.start_instance != null){
				return false;
			}
		} else if (!start_instance.equals(other.start_instance)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OwCoordinatorDataIn [name=" + name + ", dataset=" + dataset
				+ ", instance=" + instance + ", start_instance="
				+ start_instance + ", end_instance=" + end_instance + "]";
	}
	
	
}
