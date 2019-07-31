package com.atomic.hadoop.oozie.domain;


import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 策略通道表 oozie_strategy
 * 
 * @author atomic
 * @date 2019-06-06
 */
@Data
public class OozieStrategy implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	private Integer id;
	/** 集群平台 */
	private Integer platformId;
	/** 策略类 */
	private String strategyClass;
	/** 策略名 */
	private String strategyName;
	/** 方法名 */
	private String methodName;
	/** 最大并发数 */
	private Integer maxCount;
	/** cron执行表达式 */
	private String cronExpression;
	/** 计划执行错误策略（1立即执行 2执行一次 3放弃执行） */
	private String misfirePolicy;
	/** 是否并发执行（0允许 1禁止） */
	private String concurrent;
	/** 状态（0正常 1暂停） */
	private String status;
	/** 创建人 */
	private String createUser;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** 更新人 */
	private String updateUser;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	private String remark;

	private HadoopPlatform hadoopPlatform;

}
