package com.atomic.hadoop.oozie.service.impl;

import com.atomic.common.core.text.Convert;
import com.atomic.dubbo.api.IStrategyService;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.oozie.domain.OozieStrategy;
import com.atomic.hadoop.oozie.mapper.OozieStrategyMapper;
import com.atomic.hadoop.oozie.service.IOozieStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 策略通道 服务层实现
 * 
 * @author atomic
 * @date 2019-06-06
 */
@Service
public class OozieStrategyServiceImpl implements IOozieStrategyService {
	@Autowired
	private OozieStrategyMapper oozieStrategyMapper;

//	@Reference(version = "1.0.0")
	private IStrategyService strategyService;


	/**
     * 查询策略通道信息
     * 
     * @param id 策略通道ID
     * @return 策略通道信息
     */
    @Override
	public OozieStrategy selectOozieStrategyById(Integer id) {
	    return oozieStrategyMapper.selectOozieStrategyById(id);
	}
	
	/**
     * 查询策略通道列表
     * 
     * @param oozieStrategy 策略通道信息
     * @return 策略通道集合
     */
	@Override
	public List<OozieStrategy> selectOozieStrategyList(OozieStrategy oozieStrategy) {
	    return oozieStrategyMapper.selectOozieStrategyList(oozieStrategy);
	}
	
    /**
     * 新增策略通道
     * 
     * @param oozieStrategy 策略通道信息
     * @return 结果
     */
	@Override
	public int insertOozieStrategy(OozieStrategy oozieStrategy) {
		oozieStrategy.setCreateUser(ShiroUtils.getLoginName());
		oozieStrategy.setCreateTime(new Date());

	    return oozieStrategyMapper.insertOozieStrategy(oozieStrategy);
	}
	
	/**
     * 修改策略通道
     * 
     * @param oozieStrategy 策略通道信息
     * @return 结果
     */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public int updateOozieStrategy(OozieStrategy oozieStrategy) throws Exception {
		oozieStrategy.setUpdateUser(ShiroUtils.getLoginName());
		oozieStrategy.setUpdateTime(new Date());
		if (oozieStrategyMapper.updateOozieStrategy(oozieStrategy) > 0){
			return strategyService.changeStatus(Long.parseLong(oozieStrategy.getId()+""));
		}
	    return 0;
	}

	/**
     * 删除策略通道对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOozieStrategyByIds(String ids) {
		return oozieStrategyMapper.deleteOozieStrategyByIds(Convert.toStrArray(ids));
	}
	
}
