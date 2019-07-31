package com.atomic.scheduler.constant;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/12/012 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public interface QuartzConstants {

    /**
     * 查询指定的hadoop平台信息
     */
    String QUERY_HADOOP_PLATFORM_SQL = "select id,name,hadoop_version,default_fS,hadoop_config,rm_url,hs_url,oozie_url,is_security,is_enable,create_user,create_time from hadoop_platform where id=?";

    /**
     * 查询指定的oozie有效job
     */
    String QUERY_OOZIE_JOB_WITH_PLATFORM = "select job_id,project_id,subsystem_id,name_en,name_cn,cycle,dispatch,time_device,strategy,priority,time_window,enable,package_id,nominal_time,status,parameter,note,online_time,online_user,coordinator_xml from oozie_job where enable='0' and strategy=? ";
}
