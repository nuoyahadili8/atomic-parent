package com.atomic.hadoop.oozie.mapper;

import com.atomic.hadoop.oozie.domain.OozieHiveSql;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Hive SQL脚本存储 数据层
 * 
 * @author atomic
 * @date 2019-05-09
 */
@Repository
public interface OozieHiveSqlMapper {
	/**
     * 查询Hive SQL脚本存储信息
     * 
     * @param id Hive SQL脚本存储ID
     * @return Hive SQL脚本存储信息
     */
	OozieHiveSql selectOozieHiveSqlById(Integer id);
	
	/**
     * 查询Hive SQL脚本存储列表
     * 
     * @param oozieHiveSql Hive SQL脚本存储信息
     * @return Hive SQL脚本存储集合
     */
	List<OozieHiveSql> selectOozieHiveSqlList(OozieHiveSql oozieHiveSql);
	
	/**
     * 新增Hive SQL脚本存储
     * 
     * @param oozieHiveSql Hive SQL脚本存储信息
     * @return 结果
     */
	int insertOozieHiveSql(OozieHiveSql oozieHiveSql);
	
	/**
     * 修改Hive SQL脚本存储
     * 
     * @param oozieHiveSql Hive SQL脚本存储信息
     * @return 结果
     */
	int updateOozieHiveSql(OozieHiveSql oozieHiveSql);
	
	/**
     * 删除Hive SQL脚本存储
     * 
     * @param id Hive SQL脚本存储ID
     * @return 结果
     */
	int deleteOozieHiveSqlById(Integer id);
	
	/**
     * 批量删除Hive SQL脚本存储
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteOozieHiveSqlByIds(String[] ids);

	/**
	 * 删除task node 以外的多余脚本信息
	 * @param params
	 * @return
	 */
	int deleteExtraScriptFileByPackage(Map<String,Object> params);

	/**
	 * 选择预删除不用的节点脚本
	 * @param params
	 * @return
	 */
	List<OozieHiveSql> selectPreDeleteScript(Map<String,Object> params);
	
}