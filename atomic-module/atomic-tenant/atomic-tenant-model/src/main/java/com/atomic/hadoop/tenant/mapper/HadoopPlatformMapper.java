package com.atomic.hadoop.tenant.mapper;

import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Hadoop平台模型 数据层
 * 
 * @author atomic
 * @date 2019-04-19
 */
@Repository
public interface HadoopPlatformMapper {
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
     * 删除Hadoop平台模型
     * 
     * @param id Hadoop平台模型ID
     * @return 结果
     */
	int deleteHadoopPlatformById(String id);
	
	/**
     * 批量删除Hadoop平台模型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteHadoopPlatformByIds(String[] ids);
	
}