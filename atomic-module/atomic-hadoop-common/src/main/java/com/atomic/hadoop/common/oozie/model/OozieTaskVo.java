package com.atomic.hadoop.common.oozie.model;

import com.atomic.hadoop.common.oozie.utils.TimeConsumeUtils;
import com.atomic.hadoop.common.oozie.vo.TaskVo;
import lombok.Data;

import java.util.Date;

@Data
public class OozieTaskVo {

	private String id;
	private TaskVo taskVo;
	private String lastStatus;
	private Date normalTime;
	private Date lastTriggerTime;
	private Date lastStartTime;
	private Date lastEndTime;
	private Long timeConsume;
	private String timeConsumeStr;

	public OozieTaskVo() {
	}
	
	public OozieTaskVo(TaskVo taskVo,String status,Date normalTime){
		this.taskVo=taskVo;
		this.lastStatus=status;
		this.normalTime=normalTime;
	}

	public OozieTaskVo(TaskVo taskVo, String lastStatus, Date lastStartTime, Date lastEndTime, String id) {
		this.taskVo = taskVo;
		this.lastStatus = lastStatus;
		this.lastStartTime = lastStartTime;
		this.lastEndTime = lastEndTime;
		if (lastStartTime == null || lastEndTime == null) {
			this.timeConsume = null;
		} else {
			this.timeConsume = (lastEndTime.getTime() - lastStartTime.getTime()) / 1000;
		}
		this.timeConsumeStr = TimeConsumeUtils.TimeConsume(timeConsume);
		this.id = id;
	}

}
