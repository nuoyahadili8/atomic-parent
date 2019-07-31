package com.atomic.hadoop.oozie.domain;


import com.atomic.common.core.domain.BaseEntity;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.domain.HadoopProject;
import com.atomic.hadoop.tenant.domain.HadoopTenant;
import lombok.Data;

import java.util.Date;

/**
 * 作业表 oozie_job
 * 
 * @author atomic
 * @date 2019-05-20
 */
@Data
public class OozieJob extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	private String jobId;
	/** 项目id */
	private Integer projectId;
	/**
	 * 集群ID
	 */
	private Integer platformId;
	/** 子系统 */
	private Integer subsystemId;
	private String subSystemNameEn;
	private String subSystemNameCn;
	/** 名称(英文) */
	private String nameEn;
	/** 名称(中文) */
	private String nameCn;
	/** 周期 */
	private String cycle;
	/** 调度 */
	private String dispatch;
	/** 定时器 */
	private String timeDevice;
	/** 调度策略 */
	private String strategy;
	/** 优先级 */
	private Integer priority;
	/** 运行时间窗口 */
	private String timeWindow;
	/**  */
	private String enable;
	/** 任务包 */
	private String packageId;
	/** 最新运行时间 */
	private Date nominalTime;
	/** 运行状态 */
	private String status;
	/** 参数 */
	private String parameter;
	/** 备注 */
	private String note;
	/** 上线时间 */
	private Date onlineTime;
	/** 上线人 */
	private String onlineUser;
	/** coordinatorXml */
	private String coordinatorXml;

	private HadoopProject hadoopProject;
	private HadoopPlatform hadoopPlatform;
	private HadoopTenant hadoopTenant;
	private OoziePackageTemplate ooziePackageTemplate;


}
