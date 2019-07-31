package com.atomic.hadoop.tenant.service.impl;

import cn.hutool.core.util.StrUtil;
import com.atomic.common.core.text.Convert;
import com.atomic.hadoop.tenant.domain.HadoopDefaultTenant;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.mapper.HadoopDefaultTenantMapper;
import com.atomic.hadoop.tenant.service.IHadoopDefaultTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;

/**
 * Hadoop集群代理租户 服务层实现
 * 
 * @author atomic
 * @date 2019-04-21
 */
@Service
public class HadoopDefaultTenantServiceImpl implements IHadoopDefaultTenantService {
	@Autowired
	private HadoopDefaultTenantMapper hadoopDefaultTenantMapper;

	/**
     * 查询Hadoop集群代理租户信息
     * 
     * @param id Hadoop集群代理租户ID
     * @return Hadoop集群代理租户信息
     */
    @Override
	public HadoopDefaultTenant selectHadoopDefaultTenantById(Integer id) {
	    return hadoopDefaultTenantMapper.selectHadoopDefaultTenantById(id);
	}
	
	/**
     * 查询Hadoop集群代理租户列表
     * 
     * @param hadoopDefaultTenant Hadoop集群代理租户信息
     * @return Hadoop集群代理租户集合
     */
	@Override
	public List<HadoopDefaultTenant> selectHadoopDefaultTenantList(HadoopDefaultTenant hadoopDefaultTenant) {
	    return hadoopDefaultTenantMapper.selectHadoopDefaultTenantList(hadoopDefaultTenant);
	}
	
    /**
     * 新增Hadoop集群代理租户
     * 
     * @param hadoopDefaultTenant Hadoop集群代理租户信息
     * @return 结果
     */
	@Override
	public int insertHadoopDefaultTenant(HadoopDefaultTenant hadoopDefaultTenant) {
	    return hadoopDefaultTenantMapper.insertHadoopDefaultTenant(hadoopDefaultTenant);
	}
	
	/**
     * 修改Hadoop集群代理租户
     * 
     * @param hadoopDefaultTenant Hadoop集群代理租户信息
     * @return 结果
     */
	@Override
	public int updateHadoopDefaultTenant(HadoopDefaultTenant hadoopDefaultTenant) {
	    return hadoopDefaultTenantMapper.updateHadoopDefaultTenant(hadoopDefaultTenant);
	}

	/**
     * 删除Hadoop集群代理租户对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteHadoopDefaultTenantByIds(String ids) {
		return hadoopDefaultTenantMapper.deleteHadoopDefaultTenantByIds(Convert.toStrArray(ids));
	}

	@Override
	@Transactional
	public int saveOrUpdateHadoopDefaultTenant(String tenantId, String tenant, String tenantGroup, HadoopPlatform hadoopPlatform, String pricipal, String keytabFilePath, String localKrb5ConfPath) throws IOException {
		HadoopDefaultTenant defaultTenant = new HadoopDefaultTenant();
		defaultTenant.setTenant(tenant);
		defaultTenant.setTenantGroup(tenantGroup);
		defaultTenant.setPlatformId(Integer.parseInt(hadoopPlatform.getId()));
		defaultTenant.setKerberosPrincipal(pricipal);
		defaultTenant.setHadoopPlatform(hadoopPlatform);

		File keytabFile = new File(keytabFilePath);
		if (keytabFile.exists()){
			InputStream is = new FileInputStream(keytabFile);
			byte[] keytabByte = new byte[is.available()];
			is.read(keytabByte);
			is.close();
			defaultTenant.setKeytabFile(keytabByte);
			defaultTenant.setKeytabFileName(keytabFile.getName());
		}

		File krb5ConfFile = new File(localKrb5ConfPath);
		if (krb5ConfFile.exists()){
			InputStream is = new FileInputStream(krb5ConfFile);
			byte[] krb5ConfByte = new byte[is.available()];
			is.read(krb5ConfByte);
			is.close();
			defaultTenant.setKrb5ConfFileName(krb5ConfFile.getName());
			defaultTenant.setKrb5ConfFile(krb5ConfByte);
		}

		if(StrUtil.isNotEmpty(tenantId)){
			defaultTenant.setId(Integer.parseInt(tenantId));
			return updateHadoopDefaultTenant(defaultTenant);
		}else{
			return insertHadoopDefaultTenant(defaultTenant);
		}
	}

	@Override
	public HadoopDefaultTenant selectHadoopDefaultTenantByPlatformId(Integer platformId) {
		return hadoopDefaultTenantMapper.selectHadoopDefaultTenantByPlatformId(platformId);
	}

}
