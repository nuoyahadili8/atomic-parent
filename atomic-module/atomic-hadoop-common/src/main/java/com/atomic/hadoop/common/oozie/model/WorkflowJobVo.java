package com.atomic.hadoop.common.oozie.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.atomic.hadoop.common.oozie.utils.TimeConsumeUtils;
import lombok.Data;
import org.apache.oozie.client.WorkflowAction;
import org.apache.oozie.client.WorkflowJob;
import org.apache.oozie.client.WorkflowJob.Status;
import com.alibaba.fastjson.annotation.JSONField;

@Data
public class WorkflowJobVo {
	private String id;
	private String group;
	private String appName;
	private String appPath;
	private Status status;
	@JSONField (format="yyyy-MM-dd HH:mm:ss")  
	private Date lastModifiedTime;
	@JSONField (format="yyyy-MM-dd HH:mm:ss")  
	private Date createdTime;
	@JSONField (format="yyyy-MM-dd HH:mm:ss")  
	private Date startTime;
	@JSONField (format="yyyy-MM-dd HH:mm:ss")  
	private Date endTime;
	private Long timeConsume;
	private String timeConsumeStr;
	private int run;
	private String consoleUrl;
	private String parentId;
	private String externalId;
	private String acl;
	private String user;
	private String conf;
	private List<WorkflowActionVo> actions;
	
	@SuppressWarnings("deprecation")
	public WorkflowJobVo(WorkflowJob job) {
		this.id = job.getId();
		this.group = job.getGroup();
		this.appName = job.getAppName();
		this.appPath = job.getAppPath();
		this.status = job.getStatus();
		this.createdTime = job.getCreatedTime();
		this.lastModifiedTime = job.getLastModifiedTime();
		this.startTime = job.getStartTime();
		this.endTime = job.getEndTime();
		if (job.getStartTime() == null || job.getEndTime() == null) {
			this.timeConsume = null;
		} else {
			this.timeConsume = (job.getEndTime().getTime() - job.getStartTime().getTime()) / 1000;
		}
		this.timeConsumeStr = TimeConsumeUtils.TimeConsume(timeConsume);
		this.run = job.getRun();
		this.consoleUrl = job.getConsoleUrl();
		this.parentId = job.getParentId();
		this.externalId = job.getExternalId();
		this.acl = job.getAcl();
		this.user = job.getUser();
		this.conf = job.getConf();
		if (job.getActions() != null) {
			this.actions = new ArrayList<WorkflowActionVo>();

			for (WorkflowAction action : job.getActions()) {
				WorkflowActionVo actionVo = new WorkflowActionVo(action);
				this.actions.add(actionVo);
			}
		}
	}

}
