package com.atomic.hadoop.common.oozie.vo;

import lombok.Data;

@Data
public class GlobalVo {
	
	private String id;
	private String global_nameen;
	private String global_namecn;
	private String job_tracker;
	private String name_node;
	private String job_xml;
	private String configuration;
	private String create_user;
	private String create_date;
	private String update_user;
	private String update_date;
	private String tenant;
	
}
