package com.atomic.hadoop.oozie.domain;

import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.domain.HadoopProject;
import com.atomic.hadoop.tenant.domain.HadoopTenant;
import lombok.Data;

import java.util.Date;

/**
 * 包模板表 oozie_package_template
 * 
 * @author atomic
 * @date 2019-05-12
 */
@Data
public class OoziePackageTemplate{
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	private String packageId;
	/** 包类型 */
	private String packageType;
	/** 包状态 */
	private String packageStatus;
	/** 检出人 */
	private String detection;

	/** 包名(en) */
	private String nameEn;
	/** 包名(cn) */
	private String nameCn;
	/** 项目ID */
	private Integer projectId;
	/** HIVE2认证 */
	private String conHive2Id;
	/** hcatlog认证 */
	private String conHcatalogId;
	/** hbase认证 */
	private String conHbaseId;
	/** json流程格式 */
	private String workflowJson;
	/** xml流程格式 */
	private String workflowXml;
	/** 描述 */
	private String note;
	/** 创建者 */
	private String createUser;
	private Date createTime;
	/** 创建者 */
	private String updateUser;
	private Date updateTime;
	/**
	 * 大数据平台
	 */
	private HadoopProject hadoopProject;

	private HadoopPlatform hadoopPlatform;

	private HadoopTenant hadoopTenant;

}
