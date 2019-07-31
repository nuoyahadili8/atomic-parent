package com.atomic.hadoop.common.oozie.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class ConfCredentialVo {
	private String id;
	private String platformId;
	private String name;
	private String type;
	private String principal;
	private String url;
	private String ownerGroup;
	private String createUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String updateUser;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	private String platformName;

}
