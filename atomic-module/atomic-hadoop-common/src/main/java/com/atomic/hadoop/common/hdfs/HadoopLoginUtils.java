package com.atomic.hadoop.common.hdfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.junit.Test;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;

import static org.apache.hadoop.fs.CommonConfigurationKeysPublic.HADOOP_SECURITY_AUTHORIZATION;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/4/15/015 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Slf4j
public class HadoopLoginUtils {


    public HadoopLoginUtils() {
    }

    /**
     * HDFS 认证登录功能
     * @param configMap
     * @param tenant
     * @param principal
     * @param keytabFilePath
     * @return
     * @throws IOException
     */
    public Configuration loginHdfs(Map<String,String> configMap,String tenant,String principal,String keytabFilePath) throws IOException{
        Configuration conf = new Configuration();
        conf.setBoolean(HadoopConstants.HADOOP_DISABLE_CACHE,true);
        for (Map.Entry<String, String> entry: configMap.entrySet()) {
            conf.set(entry.getKey(),entry.getValue());
        }
        //Kerberos认证
        if (HadoopConstants.HADOOP_KERBEROS.equals(configMap.get(HadoopConstants.HADOOP_SECURITY_AUTHENTICATION))){
            UserGroupInformation.setLoginUser(null);
            String kdcRealName = principal.substring(principal.indexOf("@"));
            System.setProperty(HadoopConstants.JAVA_SECURITY_KRB5_CONF_KEY,configMap.get(HadoopConstants.JAVA_SECURITY_KRB5_CONF_KEY));
            conf.set("hadoop.security.authentication", "kerberos");
            conf.set("hadoop.security.authorization", "true");
            conf.set("dfs.namenode.kerberos.principal.pattern", "hdfs/*" + kdcRealName);
            //解决异常：There are 1 datanode(s) running and 1 node(s) are excluded in this operation.
            conf.set("dfs.client.use.datanode.hostname","true");
            // 使用设置的用户登陆
            UserGroupInformation.setConfiguration(conf);
            log.info("before loginUserFromKeytab............AtoimcUser:" + principal);
            UserGroupInformation.loginUserFromKeytab(principal, keytabFilePath);
            log.info("after loginUserFromKeytab............AtoimcUser:" + UserGroupInformation.getCurrentUser());
        } else {
            log.info("No Kerberos before loginUserFromKeytab............AtoimcUser:" + UserGroupInformation.getLoginUser());
            UserGroupInformation.setLoginUser(null);
            System.setProperty(HadoopConstants.HADOOP_USER_NAME_JAVA, tenant);
            System.setProperty(HadoopConstants.USER_NAME, tenant);
            System.setProperty("HADOOP_PROXY_USER",tenant);
            conf.set(HadoopConstants.HADOOP_USER_NAME, tenant);
            conf.set(HadoopConstants.USER_NAME, tenant);
            //解决异常：There are 1 datanode(s) running and 1 node(s) are excluded in this operation.
            conf.set("dfs.client.use.datanode.hostname","true");
            log.info("No Kerberos after loginUserFromKeytab............AtoimcUser:" + UserGroupInformation.getLoginUser());
        }
        return conf;
    }

    /**
     * 判断是否具有代理租户的功能
     * @param conf
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean isProxyUser(Configuration conf) throws IOException, InterruptedException {
        log.info("UserGroupInformation current user name is "+UserGroupInformation.getCurrentUser());
        log.info("UserGroupInformation login user name is "+UserGroupInformation.getCurrentUser());
        UserGroupInformation ugi = UserGroupInformation.createProxyUser("hdfs", UserGroupInformation.getLoginUser());
        ugi.doAs(new PrivilegedExceptionAction<Void>() {
            @Override
            public Void run() throws Exception {
                FileSystem fs = FileSystem.get(conf);
                FileStatus[] fileStatuses = fs.listStatus(new Path("/"));
                for (FileStatus file:fileStatuses){
                    log.info(file.getPath().toString());
                }
                return null;
            }
        });
        return true;
    }


    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        System.setProperty("java.security.krb5.conf", "D:/profile/keytab/3/hadoop-conf/krb5.conf");
        conf.addResource(new Path("file:///D:/profile/keytab/3/hadoop-conf/core-site.xml"));
        conf.addResource(new Path("file:///D:/profile/keytab/3/hadoop-conf/hdfs-site.xml"));
        conf.setBoolean("fs.hdfs.impl.disable.cache", true);
        conf.set("hadoop.security.authentication", "kerberos");
        conf.set("hadoop.security.authorization", "true");
        System.out.println("before..........." + "atomic");
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab("atomic@CLOUDERA", "file:///D:/profile/keytab/3/hadoop-conf/atomic.keytab");
        System.out.println("after..........." + UserGroupInformation.getCurrentUser());
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path("/user");
        boolean isExists=fs.exists(path);
        String str=isExists?"Exists":"Not Exists";
        System.out.println("指定文件或目录"+str);
    }

    @Test
    public void test1() throws IOException {
        Configuration conf = new Configuration();
        System.setProperty("java.security.krb5.conf", "D:/profile/keytab/3/krb5.conf");
        conf.set("fs.defaultFS","hdfs://quickstart.cloudera:8020");
        conf.set("hadoop.security.authentication","kerberos");
        conf.set("hadoop.security.authorization","true");
        conf.set("dfs.namenode.kerberos.principal.pattern", "hdfs/*@CLOUDERA");
        UserGroupInformation.setConfiguration(conf);
        System.out.println("before...........atomic");
        UserGroupInformation.loginUserFromKeytab("atomic@CLOUDERA", "file:///D:/profile/keytab/3/atomic.keytab");
        System.out.println("after..........." + UserGroupInformation.getCurrentUser());
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path("hdfs://quickstart.cloudera:8020/user");
        boolean isExists=fs.exists(path);
        String str=isExists?"Exists":"Not Exists";
        System.out.println("指定文件或目录"+str);
    }

    @Test
    public void test2() throws IOException {
        Configuration conf = new Configuration();
        System.setProperty("java.security.krb5.conf", "D:/profile/keytab/3/krb5.conf");
        conf.set("fs.defaultFS","hdfs://quickstart.cloudera:8020");
        conf.set("hadoop.security.authentication","kerberos");
        conf.set("hadoop.security.authorization","true");
        conf.set("dfs.namenode.kerberos.principal.pattern", "hdfs/*@CLOUDERA");
        conf.set("dfs.client.use.datanode.hostname","true");
        UserGroupInformation.setConfiguration(conf);
        System.out.println("before...........atomic");
        UserGroupInformation.loginUserFromKeytab("atomic@CLOUDERA", "file:///D:/profile/keytab/3/atomic.keytab");
        System.out.println("after..........." + UserGroupInformation.getCurrentUser());
        HdfsUtils utils = new HdfsUtils(conf);
        utils.sendFile("hdfs://quickstart.cloudera:8020/user/atomic/","D:/1111.xlsx");
    }

    @Test
    public void test() throws IOException {
        Configuration conf = new Configuration();
        conf.set("dfs.nameservices","nameservice1");
        System.setProperty("java.security.krb5.conf", "D:/Documents/oozieweb/126-conf/krb5.conf");
        conf.addResource(new Path("file:///D:/Documents/oozieweb/126-conf/core-site.xml"));
        conf.addResource(new Path("file:///D:/Documents/oozieweb/126-conf/hdfs-site.xml"));
        conf.setBoolean("fs.hdfs.impl.disable.cache", true);
        conf.set("hadoop.security.authentication", "kerberos");
        conf.set(HADOOP_SECURITY_AUTHORIZATION, "true");
        System.out.println("before..........." + "oozieweb");
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab("oozieweb@ADSERV.COM", "file:///D:/Documents/oozieweb/126-conf/oozieweb.keytab");
        System.out.println("after..........." + UserGroupInformation.getCurrentUser());
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path("/user");
        boolean isExists=fs.exists(path);
        String str=isExists?"Exists":"Not Exists";
        System.out.println("指定文件或目录"+str);
    }


}
