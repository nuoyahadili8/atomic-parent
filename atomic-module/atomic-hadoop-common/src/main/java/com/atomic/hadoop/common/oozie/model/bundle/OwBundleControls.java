package com.atomic.hadoop.common.oozie.model.bundle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("controls")
public class OwBundleControls {

	@XStreamAlias("kick-off-time")
	private String kick_off_time;

	public String getKick_off_time() {
		return kick_off_time;
	}

	public void setKick_off_time(String kick_off_time) {
		this.kick_off_time = kick_off_time;
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

		OwBundleControls other = (OwBundleControls) obj;
		if (kick_off_time == null) {
			if (other.kick_off_time != null){
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kick_off_time == null) ? 0 : kick_off_time.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "OwBundleControls [kick_off_time=" + kick_off_time + "]";
	}
}
