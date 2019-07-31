package com.atomic.hadoop.oozie.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Hive SQL脚本存储表 oozie_hive_sql
 * 
 * @author atomic
 * @date 2019-05-09
 */
@Data
public class OozieHiveSql {
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	private Integer id;
	/** 版本 */
	private Integer version;
	/** 任务ID */
	private String taskId;
	/** hive2 节点ID */
	private String nodeId;
	/** hive2 SQL内容 */
	private String hsql;
	/** hsql存储在HDFS的路径 */
	private String hsqlPath;
	/**
	 * 脚本类型
	 */
	private String scriptType;
	/** 创建者 */
	private String createUser;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/** 更新者 */
	private String updateUser;
	/** 更新时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;


}
