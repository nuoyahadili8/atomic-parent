package com.atomic.hadoop.tenant.mapper;

import com.atomic.hadoop.tenant.domain.HadoopTenant;

import java.util.List;

/**
 * Hadoop平台租户模型 数据层
 * 
 * @author atomic
 * @date 2019-04-20
 */
public interface HadoopTenantMapper {
	/**
     * 查询Hadoop平台租户模型信息
     * 
     * @param id Hadoop平台租户模型ID
     * @return Hadoop平台租户模型信息
     */
	HadoopTenant selectHadoopTenantById(String id);
	
	/**
     * 查询Hadoop平台租户模型列表
     * 
     * @param hadoopTenant Hadoop平台租户模型信息
     * @return Hadoop平台租户模型集合
     */
	List<HadoopTenant> selectHadoopTenantList(HadoopTenant hadoopTenant);
	
	/**
     * 新增Hadoop平台租户模型
     * 
     * @param hadoopTenant Hadoop平台租户模型信息
     * @return 结果
     */
	int insertHadoopTenant(HadoopTenant hadoopTenant);
	
	/**
     * 修改Hadoop平台租户模型
     * 
     * @param hadoopTenant Hadoop平台租户模型信息
     * @return 结果
     */
	int updateHadoopTenant(HadoopTenant hadoopTenant);
	
	/**
     * 删除Hadoop平台租户模型
     * 
     * @param id Hadoop平台租户模型ID
     * @return 结果
     */
	int deleteHadoopTenantById(String id);
	
	/**
     * 批量删除Hadoop平台租户模型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteHadoopTenantByIds(String[] ids);
	
}