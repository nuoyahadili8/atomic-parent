package com.atomic.hadoop.oozie.service;

import com.atomic.hadoop.oozie.domain.OozieJob;
import com.atomic.hadoop.oozie.domain.OozieJobDepedency;

import java.util.List;

/**
 * 作业依赖关系 服务层
 * 
 * @author atomic
 * @date 2019-05-23
 */
public interface IOozieJobDepedencyService {
	/**
     * 查询作业依赖关系信息
     * 
     * @param id 作业依赖关系ID
     * @return 作业依赖关系信息
     */
	OozieJobDepedency selectOozieJobDepedencyById(String id);
	
	/**
     * 查询作业依赖关系列表
     * 
     * @param oozieJobDepedency 作业依赖关系信息
     * @return 作业依赖关系集合
     */
	List<OozieJobDepedency> selectOozieJobDepedencyList(OozieJobDepedency oozieJobDepedency);
	
	/**
     * 新增作业依赖关系
     * 
     * @param oozieJobDepedency 作业依赖关系信息
     * @return 结果
     */
	int insertOozieJobDepedency(OozieJobDepedency oozieJobDepedency);
	
	/**
     * 修改作业依赖关系
     * 
     * @param oozieJobDepedency 作业依赖关系信息
     * @return 结果
     */
	int updateOozieJobDepedency(OozieJobDepedency oozieJobDepedency);
		
	/**
     * 删除作业依赖关系信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteOozieJobDepedencyByIds(String[] ids);

	/**
	 * 根据jobId获取依赖作业列表
	 * @param oozieJob
	 * @return
	 */
	List<OozieJob> getDepedencyList(OozieJob oozieJob);

}
