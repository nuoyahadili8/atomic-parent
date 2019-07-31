package com.atomic.hadoop.tenant.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.atomic.common.utils.StringUtils;
import com.atomic.hadoop.common.hdfs.HadoopConstants;
import com.atomic.hadoop.common.hdfs.HadoopLoginUtils;
import com.atomic.hadoop.tenant.domain.HadoopDefaultTenant;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.mapper.HadoopDefaultTenantMapper;
import com.atomic.hadoop.tenant.mapper.HadoopPlatformMapper;
import com.atomic.hadoop.tenant.service.IHadoopService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
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
@Service
public class HadoopServiceImpl implements IHadoopService {

    @Value("${Atomic.profile}")
    private String keytabBasePath;

    @Autowired
    private HadoopPlatformMapper hadoopPlatformMapper;

    @Autowired
    private HadoopDefaultTenantMapper hadoopDefaultTenantMapper;

    @Override
    public Configuration getHadoopConfiguration(String platformId) throws Exception {
        HadoopPlatform hadoopPlatform = hadoopPlatformMapper.selectHadoopPlatformById(platformId);
        Configuration conf=new Configuration();
        conf.set(HadoopConstants.HADOOP_DEFAULTFS,hadoopPlatform.getDefaultFs());
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path("/user");
        boolean isExists=fs.exists(path);
        String str=isExists?"Exists":"Not Exists";
        System.out.println("指定文件或目录"+str);
        return conf;
    }

    /**
     *
     * @param platformId        集群平台ID
     * @param configJson        hadoop config json配置项
     * @param tenant            租户
     * @param tenantGroup       租户组
     * @param principal         认证凭据
     * @param keytabFullPath    keytab文件绝对路径
     * @param krb5confFullPath  krb5.conf配置文件绝对路径
     * @return
     * @throws Exception
     */
    @Override
    public boolean checkHadoopEnv(String platformId, String configJson, String tenant, String tenantGroup, String principal, String keytabFullPath, String krb5confFullPath) throws Exception{
        Map<String,String> map=new HashMap<>();
        HadoopPlatform hadoopPlatform = hadoopPlatformMapper.selectHadoopPlatformById(platformId);

        map.put(HadoopConstants.HADOOP_DEFAULTFS,hadoopPlatform.getDefaultFs());

        if(HadoopConstants.HAS_KERBEROS.equals(hadoopPlatform.getIsSecurity())){
            map.put(HadoopConstants.HADOOP_SECURITY_AUTHENTICATION,HadoopConstants.HADOOP_KERBEROS);
        }
        map.put(HadoopConstants.HADOOP_USER_NAME,tenant);
        map.put(HadoopConstants.USER_NAME,tenant);
        JSONObject jsonObject = JSONUtil.parseObj(configJson);
        if (jsonObject!=null && jsonObject.size()>0){
            for (Map.Entry entry: jsonObject.entrySet()) {
                map.put(entry.getKey().toString(),entry.getValue().toString());
            }
        }

        if(HadoopConstants.HAS_KERBEROS.equals(hadoopPlatform.getIsSecurity()) && StringUtils.isNotEmpty(principal) && StringUtils.isNotEmpty(keytabFullPath)){
            map.put(HadoopConstants.USERNAME_CLIENT_KEYTAB_PRINCIPAL,principal);
            map.put(HadoopConstants.USERNAME_CLIENT_KEYTAB_FILE,keytabFullPath);
        }

        if(HadoopConstants.HAS_KERBEROS.equals(hadoopPlatform.getIsSecurity()) && StringUtils.isNotEmpty(krb5confFullPath)){
            map.put(HadoopConstants.JAVA_SECURITY_KRB5_CONF_KEY,krb5confFullPath);
        }

        HadoopLoginUtils hadoopLoginUtils=new HadoopLoginUtils();
        Configuration conf=hadoopLoginUtils.loginHdfs(map,tenant,principal,keytabFullPath);
        return hadoopLoginUtils.isProxyUser(conf);
    }

    /**
     * 获取hadoop集群环境配置信息
     * @param hadoopPlatform
     * @param hadoopDefaultTenant
     * @return
     */
    @Override
    public Map<String,String> getHadoopEnv(HadoopPlatform hadoopPlatform, HadoopDefaultTenant hadoopDefaultTenant){
        Map<String,String> map=new HashMap<>(16);
        map.put(HadoopConstants.HADOOP_DEFAULTFS,hadoopPlatform.getDefaultFs());

        if(HadoopConstants.HAS_KERBEROS.equals(hadoopPlatform.getIsSecurity())){
            map.put(HadoopConstants.HADOOP_SECURITY_AUTHENTICATION,HadoopConstants.HADOOP_KERBEROS);
        }
        map.put(HadoopConstants.HADOOP_USER_NAME,hadoopDefaultTenant.getTenant());
        map.put(HadoopConstants.USER_NAME,hadoopDefaultTenant.getTenant());
        if (StringUtils.isNotEmpty(hadoopPlatform.getHadoopConfig())){
            JSONObject jsonObject = JSONUtil.parseObj(hadoopPlatform.getHadoopConfig());
            if (jsonObject!=null && jsonObject.size()>0){
                for (Map.Entry entry: jsonObject.entrySet()) {
                    map.put(entry.getKey().toString(),entry.getValue().toString());
                }
            }
        }

        String keytabFullPath = "file:///" + keytabBasePath + HadoopConstants.DIR_KEYTAB + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getHadoopPlatform().getId() + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getKeytabFileName();
        String principal = hadoopDefaultTenant.getKerberosPrincipal();
        if(HadoopConstants.HAS_KERBEROS.equals(hadoopPlatform.getIsSecurity()) && StringUtils.isNotEmpty(principal) && StringUtils.isNotEmpty(keytabFullPath)){
            map.put(HadoopConstants.USERNAME_CLIENT_KEYTAB_PRINCIPAL,principal);
            map.put(HadoopConstants.USERNAME_CLIENT_KEYTAB_FILE,keytabFullPath);
        }

        String krb5ConfFileFullNamePath = keytabBasePath + HadoopConstants.DIR_KEYTAB + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getHadoopPlatform().getId() + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getKrb5ConfFileName();
        if(HadoopConstants.HAS_KERBEROS.equals(hadoopPlatform.getIsSecurity()) && StringUtils.isNotEmpty(krb5ConfFileFullNamePath)){
            map.put(HadoopConstants.JAVA_SECURITY_KRB5_CONF_KEY,krb5ConfFileFullNamePath);
        }
        return map;
    }

    /**
     * 登录HDFS
     * @param hadoopPlatformId      集群平台ID
     * @return
     */
    @Override
    public Configuration loginHadoop(Integer hadoopPlatformId) throws IOException {
        HadoopPlatform hadoopPlatform = hadoopPlatformMapper.selectHadoopPlatformById(hadoopPlatformId+"");
        HadoopDefaultTenant hadoopDefaultTenant = new HadoopDefaultTenant();
        hadoopDefaultTenant.setPlatformId(hadoopPlatformId);
        Map<String, String> hadoopEnv;
        Configuration conf;
        List<HadoopDefaultTenant> hadoopDefaultTenants = hadoopDefaultTenantMapper.selectHadoopDefaultTenantList(hadoopDefaultTenant);
        if (hadoopDefaultTenants != null && hadoopDefaultTenants.size()>0){
            hadoopDefaultTenant = hadoopDefaultTenants.get(0);
            downLoadHadoopConfigFile(keytabBasePath,hadoopDefaultTenant);
            hadoopEnv = getHadoopEnv(hadoopPlatform, hadoopDefaultTenant);
            HadoopLoginUtils hadoopLoginUtils=new HadoopLoginUtils();
            String keytabFileFullNamePath = keytabBasePath + HadoopConstants.DIR_KEYTAB + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getHadoopPlatform().getId() + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getKeytabFileName();
            conf=hadoopLoginUtils.loginHdfs(hadoopEnv,hadoopDefaultTenant.getTenant(),hadoopDefaultTenant.getKerberosPrincipal(),keytabFileFullNamePath);
            return conf;
        }
        return null;
    }

    /**
     * 从数据库default_tenant表中下载hadoop kerberos相关配置文件
     * @param keytabBasePath
     * @param hadoopDefaultTenant
     * @return
     */
    @Override
    public int downLoadHadoopConfigFile(String keytabBasePath, HadoopDefaultTenant hadoopDefaultTenant) throws IOException {
        byte[] keytabByte=hadoopDefaultTenant.getKeytabFile();
        if (keytabByte != null){
            String keytabFileFullNamePath = keytabBasePath + HadoopConstants.DIR_KEYTAB + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getHadoopPlatform().getId() + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getKeytabFileName();
            File file = new File(keytabFileFullNamePath);
            if (!file.exists()){
                OutputStream out=new FileOutputStream(file);
                out.write(keytabByte);
                out.close();
            }
        }

        byte[] krb5ConfByte=hadoopDefaultTenant.getKrb5ConfFile();
        if (krb5ConfByte != null){
            String krb5ConfFileFullNamePath = keytabBasePath + HadoopConstants.DIR_KEYTAB + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getHadoopPlatform().getId() + HadoopConstants.FILE_SEPARATOR + hadoopDefaultTenant.getKrb5ConfFileName();
            File file = new File(krb5ConfFileFullNamePath);
            if (!file.exists()){
                OutputStream out=new FileOutputStream(file);
                out.write(keytabByte);
                out.close();
            }
        }
        return 0;
    }
}
