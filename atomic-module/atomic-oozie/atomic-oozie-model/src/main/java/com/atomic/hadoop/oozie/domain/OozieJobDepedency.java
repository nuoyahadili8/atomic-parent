package com.atomic.hadoop.oozie.domain;


import com.atomic.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 作业依赖关系表 oozie_job_depedency
 * 
 * @author atomic
 * @date 2019-05-23
 */
@Data
public class OozieJobDepedency extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String id;
	/** 作业ID */
	private String jobId;
	/** 依赖作业ID */
	private String depedencyId;
	/** 子系统 */
	private Integer subsystemId;
	private String subsystemNameEn;
	/** 依赖作业名称(En) */
	private String depedencyNameEn;
	/** 依赖作业名称(Cn) */
	private String depedencyNameCn;
	/** 周期 */
	private String cycle;
	/** 运行状态 */
	private String status;
	/** 开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;
	/** 结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;
	/** 运行时长 */
	private String durTime;
	/** 资源队列 */
	private String yarnQueue;
	/** 是否启用 */
	private String enable;

}
