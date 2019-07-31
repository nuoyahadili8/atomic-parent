package com.atomic.hadoop.tenant.service.impl;

import com.atomic.common.constant.UserConstants;
import com.atomic.common.core.domain.Ztree;
import com.atomic.common.core.text.Convert;
import com.atomic.common.utils.StringUtils;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.mapper.HadoopPlatformMapper;
import com.atomic.hadoop.tenant.service.IHadoopDefaultTenantService;
import com.atomic.hadoop.tenant.service.IHadoopPlatformService;
import com.atomic.hadoop.tenant.service.IOozieCredentialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hadoop平台模型 服务层实现
 * 
 * @author atomic
 * @date 2019-04-19
 */
@Service
@Slf4j
public class HadoopPlatformServiceImpl implements IHadoopPlatformService {
	@Autowired
	private HadoopPlatformMapper hadoopPlatformMapper;

	@Autowired
	private IHadoopDefaultTenantService hadoopDefaultTenantService;

	@Autowired
	private IOozieCredentialService oozieCredentialService;

	/**
     * 查询Hadoop平台模型信息
     * 
     * @param id Hadoop平台模型ID
     * @return Hadoop平台模型信息
     */
    @Override
	public HadoopPlatform selectHadoopPlatformById(String id) {
	    return hadoopPlatformMapper.selectHadoopPlatformById(id);
	}
	
	/**
     * 查询Hadoop平台模型列表
     * 
     * @param hadoopPlatform Hadoop平台模型信息
     * @return Hadoop平台模型集合
     */
	@Override
	public List<HadoopPlatform> selectHadoopPlatformList(HadoopPlatform hadoopPlatform) {
	    return hadoopPlatformMapper.selectHadoopPlatformList(hadoopPlatform);
	}
	
    /**
     * 新增Hadoop平台模型
     * 
     * @param hadoopPlatform Hadoop平台模型信息
     * @return 结果
     */
	@Override
	public int insertHadoopPlatform(HadoopPlatform hadoopPlatform) {
		//默认设置新增加的集群平台不可用，需进行高级配置
		hadoopPlatform.setIsEnable("0");
	    return hadoopPlatformMapper.insertHadoopPlatform(hadoopPlatform);
	}
	
	/**
     * 修改Hadoop平台模型
     * 
     * @param hadoopPlatform Hadoop平台模型信息
     * @return 结果
     */
	@Override
	public int updateHadoopPlatform(HadoopPlatform hadoopPlatform) {
	    return hadoopPlatformMapper.updateHadoopPlatform(hadoopPlatform);
	}

	/**
     * 删除Hadoop平台模型对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteHadoopPlatformByIds(String ids) {
		return hadoopPlatformMapper.deleteHadoopPlatformByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<Ztree> selectPlatformTree(HadoopPlatform hadoopPlatform) {
		List<HadoopPlatform> hadoopPlatformList = hadoopPlatformMapper.selectHadoopPlatformList(hadoopPlatform);
		return initZtree(hadoopPlatformList);
	}

	@Override
	@Transactional
	public int saveDefaultTenantAndCred(String platformId, String principalHive2, String urlHive2, String principalHcatLog, String urlHcatLog, String tenantId, String tenant, String tenantGroup, HadoopPlatform hadoopPlatform, String principal, String localKeytabPath, String localKrb5ConfPath) {
		try{
			oozieCredentialService.saveOrUpdateCredential(platformId,principalHive2,urlHive2,principalHcatLog,urlHcatLog);
		}catch(Exception e){
			e.printStackTrace();
			log.error("发生异常：" + e.getMessage());
			return 1;
		}

		try {
			hadoopDefaultTenantService.saveOrUpdateHadoopDefaultTenant(tenantId,tenant,tenantGroup,hadoopPlatform,principal,localKeytabPath,localKrb5ConfPath);
		} catch (IOException e) {
			log.error("发生异常：" + e.getMessage());
			return 1;
		}

		return 0;
	}

	public List<Ztree> initZtree(List<HadoopPlatform> hadoopPlatformList) {
		return initZtree(hadoopPlatformList, null);
	}

	/**
	 * 对象转组织树
	 *
	 * @param roleDeptList 角色已存在菜单列表
	 * @return 树结构列表
	 */
	public List<Ztree> initZtree(List<HadoopPlatform> hadoopPlatformList, List<String> roleDeptList) {

		List<Ztree> ztrees = new ArrayList();
		Ztree root = new Ztree();
		root.setId(0L);
		root.setpId(0L);
		root.setTitle("大数据平台");
		root.setName("大数据平台");
		ztrees.add(root);

		boolean isCheck = StringUtils.isNotNull(roleDeptList);
		for (HadoopPlatform hadoopPlatform : hadoopPlatformList) {
			if (UserConstants.PLATFORM_NORMAL.equals(hadoopPlatform.getIsEnable())) {
				Ztree ztree = new Ztree();
				ztree.setId(Long.parseLong(hadoopPlatform.getId()));
				ztree.setpId(0L);
				ztree.setName(hadoopPlatform.getName());
				ztree.setTitle(hadoopPlatform.getName());
				ztrees.add(ztree);
			}
		}
		return ztrees;
	}
	
}
