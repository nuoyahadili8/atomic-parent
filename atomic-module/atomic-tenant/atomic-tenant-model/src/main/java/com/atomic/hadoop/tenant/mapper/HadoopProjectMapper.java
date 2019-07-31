package com.atomic.hadoop.tenant.mapper;

import com.atomic.hadoop.tenant.domain.HadoopProject;

import java.util.List;

/**
 * 工程项目 数据层
 * 
 * @author atomic
 * @date 2019-04-26
 */
public interface HadoopProjectMapper {
	/**
     * 查询工程项目信息
     * 
     * @param id 工程项目ID
     * @return 工程项目信息
     */
	HadoopProject selectHadoopProjectById(Integer id);
	
	/**
     * 查询工程项目列表
     * 
     * @param hadoopProject 工程项目信息
     * @return 工程项目集合
     */
	List<HadoopProject> selectHadoopProjectList(HadoopProject hadoopProject);
	
	/**
     * 新增工程项目
     * 
     * @param hadoopProject 工程项目信息
     * @return 结果
     */
	int insertHadoopProject(HadoopProject hadoopProject);
	
	/**
     * 修改工程项目
     * 
     * @param hadoopProject 工程项目信息
     * @return 结果
     */
	int updateHadoopProject(HadoopProject hadoopProject);
	
	/**
     * 删除工程项目
     * 
     * @param id 工程项目ID
     * @return 结果
     */
	int deleteHadoopProjectById(Integer id);
	
	/**
     * 批量删除工程项目
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteHadoopProjectByIds(String[] ids);

	/**
	 * 检查项目工程名字的唯一性
	 * @param name
	 * @return
	 */
	int checkProjectNameUnique(String name);
	
}