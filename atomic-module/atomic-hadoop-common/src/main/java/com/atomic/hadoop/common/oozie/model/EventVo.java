package com.atomic.hadoop.common.oozie.model;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class EventVo {
	private String id;
	private String name;
	private String flag;
	private String instanceBegin;
	private String instanceEnd;
	private String datasetId;
	private String coordinatorId;
	private String taskId;
	private String taskDepedencyRelationId;
	private String createUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String updateUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	private DatasetVo datasetVo;

}
