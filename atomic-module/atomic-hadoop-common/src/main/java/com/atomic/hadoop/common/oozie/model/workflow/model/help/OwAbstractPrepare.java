package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class OwAbstractPrepare {
	@XStreamAsAttribute
	public String path;

	public OwAbstractPrepare(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		OwAbstractPrepare other = (OwAbstractPrepare) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OwAbstractPrepare [path=" + path + "]";
	}
}
