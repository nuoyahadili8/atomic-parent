package com.atomic.hadoop.tenant.service;

import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.atomic.hadoop.tenant.domain.OozieCredential;

import java.util.List;

/**
 * Oozie安全认证凭据 服务层
 *
 * @author atomic
 * @date 2019-05-15
 */
public interface IOozieCredentialService {
    /**
     * 查询Oozie安全认证凭据信息
     *
     * @param id Oozie安全认证凭据ID
     * @return Oozie安全认证凭据信息
     */
    OozieCredential selectOozieCredentialById(Integer id);

    /**
     * 查询Oozie安全认证凭据列表
     *
     * @param oozieCredential Oozie安全认证凭据信息
     * @return Oozie安全认证凭据集合
     */
    List<OozieCredential> selectOozieCredentialList(OozieCredential oozieCredential);

    /**
     * 新增Oozie安全认证凭据
     *
     * @param oozieCredential Oozie安全认证凭据信息
     * @return 结果
     */
    int insertOozieCredential(OozieCredential oozieCredential);

    /**
     * 修改Oozie安全认证凭据
     *
     * @param oozieCredential Oozie安全认证凭据信息
     * @return 结果
     */
    int updateOozieCredential(OozieCredential oozieCredential);

    /**
     * 删除Oozie安全认证凭据信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteOozieCredentialByIds(String ids);

    /**
     * 按系统平台删除
     *
     * @param platformId
     * @return
     */
    int deleteOozieCredentialByPlatformId(String platformId);

    void saveOrUpdateCredential(String platformId, String hive2Principal, String hive2Url, String hcatLogPricipal, String hcatLogUrl);

    /**
     * 获取包下的安全凭证
     * @param hadoopPlatformId
     * @param isHiveCred
     * @param isHcatCred
     * @param isHbaseCred
     * @return
     */
    List<ConfCredentialVo> getPackageCredList(String hadoopPlatformId, String isHiveCred, String isHcatCred, String isHbaseCred);
}
