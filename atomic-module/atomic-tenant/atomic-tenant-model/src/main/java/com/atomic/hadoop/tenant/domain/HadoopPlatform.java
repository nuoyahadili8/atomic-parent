package com.atomic.hadoop.tenant.domain;


import com.atomic.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Hadoop平台模型表 hadoop_platform
 *
 * @author atomic
 * @date 2019-04-19
 */
@Data
public class HadoopPlatform extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String id;
    /**
     * Hadoop平台名称
     */
    private String name;
    /**
     * Hadoop版本号
     */
    private String hadoopVersion;
    /**
     * 默认HDFS协议串
     */
    private String defaultFs;
    /**
     * jobTracker地址
     */
    private String jobTracker;
    /**
     * Hadoop Conf配置信息（Json串）
     */
    private String hadoopConfig;
    /**
     * Resource Manager URL
     */
    private String rmUrl;
    /**
     * History Server URL
     */
    private String hsUrl;
    /**
     * Hive JDBC URL
     */
    private String hiveJdbcUrl;
    /**
     * impala Server URL
     */
    private String impalaUrl;
    /**
     * Oozie server URL
     */
    private String oozieUrl;
    /**
     * Oozie Server mysql url地址
     */
    private String oozieMysqlUrl;
    /**
     * Oozie Server mysql 用户名
     */
    private String oozieMysqlUser;
    /**
     * Oozie Server mysql 密码
     */
    private String oozieMysqlPasswd;
    /**
     * 是否为开启kerberos认证:1开启kerberos认证，0未开启
     */
    private String isSecurity;
    /**
     * Hadoop平台是否启用
     */
    private String isEnable;
    /**
     * 创建者
     */
    private String createUser;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新者
     */
    private String updateUser;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
