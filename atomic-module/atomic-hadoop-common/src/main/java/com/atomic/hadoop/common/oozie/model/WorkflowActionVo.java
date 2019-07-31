package com.atomic.hadoop.common.oozie.model;

import java.util.Date;

import lombok.Data;
import org.apache.oozie.client.WorkflowAction;
import org.apache.oozie.client.WorkflowAction.Status;

import com.alibaba.fastjson.annotation.JSONField;

@Data
public class WorkflowActionVo {
	private String conf;
	private String consoleUrl;
	//private String cred;
	private String data;
	@JSONField (format="yyyy-MM-dd HH:mm:ss")  
	private Date endTime;
	private String errorCode;
	private String errorMessage;
	private String externalChildIDs;
	private String externalId;
	private String externalStatus;
	private String id;
	private String name;
	@JSONField (format="yyyy-MM-dd HH:mm:ss")  
	private Date startTime;
	private int retries;
	private String stats;
	private Status status;
	private String trackerUri;
	private String transition;
	private String type;
/*	private int userRetryCount;
	private int userRetryInterval;
	private int retryMax;*/

	public WorkflowActionVo(WorkflowAction action){
		this.conf=action.getConf();
		this.consoleUrl=action.getConsoleUrl();
		//this.cred=action.getCred();
		this.data=action.getData();
		this.endTime=action.getEndTime();
		this.errorCode=action.getErrorCode();
		this.errorMessage=action.getErrorMessage();
		this.externalChildIDs=action.getExternalChildIDs();
		this.externalId=action.getExternalId();
		this.externalStatus=action.getExternalStatus();
		this.id=action.getId();
		this.name=action.getName();
		this.startTime=action.getStartTime();
		this.retries=action.getRetries();
		this.stats=action.getStats();
		this.status=action.getStatus();
		this.trackerUri=action.getTrackerUri();
		this.transition=action.getTransition();
		this.type=action.getType();
	//	this.userRetryCount=action.getUserRetryCount();
	//	this.userRetryInterval=action.getUserRetryInterval();
	//	this.retryMax=action.getUserRetryMax();		
	}


}
