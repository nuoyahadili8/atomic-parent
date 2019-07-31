package com.atomic.hadoop.oozie.service.impl;

import com.atomic.common.core.text.Convert;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.oozie.domain.OozieSubsystem;
import com.atomic.hadoop.oozie.mapper.OozieSubsystemMapper;
import com.atomic.hadoop.oozie.service.IOozieSubsystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 子系统 服务层实现
 * 
 * @author atomic
 * @date 2019-05-19
 */
@Service
public class OozieSubsystemServiceImpl implements IOozieSubsystemService {
	@Autowired
	private OozieSubsystemMapper oozieSubsystemMapper;

	/**
     * 查询子系统信息
     * 
     * @param id 子系统ID
     * @return 子系统信息
     */
    @Override
	public OozieSubsystem selectOozieSubsystemById(Integer id) {
	    return oozieSubsystemMapper.selectOozieSubsystemById(id);
	}
	
	/**
     * 查询子系统列表
     * 
     * @param oozieSubsystem 子系统信息
     * @return 子系统集合
     */
	@Override
	public List<OozieSubsystem> selectOozieSubsystemList(OozieSubsystem oozieSubsystem) {
	    return oozieSubsystemMapper.selectOozieSubsystemList(oozieSubsystem);
	}
	
    /**
     * 新增子系统
     * 
     * @param oozieSubsystem 子系统信息
     * @return 结果
     */
	@Override
	public int insertOozieSubsystem(OozieSubsystem oozieSubsystem) {
		oozieSubsystem.setCreateUser(ShiroUtils.getLoginName());
		oozieSubsystem.setCreateTime(new Date());
	    return oozieSubsystemMapper.insertOozieSubsystem(oozieSubsystem);
	}
	
	/**
     * 修改子系统
     * 
     * @param oozieSubsystem 子系统信息
     * @return 结果
     */
	@Override
	public int updateOozieSubsystem(OozieSubsystem oozieSubsystem) {
		oozieSubsystem.setUpdateUser(ShiroUtils.getLoginName());
		oozieSubsystem.setUpdateTime(new Date());
	    return oozieSubsystemMapper.updateOozieSubsystem(oozieSubsystem);
	}

	/**
     * 删除子系统对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOozieSubsystemByIds(String ids) {
		return oozieSubsystemMapper.deleteOozieSubsystemByIds(Convert.toStrArray(ids));
	}
	
}
