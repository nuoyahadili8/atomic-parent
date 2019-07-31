package com.atomic.hadoop.tenant.domain;


import com.atomic.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * Hadoop平台租户模型表 hadoop_tenant
 * 
 * @author atomic
 * @date 2019-04-20
 */
@Data
public class HadoopTenant extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	private String id;
	/** 租户名 */
	private String tenant;
	/** 租户组 */
	private String tenantGroup;
	/** 所属的Hadoop平台ID */
	private String platformId;
	/** 是否为HDFS代理用户:1是,0否 */
	private Integer isPoxyuser;
	/** Yarn资源队列 */
	private String yarnQueue;
	/** keytab文件路径 */
	private String keytabFile;
	/** 是否启用 */
	private String isEnable;
	/** 创建者 */
	private String createUser;
	/** 更新者 */
	private String updateUser;

	private HadoopPlatform hadoopPlatform;


}
