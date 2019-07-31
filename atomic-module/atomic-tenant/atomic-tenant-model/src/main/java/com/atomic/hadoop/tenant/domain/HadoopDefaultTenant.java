package com.atomic.hadoop.tenant.domain;


import lombok.Data;

/**
 * Hadoop集群代理租户表 hadoop_default_tenant
 * 
 * @author atomic
 * @date 2019-04-21
 */
@Data
public class HadoopDefaultTenant {
	/** 唯一标识 */
	private Integer id;
	/** 租户名 */
	private String tenant;
	/** 租户组 */
	private String tenantGroup;
	/** Hadoop注册平台ID */
	private Integer platformId;
	/** 是否开启kerberos认证 */
	private Integer haveKerberos;
	/** Kerberos认证Principal名 */
	private String kerberosPrincipal;
	/** keytab文件名 */
	private String keytabFileName;
	/** Kerberos认证 keytab文件 */
	private byte[] keytabFile;
	/** krb5.conf文件名 */
	private String krb5ConfFileName;
	/** krb5.conf认证文件 */
	private byte[] krb5ConfFile;
	/** Hadoop集群平台*/
	private HadoopPlatform hadoopPlatform;

	public HadoopDefaultTenant(){}

	public HadoopDefaultTenant(Integer platformId){
		this.platformId=platformId;
	}

}
