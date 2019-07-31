package com.atomic.hadoop.tenant.service.impl;

import cn.hutool.core.util.StrUtil;
import com.atomic.common.core.text.Convert;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.atomic.hadoop.common.hdfs.HadoopConstants;
import com.atomic.hadoop.tenant.domain.OozieCredential;
import com.atomic.hadoop.tenant.mapper.OozieCredentialMapper;
import com.atomic.hadoop.tenant.service.IOozieCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Oozie安全认证凭据 服务层实现
 *
 * @author atomic
 * @date 2019-05-15
 */
@Service
public class OozieCredentialServiceImpl implements IOozieCredentialService {
    @Autowired
    private OozieCredentialMapper oozieCredentialMapper;

    /**
     * 查询Oozie安全认证凭据信息
     *
     * @param id Oozie安全认证凭据ID
     * @return Oozie安全认证凭据信息
     */
    @Override
    public OozieCredential selectOozieCredentialById(Integer id) {
        return oozieCredentialMapper.selectOozieCredentialById(id);
    }

    /**
     * 查询Oozie安全认证凭据列表
     *
     * @param oozieCredential Oozie安全认证凭据信息
     * @return Oozie安全认证凭据集合
     */
    @Override
    public List<OozieCredential> selectOozieCredentialList(OozieCredential oozieCredential) {
        return oozieCredentialMapper.selectOozieCredentialList(oozieCredential);
    }

    /**
     * 新增Oozie安全认证凭据
     *
     * @param oozieCredential Oozie安全认证凭据信息
     * @return 结果
     */
    @Override
    public int insertOozieCredential(OozieCredential oozieCredential) {
        return oozieCredentialMapper.insertOozieCredential(oozieCredential);
    }

    /**
     * 修改Oozie安全认证凭据
     *
     * @param oozieCredential Oozie安全认证凭据信息
     * @return 结果
     */
    @Override
    public int updateOozieCredential(OozieCredential oozieCredential) {
        return oozieCredentialMapper.updateOozieCredential(oozieCredential);
    }

    /**
     * 删除Oozie安全认证凭据对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOozieCredentialByIds(String ids) {
        return oozieCredentialMapper.deleteOozieCredentialByIds(Convert.toStrArray(ids));
    }

    @Override
    public int deleteOozieCredentialByPlatformId(String platformId) {
        return oozieCredentialMapper.deleteOozieCredentialByPlatformId(platformId);
    }

    @Transactional
    @Override
    public void saveOrUpdateCredential(String platformId, String hive2Principal, String hive2Url, String hcatLogPricipal, String hcatLogUrl) {
        oozieCredentialMapper.deleteOozieCredentialByPlatformId(platformId);
        if (StrUtil.isNotEmpty(hive2Principal) && StrUtil.isNotEmpty(hive2Url)){
            OozieCredential hiveCred = new OozieCredential();
            hiveCred.setPlatformId(Integer.parseInt(platformId));
            hiveCred.setName(HadoopConstants.HIVE_CREDENTIAL_NAME);
            hiveCred.setType(HadoopConstants.HIVE_CREDENTIAL_TYPE);
            hiveCred.setPrincipal(hive2Principal);
            hiveCred.setUrl(hive2Url);
            hiveCred.setCreateTime(new Date());
            hiveCred.setCreateUser(ShiroUtils.getLoginName());
            oozieCredentialMapper.insertOozieCredential(hiveCred);
        }

        if (StrUtil.isNotEmpty(hcatLogPricipal) && StrUtil.isNotEmpty(hcatLogUrl)){
            OozieCredential hcatCred = new OozieCredential();
            hcatCred.setPlatformId(Integer.parseInt(platformId));
            hcatCred.setName(HadoopConstants.HCATLOG_CREDENTIAL_NAME);
            hcatCred.setType(HadoopConstants.HCATLOG_CREDENTIAL_TYPE);
            hcatCred.setPrincipal(hcatLogPricipal);
            hcatCred.setUrl(hcatLogUrl);
            hcatCred.setCreateTime(new Date());
            hcatCred.setCreateUser(ShiroUtils.getLoginName());
            oozieCredentialMapper.insertOozieCredential(hcatCred);
        }

        OozieCredential hbaseCred = new OozieCredential();
        hbaseCred.setPlatformId(Integer.parseInt(platformId));
        hbaseCred.setName(HadoopConstants.HBASE_CREDENTIAL);
        hbaseCred.setType(HadoopConstants.HBASE_CREDENTIAL);
        hbaseCred.setPrincipal(HadoopConstants.HBASE_CREDENTIAL);
        hbaseCred.setUrl(HadoopConstants.HBASE_CREDENTIAL);
        hbaseCred.setCreateUser(ShiroUtils.getLoginName());
        hbaseCred.setCreateTime(new Date());
        oozieCredentialMapper.insertOozieCredential(hbaseCred);
    }

    @Override
    public List<ConfCredentialVo> getPackageCredList(String hadoopPlatformId,String isHiveCred,String isHcatCred,String isHbaseCred) {
        OozieCredential oozieCredential = new OozieCredential();
        oozieCredential.setPlatformId(Integer.parseInt(hadoopPlatformId));
        List<ConfCredentialVo> credList = new ArrayList<>();
        if (HadoopConstants.IS_CRED.equals(isHiveCred)){
            oozieCredential.setType(HadoopConstants.HIVE_CREDENTIAL_TYPE);
            List<OozieCredential> oozieCredentials = selectOozieCredentialList(oozieCredential);
            if (oozieCredentials != null && oozieCredentials.size()>0){
                ConfCredentialVo hiveConfCredVo = new ConfCredentialVo();
                hiveConfCredVo.setType(HadoopConstants.HIVE_CREDENTIAL_TYPE);
                hiveConfCredVo.setPrincipal(oozieCredentials.get(0).getPrincipal());
                hiveConfCredVo.setUrl(oozieCredentials.get(0).getUrl());
                hiveConfCredVo.setName(oozieCredentials.get(0).getName());
                credList.add(hiveConfCredVo);
            }
        }

        if (HadoopConstants.IS_CRED.equals(isHcatCred)){
            oozieCredential.setType(HadoopConstants.HCATLOG_CREDENTIAL_TYPE);
            List<OozieCredential> oozieCredentials = selectOozieCredentialList(oozieCredential);
            if (oozieCredentials != null && oozieCredentials.size()>0){
                ConfCredentialVo hcatCredVo = new ConfCredentialVo();
                hcatCredVo.setType(HadoopConstants.HCATLOG_CREDENTIAL_TYPE);
                hcatCredVo.setPrincipal(oozieCredentials.get(0).getPrincipal());
                hcatCredVo.setUrl(oozieCredentials.get(0).getUrl());
                hcatCredVo.setName(oozieCredentials.get(0).getName());
                credList.add(hcatCredVo);
            }
        }

        if (HadoopConstants.IS_CRED.equals(isHbaseCred)){
            oozieCredential.setType(HadoopConstants.HBASE_CREDENTIAL);
            List<OozieCredential> oozieCredentials = selectOozieCredentialList(oozieCredential);
            if (oozieCredentials != null && oozieCredentials.size()>0){
                ConfCredentialVo hbaseCredVo = new ConfCredentialVo();
                hbaseCredVo.setType(HadoopConstants.HBASE_CREDENTIAL);
                hbaseCredVo.setPrincipal(oozieCredentials.get(0).getPrincipal());
                hbaseCredVo.setUrl(oozieCredentials.get(0).getUrl());
                hbaseCredVo.setName(oozieCredentials.get(0).getName());
                credList.add(hbaseCredVo);
            }
        }
        return credList;
    }

}
