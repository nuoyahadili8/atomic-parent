package com.atomic.hadoop.tenant.domain;


import com.atomic.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 组织与工程关系表 hadoop_project_dept
 * 
 * @author atomic
 * @date 2019-05-01
 */
@Data
public class HadoopProjectDept extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 工程项目 */
	private Integer projectId;
	/** 组织 */
	private Long deptId;

}
