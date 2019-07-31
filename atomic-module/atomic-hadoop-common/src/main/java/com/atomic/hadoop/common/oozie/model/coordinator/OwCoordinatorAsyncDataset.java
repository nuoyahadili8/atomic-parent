package com.atomic.hadoop.common.oozie.model.coordinator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("dataset")
public class OwCoordinatorAsyncDataset {
	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	@XStreamAlias("sequence-type")
	private String sequence_type;
	
	@XStreamAsAttribute
	@XStreamAlias("initial-version")
	private String initial_version;
	
	@XStreamAlias("uri-template")
	private String uri_template;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSequence_type() {
		return sequence_type;
	}

	public void setSequence_type(String sequence_type) {
		this.sequence_type = sequence_type;
	}

	public String getInitial_version() {
		return initial_version;
	}

	public void setInitial_version(String initial_version) {
		this.initial_version = initial_version;
	}

	public String getUri_template() {
		return uri_template;
	}

	public void setUri_template(String uri_template) {
		this.uri_template = uri_template;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((initial_version == null) ? 0 : initial_version.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((sequence_type == null) ? 0 : sequence_type.hashCode());
		result = prime * result
				+ ((uri_template == null) ? 0 : uri_template.hashCode());
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
		OwCoordinatorAsyncDataset other = (OwCoordinatorAsyncDataset) obj;
		if (initial_version == null) {
			if (other.initial_version != null)
				return false;
		} else if (!initial_version.equals(other.initial_version))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sequence_type == null) {
			if (other.sequence_type != null)
				return false;
		} else if (!sequence_type.equals(other.sequence_type))
			return false;
		if (uri_template == null) {
			if (other.uri_template != null)
				return false;
		} else if (!uri_template.equals(other.uri_template))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OwCoordinatorAsyncDataset [name=" + name + ", sequence_type="
				+ sequence_type + ", initial_version=" + initial_version
				+ ", uri_template=" + uri_template + "]";
	}


}
