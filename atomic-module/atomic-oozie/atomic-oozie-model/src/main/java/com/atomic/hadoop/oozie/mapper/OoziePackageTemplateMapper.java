package com.atomic.hadoop.oozie.mapper;

import com.atomic.hadoop.oozie.domain.OoziePackageTemplate;

import java.util.List;

/**
 * 任务包 数据层
 * 
 * @author atomic
 * @date 2019-05-12
 */
public interface OoziePackageTemplateMapper {
	/**
     * 查询任务包信息
     * 
     * @param packageId 任务包ID
     * @return 任务包信息
     */
	OoziePackageTemplate selectOoziePackageTemplateById(String packageId);
	
	/**
     * 查询任务包列表
     * 
     * @param ooziePackageTemplate 任务包信息
     * @return 任务包集合
     */
	List<OoziePackageTemplate> selectOoziePackageTemplateList(OoziePackageTemplate ooziePackageTemplate);

	/**
	 * 查询任务包，排除普通包被使用的。
	 * @param ooziePackageTemplate
	 * @return
	 */
	List<OoziePackageTemplate> selectPackageWithNotUsedOfCommonPackage(OoziePackageTemplate ooziePackageTemplate);
	
	/**
     * 新增任务包
     * 
     * @param ooziePackageTemplate 任务包信息
     * @return 结果
     */
	int insertOoziePackageTemplate(OoziePackageTemplate ooziePackageTemplate);
	
	/**
     * 修改任务包
     * 
     * @param ooziePackageTemplate 任务包信息
     * @return 结果
     */
	int updateOoziePackageTemplate(OoziePackageTemplate ooziePackageTemplate);
	
	/**
     * 删除任务包
     * 
     * @param packageId 任务包ID
     * @return 结果
     */
	int deleteOoziePackageTemplateById(String packageId);
	
	/**
     * 批量删除任务包
     * 
     * @param packageIds 需要删除的数据ID
     * @return 结果
     */
	int deleteOoziePackageTemplateByIds(String[] packageIds);
	
}