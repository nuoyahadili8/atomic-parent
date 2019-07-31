package com.atomic.hadoop.oozie.log.mapper;


import com.atomic.common.annotation.DataSource;
import com.atomic.common.enums.DataSourceType;
import com.atomic.hadoop.oozie.log.domain.WfJobsVw;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 任务运行视图 数据层
 * 
 * @author atomic
 * @date 2019-07-26
 */
@Repository
public interface WfJobsVwMapper {
	/**
     * 查询任务运行视图信息
     * 
     * @param id 任务运行视图ID
     * @return 任务运行视图信息
     */
	@DataSource(value = DataSourceType.SLAVE)
	WfJobsVw selectWfJobsVwById(String id);
	
	/**
     * 查询任务运行视图列表
     * 
     * @param wfJobsVw 任务运行视图信息
     * @return 任务运行视图集合
     */
	@DataSource(value = DataSourceType.SLAVE)
	List<WfJobsVw> selectWfJobsVwList(WfJobsVw wfJobsVw);
	

	
}