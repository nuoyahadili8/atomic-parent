package com.atomic.hadoop.common.oozie.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.XOozieClient;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/7/2/002 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public abstract class AbstractOozieTask implements Callable<Map<String, Object>> {
    private static String OOZIE_SERVER_URL = "oozie.server.url";
    private static String HADOOP_NAMENODE = "hadoop.nameNode";
    private static String HADOOP_JOBTRACKER = "hadoop.jobTracker";
    private static String HADOOP_QUEUENAME = "hadoop.queueName";
    private static String OOZIE_APP_ROOT_HDFS = "oozie.app.root.hdfs";
    private static String OOZIE_libpath = "oozie.libpath";

    private static String nameNode;
    private static String jobTracker;
    private static String queuename;

    private static String oozieAppRootHdfs;
    private static String oozieLibPath;

    private static final String USER_NAME = "\\$\\{HadoopUsername_Oozie\\}";

    protected String oozieUser;
    protected String jobId;
    protected String jobName;
    protected String type;
    protected String oozieServer;
    protected Map<String, String> kvs;

    public AbstractOozieTask(String oozieUser, String jobId, String jobName, String type, Map<String, String> kvs) {
        super();
        this.oozieUser = oozieUser;
        this.jobId = jobId;
        this.jobName = jobName;
        this.type = type;
        this.kvs = kvs;
        this.oozieServer = kvs.get("oozie.server");
    }

    protected Properties createConf(XOozieClient xOozieClient, String oozieUser, String flowName, String type, Map<String, String> kvs) {
        Properties conf = xOozieClient.createConfiguration();
        if (kvs != null && !kvs.isEmpty()) {
            conf.putAll(kvs);
        }
        return conf;
    }
}
