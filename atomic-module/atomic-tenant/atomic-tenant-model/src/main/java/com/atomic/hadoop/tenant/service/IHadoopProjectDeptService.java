package com.atomic.hadoop.tenant.service;

import com.atomic.hadoop.tenant.domain.HadoopProjectDept;

import java.util.List;

/**
 * 组织与工程关系 服务层
 * 
 * @author atomic
 * @date 2019-05-01
 */
public interface IHadoopProjectDeptService {
	/**
     * 查询组织与工程关系信息
     * 
     * @param projectId 组织与工程关系ID
     * @return 组织与工程关系信息
     */
	HadoopProjectDept selectHadoopProjectDeptById(Integer projectId);
	
	/**
     * 查询组织与工程关系列表
     * 
     * @param hadoopProjectDept 组织与工程关系信息
     * @return 组织与工程关系集合
     */
	List<HadoopProjectDept> selectHadoopProjectDeptList(HadoopProjectDept hadoopProjectDept);
	
	/**
     * 新增组织与工程关系
     * 
     * @param hadoopProjectDept 组织与工程关系信息
     * @return 结果
     */
	int insertHadoopProjectDept(HadoopProjectDept hadoopProjectDept);
	
	/**
     * 修改组织与工程关系
     * 
     * @param hadoopProjectDept 组织与工程关系信息
     * @return 结果
     */
	int updateHadoopProjectDept(HadoopProjectDept hadoopProjectDept);
		
	/**
     * 删除组织与工程关系信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteHadoopProjectDeptByIds(String ids);

	/**
	 * 更新操作
	 * @param deptIds
	 * @param projectId
	 * @return
	 */
	int saveOrUpdateHadoopProjectDept(String deptIds, String projectId);

}
