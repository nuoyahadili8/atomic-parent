package com.atomic.hadoop.oozie.log.service;


import com.atomic.hadoop.oozie.log.domain.WfJobsVw;

import java.util.List;

/**
 * 任务运行视图 服务层
 * 
 * @author atomic
 * @date 2019-07-26
 */
public interface IWfJobsVwService {
	/**
     * 查询任务运行视图信息
     * 
     * @param id 任务运行视图ID
     * @return 任务运行视图信息
     */
	WfJobsVw selectWfJobsVwById(String id);
	
	/**
     * 查询任务运行视图列表
     * 
     * @param wfJobsVw 任务运行视图信息
     * @return 任务运行视图集合
     */
	List<WfJobsVw> selectWfJobsVwList(WfJobsVw wfJobsVw);
	
}
