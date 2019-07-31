package com.atomic.hadoop.tenant.service;

import com.atomic.hadoop.tenant.domain.HadoopDefaultTenant;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;

import java.io.IOException;
import java.util.List;

/**
 * Hadoop集群代理租户 服务层
 * 
 * @author atomic
 * @date 2019-04-21
 */
public interface IHadoopDefaultTenantService {
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
     * 删除Hadoop集群代理租户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteHadoopDefaultTenantByIds(String ids);

	int saveOrUpdateHadoopDefaultTenant(String tenantId, String tenant, String tenantGroup, HadoopPlatform hadoopPlatform, String pricipal, String keytabName, String localKrb5ConfPath) throws IOException;

	/**
	 * 通过集群ID获取默认代理租户
	 * @param platformId
	 * @return
	 */
	HadoopDefaultTenant selectHadoopDefaultTenantByPlatformId(Integer platformId);

}
