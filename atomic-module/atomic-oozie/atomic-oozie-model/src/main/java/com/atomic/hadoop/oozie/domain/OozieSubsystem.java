package com.atomic.hadoop.oozie.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 子系统表 oozie_subsystem
 * 
 * @author atomic
 * @date 2019-05-19
 */
@Data
public class OozieSubsystem{
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	private Integer id;
	/** 英文(简称) */
	private String nameEn;
	/** 英文(全称) */
	private String nameEnFull;
	/** 名称(中文) */
	private String nameCn;
	/** 顺序号 */
	private Integer orderNo;
	/** 创建者 */
	private String createUser;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/** 更新者 */
	private String updateUser;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

}
