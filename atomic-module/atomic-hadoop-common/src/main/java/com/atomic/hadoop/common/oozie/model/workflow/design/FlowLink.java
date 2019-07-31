package com.atomic.hadoop.common.oozie.model.workflow.design;

public class FlowLink {
	public FlowLink(){
		
	}
	private int node1id;
	private int node2id;

	@Override
	public String toString() {
		return "FlowLink [node1id=" + node1id + ", node2id=" + node2id + "]";
	}

	public int getNode1id() {
		return node1id;
	}

	public void setNode1id(int node1id) {
		this.node1id = node1id;
	}

	public int getNode2id() {
		return node2id;
	}

	public void setNode2id(int node2id) {
		this.node2id = node2id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + node1id;
		result = prime * result + node2id;
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
		FlowLink other = (FlowLink) obj;
		if (node1id != other.node1id)
			return false;
		if (node2id != other.node2id)
			return false;
		return true;
	}

}
