package com.atomic.hadoop.oozie.service.impl;

import com.atomic.hadoop.oozie.domain.OozieJob;
import com.atomic.hadoop.oozie.domain.OozieJobDepedency;
import com.atomic.hadoop.oozie.mapper.OozieJobDepedencyMapper;
import com.atomic.hadoop.oozie.service.IOozieJobDepedencyService;
import com.atomic.hadoop.oozie.service.IOozieJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作业依赖关系 服务层实现
 * 
 * @author atomic
 * @date 2019-05-23
 */
@Service
public class OozieJobDepedencyServiceImpl implements IOozieJobDepedencyService {
	@Autowired
	private OozieJobDepedencyMapper oozieJobDepedencyMapper;

	@Autowired
	private IOozieJobService oozieJobService;

	/**
     * 查询作业依赖关系信息
     * 
     * @param id 作业依赖关系ID
     * @return 作业依赖关系信息
     */
    @Override
	public OozieJobDepedency selectOozieJobDepedencyById(String id) {
	    return oozieJobDepedencyMapper.selectOozieJobDepedencyById(id);
	}
	
	/**
     * 查询作业依赖关系列表
     * 
     * @param oozieJobDepedency 作业依赖关系信息
     * @return 作业依赖关系集合
     */
	@Override
	public List<OozieJobDepedency> selectOozieJobDepedencyList(OozieJobDepedency oozieJobDepedency) {
	    return oozieJobDepedencyMapper.selectOozieJobDepedencyList(oozieJobDepedency);
	}
	
    /**
     * 新增作业依赖关系
     * 
     * @param oozieJobDepedency 作业依赖关系信息
     * @return 结果
     */
	@Override
	public int insertOozieJobDepedency(OozieJobDepedency oozieJobDepedency) {
	    return oozieJobDepedencyMapper.insertOozieJobDepedency(oozieJobDepedency);
	}
	
	/**
     * 修改作业依赖关系
     * 
     * @param oozieJobDepedency 作业依赖关系信息
     * @return 结果
     */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateOozieJobDepedency(OozieJobDepedency oozieJobDepedency) {
		OozieJobDepedency depedency = oozieJobDepedencyMapper.selectOozieJobDepedencyById(oozieJobDepedency.getId());
		oozieJobDepedencyMapper.updateOozieJobDepedency(oozieJobDepedency);
		OozieJob oozieJob = oozieJobService.selectOozieJobById(depedency.getJobId());
		String coordinatorXml = oozieJobService.getCoordinatorXml(oozieJob);
		oozieJob.setCoordinatorXml(coordinatorXml);
		oozieJobService.updateOozieJob(oozieJob);
	    return 1;
	}

	/**
     * 删除作业依赖关系对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOozieJobDepedencyByIds(String[] ids) {
		return oozieJobDepedencyMapper.deleteOozieJobDepedencyByIds(ids);
	}

	@Override
	public List<OozieJob> getDepedencyList(OozieJob oozieJob) {
		return oozieJobDepedencyMapper.getDepedencyList(oozieJob);
	}

}
