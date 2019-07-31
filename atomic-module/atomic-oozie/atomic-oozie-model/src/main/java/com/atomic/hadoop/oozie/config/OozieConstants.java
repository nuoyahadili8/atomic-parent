package com.atomic.hadoop.oozie.config;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/5/17/017 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public interface OozieConstants {

    /** 任务包状态 */
    /** 新建 */
    String PACKAGE_STATUS_NEW = "1";

    /** 已发布 */
    String PACKAGE_STATUS_ONLINE = "2";

    /** 检出 */
    String PACKAGE_STATUS_OUTEDIT = "3";

    /** * 下线 */
    String PACKAGE_STATUS_OFFLINE = "4";

    String IS_CRED = "on";

    String WORKFLOW = "workflow";

    String WORKFLOW_XML = "workflow.xml";

    String COORDINATOR = "coordinator";

    String COORDINATOR_XML = "coordinator.xml";

    String BUNDLE = "bundle";

    String BUNDLE_XML = "bundle.xml";
}
