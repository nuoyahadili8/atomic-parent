package com.atomic.hadoop.oozie.service;



import com.atomic.hadoop.oozie.domain.OozieStrategy;

import java.util.List;

/**
 * 策略通道 服务层
 * 
 * @author atomic
 * @date 2019-06-06
 */
public interface IOozieStrategyService {
	/**
     * 查询策略通道信息
     * 
     * @param id 策略通道ID
     * @return 策略通道信息
     */
	OozieStrategy selectOozieStrategyById(Integer id);
	
	/**
     * 查询策略通道列表
     * 
     * @param oozieStrategy 策略通道信息
     * @return 策略通道集合
     */
	List<OozieStrategy> selectOozieStrategyList(OozieStrategy oozieStrategy);
	
	/**
     * 新增策略通道
     * 
     * @param oozieStrategy 策略通道信息
     * @return 结果
     */
	int insertOozieStrategy(OozieStrategy oozieStrategy);
	
	/**
     * 修改策略通道
     * 
     * @param oozieStrategy 策略通道信息
     * @return 结果
     */
	int updateOozieStrategy(OozieStrategy oozieStrategy) throws Exception;
		
	/**
     * 删除策略通道信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteOozieStrategyByIds(String ids) throws Exception;
	
}
