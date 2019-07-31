package com.atomic.hadoop.oozie.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.atomic.common.core.text.Convert;
import com.atomic.hadoop.oozie.domain.OozieHiveSql;
import com.atomic.hadoop.oozie.mapper.OozieHiveSqlMapper;
import com.atomic.hadoop.oozie.service.IOozieHiveSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Hive SQL脚本存储 服务层实现
 * 
 * @author atomic
 * @date 2019-05-09
 */
@Service
public class OozieHiveSqlServiceImpl implements IOozieHiveSqlService {
	@Autowired
	private OozieHiveSqlMapper oozieHiveSqlMapper;

	/**
     * 查询Hive SQL脚本存储信息
     * 
     * @param id Hive SQL脚本存储ID
     * @return Hive SQL脚本存储信息
     */
    @Override
	public OozieHiveSql selectOozieHiveSqlById(Integer id) {
	    return oozieHiveSqlMapper.selectOozieHiveSqlById(id);
	}
	
	/**
     * 查询Hive SQL脚本存储列表
     * 
     * @param oozieHiveSql Hive SQL脚本存储信息
     * @return Hive SQL脚本存储集合
     */
	@Override
	public List<OozieHiveSql> selectOozieHiveSqlList(OozieHiveSql oozieHiveSql) {
	    return oozieHiveSqlMapper.selectOozieHiveSqlList(oozieHiveSql);
	}
	
    /**
     * 新增Hive SQL脚本存储
     * 
     * @param oozieHiveSql Hive SQL脚本存储信息
     * @return 结果
     */
	@Override
	public int insertOozieHiveSql(OozieHiveSql oozieHiveSql) {
	    return oozieHiveSqlMapper.insertOozieHiveSql(oozieHiveSql);
	}
	
	/**
     * 修改Hive SQL脚本存储
     * 
     * @param oozieHiveSql Hive SQL脚本存储信息
     * @return 结果
     */
	@Override
	public int updateOozieHiveSql(OozieHiveSql oozieHiveSql) {
	    return oozieHiveSqlMapper.updateOozieHiveSql(oozieHiveSql);
	}

	/**
     * 删除Hive SQL脚本存储对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOozieHiveSqlByIds(String ids) {
		return oozieHiveSqlMapper.deleteOozieHiveSqlByIds(Convert.toStrArray(ids));
	}

	@Override
	public int saveHSQLToDB(OozieHiveSql oozieHiveSql) {
		String hsql=oozieHiveSql.getHsql();
//		ShiroUtils.getLoginName();
		String user=null;
		oozieHiveSql.setHsql(null);
		List<OozieHiveSql> oozieHiveSqls = selectOozieHiveSqlList(oozieHiveSql);
		if (CollUtil.isEmpty(oozieHiveSqls)){
			oozieHiveSql.setHsql(hsql);
			oozieHiveSql.setCreateUser(user == null? "admin":user);
			oozieHiveSql.setCreateTime(new Date());
			insertOozieHiveSql(oozieHiveSql);
		} else {
			oozieHiveSql = oozieHiveSqls.get(0);
			oozieHiveSql.setHsql(hsql);
			oozieHiveSql.setUpdateUser(user == null? "admin":user);
			oozieHiveSql.setUpdateTime(new Date());
			updateOozieHiveSql(oozieHiveSql);
		}
		return 0;
	}

	@Override
	public int deleteExtraScriptFileByPackage(Map<String, Object> params) {
		return oozieHiveSqlMapper.deleteExtraScriptFileByPackage(params);
	}

	@Override
	public List<OozieHiveSql> selectPreDeleteScript(Map<String, Object> params) {
		return oozieHiveSqlMapper.selectPreDeleteScript(params);
	}

}
