package com.atomic.hadoop.common.oozie.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.atomic.hadoop.common.oozie.utils.TimeConsumeUtils;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TaskVo {
	private String id;
	private String projectId;
	private String platformId;
	private String subsystemId;
	private String nameEn;
	private String nameCn;
	private String businessType;
	private String cycle;
	private String offset;
	private String dataKeepCycleDay;
	private String dataKeepCycleMonth;
	private String strategy;
	private String priority;
	private String state;
	private String queue;
	private String triggerFile;
	private String globalId;
	private String conHive2Id;
	private String conHcatalogId;
	private String conHbaseId;
	private String note;
	private String enableFlag;
	private String enableFlagUpdateUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date enableFlagUpdateTime;
	private String displayFlag;
	private String displayFlagUpdateUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date displayFlagUpdateTime;
	private String ownerGroup;
	private String createUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String updateUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	private String projectName;
	private List<String> projectIdList = new ArrayList<>();
	private List<String> nameEnList = new ArrayList<>();
	private String userType;
	private String hadoopUsername;
	private String subsystemNameEn;
	private String businessTypeName;
	private String cycleName;
	private String stateName;
	private String ownerGroupName;
	private String coordinatorState;
	private String coordinatorStateName;
	private String coordinatorSubmitFlag;
	private String coordinatorStatus;
	private String coordinatorStatusName;

	private GlobalVo globalVo;
	private ConfCredentialVo conHive2Vo;
	private ConfCredentialVo conHbaseVo;
	private ConfCredentialVo conHcatlogVo;

	private String status;
	private String oozieStatus;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date nominalTime;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date nominalStartTime;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date nominalEndTime;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	private Long timeConsume;
	private String timeConsumeStr;

	public String getTimeConsumeStr() {
		if (startTime == null || endTime == null) {
			this.timeConsume = null;
		} else {
			this.timeConsume = (endTime.getTime() - startTime.getTime()) / 1000;
		}
		this.timeConsumeStr = TimeConsumeUtils.TimeConsume(timeConsume);
		return timeConsumeStr;
	}

}
