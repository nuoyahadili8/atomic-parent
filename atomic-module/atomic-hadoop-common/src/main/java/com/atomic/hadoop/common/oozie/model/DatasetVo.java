package com.atomic.hadoop.common.oozie.model;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class DatasetVo {
	private String id;
	private String nameEn;
	private String nameCn;
	private String frequency;
	private String frequencyType;
	private String initialInstance;
	private String uriTemplate;
	private String sourceType;
	private String doneFlag;
	private String taskId;
	private String coordinatorId;
	private String ownerGroup;
	private String createUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String updateUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;


}
