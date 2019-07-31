package com.atomic.hadoop.tenant.service;

import com.atomic.hadoop.tenant.domain.HadoopDefaultTenant;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.util.Map;


/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/4/21/021 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public interface IHadoopService {

    Configuration getHadoopConfiguration(String platformId) throws Exception;

    boolean checkHadoopEnv(String platformId, String configJson, String tenant, String tenantGroup, String principal, String keytabName, String krb5confFullPath) throws Exception;

    Configuration loginHadoop(Integer hadoopPlatformId) throws IOException;

    /**
     * 从数据库default_tenant表中下载hadoop kerberos相关配置文件
     * @param keytabBasePath
     * @param hadoopDefaultTenant
     * @return
     */
    int downLoadHadoopConfigFile(String keytabBasePath, HadoopDefaultTenant hadoopDefaultTenant) throws IOException;

    /**
     * 获取hadoop集群环境配置信息
     * @param hadoopPlatform
     * @param hadoopDefaultTenant
     * @return
     */
    Map<String,String> getHadoopEnv(HadoopPlatform hadoopPlatform, HadoopDefaultTenant hadoopDefaultTenant);
}
