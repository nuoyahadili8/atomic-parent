package com.atomic.hadoop.oozie.log.service.impl;

import java.util.List;

import com.atomic.hadoop.oozie.log.domain.WfJobsVw;
import com.atomic.hadoop.oozie.log.mapper.WfJobsVwMapper;
import com.atomic.hadoop.oozie.log.service.IWfJobsVwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务运行视图 服务层实现
 * 
 * @author atomic
 * @date 2019-07-26
 */
@Service
public class WfJobsVwServiceImpl implements IWfJobsVwService {
	@Autowired
	private WfJobsVwMapper wfJobsVwMapper;

	/**
     * 查询任务运行视图信息
     * 
     * @param id 任务运行视图ID
     * @return 任务运行视图信息
     */
    @Override
	public WfJobsVw selectWfJobsVwById(String id) {
	    return wfJobsVwMapper.selectWfJobsVwById(id);
	}
	
	/**
     * 查询任务运行视图列表
     * 
     * @param wfJobsVw 任务运行视图信息
     * @return 任务运行视图集合
     */
	@Override
	public List<WfJobsVw> selectWfJobsVwList(WfJobsVw wfJobsVw) {
	    return wfJobsVwMapper.selectWfJobsVwList(wfJobsVw);
	}
	
}
