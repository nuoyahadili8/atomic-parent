package com.atomic.hadoop.tenant.service.impl;

import com.atomic.common.core.text.Convert;
import com.atomic.hadoop.tenant.domain.HadoopTenant;
import com.atomic.hadoop.tenant.mapper.HadoopTenantMapper;
import com.atomic.hadoop.tenant.service.IHadoopTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Hadoop平台租户模型 服务层实现
 * 
 * @author atomic
 * @date 2019-04-20
 */
@Service
public class HadoopTenantServiceImpl implements IHadoopTenantService {
	@Autowired
	private HadoopTenantMapper hadoopTenantMapper;

	/**
     * 查询Hadoop平台租户模型信息
     * 
     * @param id Hadoop平台租户模型ID
     * @return Hadoop平台租户模型信息
     */
    @Override
	public HadoopTenant selectHadoopTenantById(String id) {
	    return hadoopTenantMapper.selectHadoopTenantById(id);
	}
	
	/**
     * 查询Hadoop平台租户模型列表
     * 
     * @param hadoopTenant Hadoop平台租户模型信息
     * @return Hadoop平台租户模型集合
     */
	@Override
	public List<HadoopTenant> selectHadoopTenantList(HadoopTenant hadoopTenant) {
	    return hadoopTenantMapper.selectHadoopTenantList(hadoopTenant);
	}
	
    /**
     * 新增Hadoop平台租户模型
     * 
     * @param hadoopTenant Hadoop平台租户模型信息
     * @return 结果
     */
	@Override
	public int insertHadoopTenant(HadoopTenant hadoopTenant) {
	    return hadoopTenantMapper.insertHadoopTenant(hadoopTenant);
	}
	
	/**
     * 修改Hadoop平台租户模型
     * 
     * @param hadoopTenant Hadoop平台租户模型信息
     * @return 结果
     */
	@Override
	public int updateHadoopTenant(HadoopTenant hadoopTenant) {
	    return hadoopTenantMapper.updateHadoopTenant(hadoopTenant);
	}

	/**
     * 删除Hadoop平台租户模型对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteHadoopTenantByIds(String ids) {
		return hadoopTenantMapper.deleteHadoopTenantByIds(Convert.toStrArray(ids));
	}

}
