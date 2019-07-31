package com.atomic.hadoop.tenant.mapper;

import com.atomic.hadoop.tenant.domain.HadoopDefaultTenant;

import java.util.List;

/**
 * Hadoop集群代理租户 数据层
 * 
 * @author atomic
 * @date 2019-04-21
 */
public interface HadoopDefaultTenantMapper {
	/**
     * 查询Hadoop集群代理租户信息
     * 
     * @param id Hadoop集群代理租户ID
     * @return Hadoop集群代理租户信息
     */
	HadoopDefaultTenant selectHadoopDefaultTenantById(Integer id);
	
	/**
     * 查询Hadoop集群代理租户列表
     * 
     * @param hadoopDefaultTenant Hadoop集群代理租户信息
     * @return Hadoop集群代理租户集合
     */
	List<HadoopDefaultTenant> selectHadoopDefaultTenantList(HadoopDefaultTenant hadoopDefaultTenant);
	
	/**
     * 新增Hadoop集群代理租户
     * 
     * @param hadoopDefaultTenant Hadoop集群代理租户信息
     * @return 结果
     */
	int insertHadoopDefaultTenant(HadoopDefaultTenant hadoopDefaultTenant);
	
	/**
     * 修改Hadoop集群代理租户
     * 
     * @param hadoopDefaultTenant Hadoop集群代理租户信息
     * @return 结果
     */
	int updateHadoopDefaultTenant(HadoopDefaultTenant hadoopDefaultTenant);
	
	/**
     * 删除Hadoop集群代理租户
     * 
     * @param id Hadoop集群代理租户ID
     * @return 结果
     */
	int deleteHadoopDefaultTenantById(Integer id);
	
	/**
     * 批量删除Hadoop集群代理租户
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteHadoopDefaultTenantByIds(String[] ids);

	/**
	 * 通过集群ID获取默认代理租户
	 * @param platformId
	 * @return
	 */
	HadoopDefaultTenant selectHadoopDefaultTenantByPlatformId(Integer platformId);
	
}