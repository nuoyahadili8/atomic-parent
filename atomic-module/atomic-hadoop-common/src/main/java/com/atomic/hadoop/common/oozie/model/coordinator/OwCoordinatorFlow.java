package com.atomic.hadoop.common.oozie.model.coordinator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("workflow")
public class OwCoordinatorFlow {
	
	public OwCoordinatorFlow(){
		
	}
	
	@XStreamAlias("app-path")
	private String app_path;
	
	private OwCoordinatorConfiguration configuration;
	
	@Override
	public String toString() {
		return "OwAction [app_path=" + app_path + ", configuration=" + configuration + "]";
	}
	
	public String getApp_path() {
		return app_path;
	}

	public void setApp_path(String app_path) {
		this.app_path = app_path;
	}

	public OwCoordinatorConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(OwCoordinatorConfiguration configuration) {
		this.configuration = configuration;
	}


	
}
