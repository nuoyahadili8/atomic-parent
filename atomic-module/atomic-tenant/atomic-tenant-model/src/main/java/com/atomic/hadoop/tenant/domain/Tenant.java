package com.atomic.hadoop.tenant.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Project:
 * @Description: hadoop租户模型
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/4/15/015 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Data
@Builder
public class Tenant {

    /**
     * 租户ID
     */
    private String id;

    /**
     * 租户名
     */
    private String userName;

    /**
     * 租户组
     */
    private String userGroup;

    /**
     * 租户密码
     */
    private String password;

    /**
     * 是否为超级租户管理员
     */
    private String superFlag;

    /**
     * kerberos文件路径
     */
    private String kerberosFile;

    /**
     * Yarn资源队列
     */
    private String yarnQueue;

    /**
     * 是否启用
     */
    private String enable;

    /**
     * 租户所属组织
     */
    private String ownerDept;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 租户资料被创建的用户
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 租户资料被修改的用户
     */
    private String updateUser;
}
