package com.atomic.hadoop.tenant.service;

import com.atomic.common.core.domain.Ztree;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;

import java.util.List;

/**
 * Hadoop平台模型 服务层
 * 
 * @author atomic
 * @date 2019-04-19
 */
public interface IHadoopPlatformService {
	/**
     * 查询Hadoop平台模型信息
     * 
     * @param id Hadoop平台模型ID
     * @return Hadoop平台模型信息
     */
	HadoopPlatform selectHadoopPlatformById(String id);
	
	/**
     * 查询Hadoop平台模型列表
     * 
     * @param hadoopPlatform Hadoop平台模型信息
     * @return Hadoop平台模型集合
     */
	List<HadoopPlatform> selectHadoopPlatformList(HadoopPlatform hadoopPlatform);
	
	/**
     * 新增Hadoop平台模型
     * 
     * @param hadoopPlatform Hadoop平台模型信息
     * @return 结果
     */
	int insertHadoopPlatform(HadoopPlatform hadoopPlatform);
	
	/**
     * 修改Hadoop平台模型
     * 
     * @param hadoopPlatform Hadoop平台模型信息
     * @return 结果
     */
	int updateHadoopPlatform(HadoopPlatform hadoopPlatform);
		
	/**
     * 删除Hadoop平台模型信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteHadoopPlatformByIds(String ids);

	/**
	 * 查询集群树
	 * @param hadoopPlatform
	 * @return
	 */
	List<Ztree> selectPlatformTree(HadoopPlatform hadoopPlatform);

	int saveDefaultTenantAndCred(String platformId, String principalHive2, String urlHive2, String principalHcatLog, String urlHcatLog, String tenantId, String tenant, String tenantGroup, HadoopPlatform hadoopPlatform, String principal, String localKeytabPath, String localKrb5ConfPath);
	
}
