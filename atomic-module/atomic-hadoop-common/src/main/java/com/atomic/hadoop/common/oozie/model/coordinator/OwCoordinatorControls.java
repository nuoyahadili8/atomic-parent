package com.atomic.hadoop.common.oozie.model.coordinator;

import org.dom4j.Element;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("controls")
public class OwCoordinatorControls {
	
	private String timeout;
	private String concurrency;
	private String execution;
	private String throttle;
	
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public String getConcurrency() {
		return concurrency;
	}
	public void setConcurrency(String concurrency) {
		this.concurrency = concurrency;
	}
	public String getExecution() {
		return execution;
	}
	public void setExecution(String execution) {
		this.execution = execution;
	}
	public String getThrottle() {
		return throttle;
	}
	public void setThrottle(String throttle) {
		this.throttle = throttle;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((concurrency == null) ? 0 : concurrency.hashCode());
		result = prime * result
				+ ((execution == null) ? 0 : execution.hashCode());
		result = prime * result
				+ ((throttle == null) ? 0 : throttle.hashCode());
		result = prime * result + ((timeout == null) ? 0 : timeout.hashCode());
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
		OwCoordinatorControls other = (OwCoordinatorControls) obj;
		if (concurrency == null) {
			if (other.concurrency != null){
				return false;
			}

		} else if (!concurrency.equals(other.concurrency)){
			return false;
		}

		if (execution == null) {
			if (other.execution != null){
				return false;
			}

		} else if (!execution.equals(other.execution)){
			return false;
		}
		if (throttle == null) {
			if (other.throttle != null){
				return false;
			}
		} else if (!throttle.equals(other.throttle)){
			return false;
		}
		if (timeout == null) {
			if (other.timeout != null){
				return false;
			}
		} else if (!timeout.equals(other.timeout)){
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "OwCoordinatorControls [timeout=" + timeout + ", concurrency="
				+ concurrency + ", execution=" + execution + ", throttle="
				+ throttle + "]";
	}
	
	public static OwCoordinatorControls parseXml(Element element) {
		OwCoordinatorControls controls=new OwCoordinatorControls();
		controls.timeout=element.elementText("timeout");
		controls.concurrency=element.elementText("concurrency");
		controls.execution=element.elementText("execution");
		controls.throttle=element.elementText("throttle");
		return controls;
	}
}
