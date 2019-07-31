package com.atomic.hadoop.common.oozie.model.coordinator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("dataset")
public class OwCoordinatorDataset {
	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private String frequency;
	
	@XStreamAsAttribute
	@XStreamAlias("initial-instance")
	private String initial_instance;
	
	@XStreamAsAttribute
	private String timezone="GMT+08:00";
	
	@XStreamAlias("uri-template")
	private String uri_template;
	
	@XStreamAlias("done-flag")
	private String done_flag;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getInitial_instance() {
		return initial_instance;
	}

	public void setInitial_instance(String initial_instance) {
		this.initial_instance = initial_instance;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getUri_template() {
		return uri_template;
	}

	public void setUri_template(String uri_template) {
		this.uri_template = uri_template;
	}

	public String getDone_flag() {
		return done_flag;
	}

	public void setDone_flag(String done_flag) {
		this.done_flag = done_flag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((done_flag == null) ? 0 : done_flag.hashCode());
		result = prime * result
				+ ((frequency == null) ? 0 : frequency.hashCode());
		result = prime
				* result
				+ ((initial_instance == null) ? 0 : initial_instance.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((timezone == null) ? 0 : timezone.hashCode());
		result = prime * result
				+ ((uri_template == null) ? 0 : uri_template.hashCode());
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
		OwCoordinatorDataset other = (OwCoordinatorDataset) obj;
		if (done_flag == null) {
			if (other.done_flag != null){
				return false;
			}
		} else if (!done_flag.equals(other.done_flag)){
			return false;
		}
		if (frequency == null) {
			if (other.frequency != null){
				return false;
			}
		} else if (!frequency.equals(other.frequency)){
			return false;
		}
		if (initial_instance == null) {
			if (other.initial_instance != null){
				return false;
			}
		} else if (!initial_instance.equals(other.initial_instance)){
			return false;
		}
		if (name == null) {
			if (other.name != null){
				return false;
			}
		} else if (!name.equals(other.name)){
			return false;
		}
		if (timezone == null) {
			if (other.timezone != null){
				return false;
			}
		} else if (!timezone.equals(other.timezone)){
			return false;
		}

		if (uri_template == null) {
			if (other.uri_template != null){
				return false;
			}
		} else if (!uri_template.equals(other.uri_template)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OwCoordinatorDataset [name=" + name + ", frequency="
				+ frequency + ", initial_instance=" + initial_instance
				+ ", timezone=" + timezone + ", uri_template=" + uri_template
				+ ", done_flag=" + done_flag + "]";
	}

}
