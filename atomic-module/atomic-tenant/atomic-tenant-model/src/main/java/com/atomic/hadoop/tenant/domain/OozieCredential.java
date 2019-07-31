package com.atomic.hadoop.tenant.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Oozie安全认证凭据表 oozie_credential
 * 
 * @author atomic
 * @date 2019-05-15
 */
@Data
public class OozieCredential {
	/** 编码 */
	private Integer id;
	/** 平台编码 */
	private Integer platformId;
	/** 名称 */
	private String name;
	/** 类型 */
	private String type;
	/** 规则 */
	private String principal;
	/** 链接 */
	private String url;
	/** 创建人 */
	private String createUser;
	/** 更新人 */
	private String updateUser;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/** 更新时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
}
