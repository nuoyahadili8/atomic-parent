package com.atomic.hadoop.tenant.domain;


import com.atomic.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 工程项目表 hadoop_project
 * 
 * @author atomic
 * @date 2019-04-26
 */
@Data
public class HadoopProject extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	private Integer projectId;
	/** 项目名称 */
	private String nameEn;
	private String nameCn;
	/** 项目版本 */
	private String projectVersion;
	/** 项目描述 */
	private String description;
	/** 客户 */
	private Integer demanderDeptId;
	private String demanderDeptName;
	/** 项目经理 */
	private Integer demanderUserId;
	private String demanderUserName;
	/** 项目规模 */
	private Double projectSize;
	/** 工作量 */
	private Double projectEffort;
	/** 项目进度 */
	private Double projectSchedule;
	/** 项目状态 */
	private Integer projectStatus;
	/** 大数据平台 */
	private Integer platformId;
	private String platformName;
	/** 租户 */
	private Integer tenantId;
	private String tenant;
	/** 创建人 */
	private String createUser;
	/** 更新人 */
	private String updateUser;

	private HadoopTenant hadoopTenant;

	private HadoopPlatform hadoopPlatform;

	/**
	 * 任务包数量
	 */
	private int packagesCnt;


}
