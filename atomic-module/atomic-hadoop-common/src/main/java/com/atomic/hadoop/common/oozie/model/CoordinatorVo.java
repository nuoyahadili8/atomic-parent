package com.atomic.hadoop.common.oozie.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CoordinatorVo {
	private String id;
	private String platformId;
	private String projectId;
	private String nameEn;
	private String nameCn;
	private String subSystemNameEn;
	private String cycle;
	private String frequency;
	private String offset;
	private String startTime;
	private String endTime;
	private String xmlns;
	private String state;
	private String submitFlag;
	private String status;
	private String appPath;
	private String params;
	private String queue;
	private String controlTimeout;
	private String controlConcurrency;
	private String controlExecution;
	private String controlThrottle;
	private String taskId;
	private String ownerGroup;
	private String createUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String updateUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	private String stateUpdateUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date stateUpdateTime;
	private String statusUpdateUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date statusUpdateTime;

	private List<String> projectIdList = new ArrayList<String>();
	private String userType;
	private String projectName;
	private String hadoopUsername;

}
