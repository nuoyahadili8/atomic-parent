package com.atomic.hadoop.oozie.log.domain;


import lombok.Data;

import java.util.Date;

/**
 * 任务运行视图表 wf_jobs_vw
 * 
 * @author atomic
 * @date 2019-07-26
 */
@Data
public class WfJobsVw{
	private static final long serialVersionUID = 1L;
	
	/** 工作流ID */
	private String id;
	/** 任务名 */
	private String appName;
	/** workflow任务路径 */
	private String appPath;
	/** 租户 */
	private String userName;
	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 理论运行时间 */
	private Date nominalTime;
	/** 运行状态 */
	private String status;

}
