package com.atomic.hadoop.common.oozie.utils;

import cn.hutool.core.date.DateUtil;
import com.atomic.common.utils.DateUtils;
import com.atomic.hadoop.common.hdfs.HadoopConstants;
import com.atomic.hadoop.common.hdfs.HdfsUtils;
import com.atomic.hadoop.common.oozie.model.CordActionVo;
import com.atomic.hadoop.common.oozie.task.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.oozie.client.*;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/7/2/002 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Slf4j
public class OozieClientUtils {

    private static final String OOZIE_USER_OOZIEWEB = "oozie.user.oozieweb";
    private static String OOZIE_SERVER_URL = "oozie.server.url";
    private static String OOZIE_APP_ROOT_LOCAL = "oozie.app.root.local";
    private static String OOZIE_APP_ROOT_HDFS = "oozie.app.root.hdfs";
    private static final String HADOOP_SECURITY_KERBEROS = "kerberos";
    private static String HIVE_SITE_XML = "hive.site.xml";
    private static String oozieServer;

    private List<WorkflowJob> jobs;

    private List<CoordinatorJob> cjobs = null;

    private List<WorkflowJob> wfjobs;

    private List<BundleJob> bjobs = null;

    private static String oozieUserOozieweb = "atomic";
    private static boolean isAuthentication = true;

    /**
     * 格式化日期，使用东八区
     * @param dateTime
     * @return
     */
    public String formatTime(String dateTime){
        Date date = DateUtil.parse(dateTime);
        String format=DateUtil.format(date,"yyyy-MM-dd HH:mm:ss");
        if (!format.contains("T") || !format.contains("+0800")) {
            format = format.replace(" ", "T").substring(0, format.lastIndexOf(":")) + "+0800";
        }
        return format;
    }

    public String getStartTimeByCycle(String dateTime,String cycleType){
        if (HadoopConstants.CYCLE_DAY.equals(cycleType)){
            Date date = DateUtil.parse(dateTime);
            String format=DateUtil.format(date,"yyyy-MM-dd HH:mm:ss");
            return formatTime(format);
        } else if (HadoopConstants.CYCLE_MONTH.equals(cycleType)){
            String firstDayOfMonth = DateUtils.getFirstDayOfMonth(dateTime);
            Date date = DateUtil.parse(firstDayOfMonth);
            String format=DateUtil.format(date,"yyyy-MM-dd HH:mm:ss");
            return formatTime(format);
        }
        return formatTime(dateTime);
    }

    public String getEndTimeByCycle(String dateTime,String cycleType){
        if (HadoopConstants.CYCLE_DAY.equals(cycleType)){
            return formatTime(dateTime + " 23:59");
        } else if (HadoopConstants.CYCLE_MONTH.equals(cycleType)){
            return formatTime(DateUtils.getLastDayOfMonth(dateTime));
        }
        return formatTime(dateTime.substring(0,10) + " 23:59");
    }

    public static void main(String[] args) {
        String firstDayOfMonth = DateUtils.getFirstDayOfMonth("2019-07-04");
        Date date = DateUtil.parse(firstDayOfMonth);
        String format=DateUtil.format(date,"yyyy-MM-dd HH:mm:ss");
        System.out.println(format);
        OozieClientUtils utils=new OozieClientUtils();
        System.out.println(utils.formatTime("2019-07-14 00:12"));
        System.out.println(utils.getStartTimeByCycle("2019-08-11","D"));
        System.out.println(utils.getStartTimeByCycle("2019-08-11","M"));
        System.out.println(utils.getEndTimeByCycle("2019-07-14","D"));
        System.out.println(utils.getEndTimeByCycle("2019-07-14","M"));
        System.out.println(utils.getEndTimeByCycle("2019-07-14","H"));
    }

    /**
     * 根据作业周期类型的开始时间获取结束时间
     * @param startTime
     * @param cycleType
     * @return
     */
    public String getEndTimeByStartTime(String startTime,String cycleType){


        return null;
    }


    public boolean saveWorkflow(String oozieUser, String flowName, String json, String oozieXml, String hdfsRootPath) {

        return saveApp(oozieUser, flowName, oozieXml, json, "workflow", hdfsRootPath);
    }
    public boolean saveCoordinator(String oozieUser, String coordinatorName, String json, String oozieXml, String hdfsRootPath) {
        return saveApp(oozieUser, coordinatorName, oozieXml, json, "coordinator", hdfsRootPath);
    }

    public boolean saveBundle(String oozieUser, String bundleName, String json, String oozieXml, String hdfsRootPath) {
        return saveApp(oozieUser, bundleName, oozieXml, json, "bundle", hdfsRootPath);
    }

    private boolean saveApp(String oozieUser, String name, String oozieXml, String json, String type, String hdfsRootPath) {
        String hdfsDir = hdfsRootPath+oozieUser + "/" + type + "/" + name;
        String hdfsFilePath = hdfsDir + "/" + type + ".xml";
        HdfsUtils hdfsUtil = new HdfsUtils();
        try {
            hdfsUtil.mkdirs(hdfsDir);
            hdfsUtil.createFile(hdfsFilePath,oozieXml);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void start(final String jobId, final String oozieUser) {
        try {
            UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                try {
                    AbstractOozieTask task = new OozieStartTask(oozieUser, jobId);
                    if (oozieUserOozieweb.equals(oozieUser)) {
                        task.call();
                    } else {
                        AuthOozieClient.doAs(oozieUser, task);
                    }
                } catch (OozieClientException e) {
                    e.printStackTrace();
                }
                return null;
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public Map<String, Object> submitWorkflow(String oozieUser, String flowName, Map<String, String> kvs) {
        return submit(oozieUser, flowName, "workflow", kvs);
    }

    public Map<String, Object> submitCoordinator(String oozieUser, String flowName, Map<String, String> kvs) {
        return submit(oozieUser, flowName, "coordinator", kvs);
    }

    public Map<String, Object> submitBundle(String oozieUser, String flowName, Map<String, String> kvs) {
        return submit(oozieUser, flowName, "bundle", kvs);
    }

    private Map<String, Object> submit(final String oozieUser, final String jobName, final String type, final Map<String, String> kvs) {
        Map<String, Object> messageMap = new HashMap(16);
        try {
            messageMap = UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Map<String, Object>>) () -> {
                Map<String, Object> messageMap1 = new HashMap(16);
                try {
                    AbstractOozieTask task = new OozieSubmitTask(oozieUser, null, jobName, type, kvs);
                    if (oozieUserOozieweb.equals(oozieUser)) {
                        messageMap1 = task.call();
                    } else {
                        messageMap1 = AuthOozieClient.doAs(oozieUser, task);
                    }
                } catch (OozieClientException e) {
                    e.printStackTrace();
                }
                log.info(String.valueOf(messageMap1));
                return messageMap1;
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        return messageMap;
    }

    public Map<String, Object> killWorkflow(String oozieUser, String jobId) {
        return kill(oozieUser, jobId, "workflow");
    }

    public Map<String, Object> killCoordinator(String oozieUser, String jobId) {
        return kill(oozieUser, jobId, "coordinator");
    }

    public Map<String, Object> killBundle(String oozieUser, String jobId) {
        return kill(oozieUser, jobId, "bundle");
    }

    public Map<String, Object> kill(final String oozieUser, final String jobId, final String type) {
        Map<String, Object> messageMap = new HashMap(16);
        try {
            messageMap = UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Map<String, Object>>) () -> {
                Map<String, Object> messageMap1 = new HashMap(16);
                try {
                    AbstractOozieTask task = new OozieKillTask(oozieUser, jobId, type);
                    if (oozieUserOozieweb.equals(oozieUser)) {
                        messageMap1 = task.call();
                    } else {
                        messageMap1 = AuthOozieClient.doAs(oozieUser, task);
                    }
                } catch (OozieClientException e) {
                    e.printStackTrace();
                }
                log.info(String.valueOf(messageMap1));
                return messageMap1;
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return messageMap;
    }

    public Map<String, Object> runWorkflow(String oozieUser, String flowName, Map<String, String> kvs) {
        return run(oozieUser, flowName, "workflow", kvs);
    }

    public Map<String, Object> runCoordinator(String oozieUser, String flowName, Map<String, String> kvs) {
        return run(oozieUser, flowName, "coordinator", kvs);
    }

    public Map<String, Object> runBundle(final String oozieUser, final String flowName, final Map<String, String> kvs) {
        return run(oozieUser, flowName, "bundle", kvs);
    }

    private Map<String, Object> run(final String oozieUser, final String flowName, final String type, final Map<String, String> kvs) {
        Map<String, Object> messageMap = new HashMap(16);
        try {
            System.out.println(oozieUser + "============" + UserGroupInformation.getLoginUser());
            messageMap = UserGroupInformation.getLoginUser().doAs(new PrivilegedExceptionAction<Map<String, Object>>() {
                Map<String, Object> messageMap = new HashMap(16);
                @Override
                public Map<String, Object> run() throws Exception {
                    try {
                        AbstractOozieTask task = new OozieRunTask(oozieUser, flowName, type, kvs);
                        if (oozieUserOozieweb.equals(oozieUser)) {
                            messageMap = task.call();
                        } else {
                            messageMap = AuthOozieClient.doAs(oozieUser, task);
                        }
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    log.info(String.valueOf(messageMap));
                    return messageMap;
                }
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        return messageMap;
    }

    public Map<String, Object> suspendWorkflow(String oozieUser, String jobId) {
        return suspend(oozieUser, jobId, "workflow");
    }

    public Map<String, Object> suspendCoordinator(String oozieUser, String jobId) {
        return suspend(oozieUser, jobId, "coordinator");
    }

    public Map<String, Object> suspendBundle(String oozieUser, String jobId) {
        return suspend(oozieUser, jobId, "bundle");
    }

    public Map<String, Object> suspend(final String oozieUser, final String jobId, final String type) {
        Map<String, Object> messageMap = new HashMap(16);
        try {
            messageMap = UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Map<String, Object>>) () -> {
                Map<String, Object> messageMap1 = new HashMap(16);
                try {
                    AbstractOozieTask task = new OozieSuspendTask(oozieUser, jobId, type);
                    if (oozieUserOozieweb.equals(oozieUser)) {
                        messageMap1 = task.call();
                    } else {
                        messageMap1 = AuthOozieClient.doAs(oozieUser, task);
                    }
                } catch (OozieClientException e) {
                    e.printStackTrace();
                }
                return messageMap1;
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return messageMap;
    }

    public Map<String, Object> resumeWorkflow(String oozieUser, String jobId) {
        return resume(oozieUser, jobId, "workflow");
    }

    public Map<String, Object> resumeCoordinator(String oozieUser, String jobId) {
        return resume(oozieUser, jobId, "coordinator");
    }

    public Map<String, Object> resumeBundle(String oozieUser, String jobId) {
        return resume(oozieUser, jobId, "bundle");
    }

    public Map<String, Object> resume(final String oozieUser, final String jobId, final String type) {
        Map<String, Object> messageMap = new HashMap(16);
        try {
            messageMap = UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Map<String, Object>>) () -> {
                Map<String, Object> messageMap1 = new HashMap(16);
                try {
                    AbstractOozieTask task = new OozieResumeTask(oozieUser, jobId, type);
                    if (oozieUserOozieweb.equals(oozieUser)) {
                        messageMap1 = task.call();
                    } else {
                        messageMap1 = AuthOozieClient.doAs(oozieUser, task);
                    }
                } catch (OozieClientException e) {
                    e.printStackTrace();
                }
                return messageMap1;
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return messageMap;
    }

    public Map<String, Object> rerunWorkflow(String oozieUser, String jobId, String jobName, Map<String, String> kvs, String rerunFlag) {
        return reRun(oozieUser, jobId, jobName, "workflow", kvs, rerunFlag);
    }

    public Map<String, Object> reRunCoordinator(String oozieUser, String jobId, String jobName, Map<String, String> kvs, String rerunFlag) {
        return reRun(oozieUser, jobId, jobName, "coordinator", kvs, rerunFlag);
    }

    public Map<String, Object> reRunBundle(String oozieUser, String jobId, String jobName, Map<String, String> kvs, String rerunFlag) {
        return reRun(oozieUser, jobId, jobName, "bundle", kvs, rerunFlag);
    }

    private Map<String, Object> reRun(final String oozieUser, final String jobId, final String jobName, final String type, final Map<String, String> kvs, final String rerunFlag) {
        Map<String, Object> messageMap = new HashMap(16);
        try {
            messageMap = UserGroupInformation.getLoginUser().doAs(new PrivilegedExceptionAction<Map<String, Object>>() {
                Map<String, Object> messageMap = new HashMap(16);

                @Override
                public Map<String, Object> run() throws Exception {
                    try {
                        AbstractOozieTask task = new OozieReRunTask(oozieUser, jobId, jobName, type, kvs, rerunFlag);
                        if (oozieUserOozieweb.equals(oozieUser)) {
                            messageMap = task.call();
                        } else {
                            messageMap = AuthOozieClient.doAs(oozieUser, task);
                        }
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    log.info(String.valueOf(messageMap));
                    return messageMap;
                }
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return messageMap;
    }

    public String getJobDefinition(final String jobId) {
        final String[] xml = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            try {
                xml[0] = oc.getJobDefinition(jobId);
                log.info("---JobDefinition------" + xml[0]);
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return xml[0];
        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    log.info("----------kill---------------" + jobId);
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        xml[0] = oc.getJobDefinition(jobId);
                        log.info("---JobDefinition------" + xml[0]);
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                        e.getMessage();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                        ee.getMessage();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
                e1.getMessage();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                e1.getMessage();
            }
            return xml[0];
        }
    }

    public WorkflowJob getWorkflowJobInfo(final String jobId) {
        final WorkflowJob[] job = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            try {
                job[0] = oc.getJobInfo(jobId);
                log.info("job------" + job[0]);
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return job[0];
        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    XOozieClient oc = new AuthOozieClient(oozieServer);
                    job[0] = oc.getJobInfo(jobId);
                    log.info("job------" + job[0]);
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return job[0];
        }
    }

    /**
     * @param filter
     *            filter：过滤器，有效的过滤名名称：
     *            name : the workflow application name from the workflow definition. 格式：name=<N>
     *            user : the user that submitted the job. 格式： user=<U>
     *            group : the group for the job. 格式：group=<G>
     *            status : the status of the job. 格式：status=<S>
     *            startcreatedtime : the start of time window in specifying createdtime range filter. 格式：yyyy-MM-dd'T'HH:mm'Z'
     *            endcreatedtime : the end of time window in specifying createdtime range filter. 格式：yyyy-MM-dd'T'HH:mm'Z'
     * @return
     */
    public List<WorkflowJob> getWorkflowJobsInfo(final String filter,String oozieServerUrl) {
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServerUrl);
            try {
                jobs = oc.getJobsInfo(filter);
                log.info("jobs.size------" + jobs.size());
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return jobs;
        } else {
            jobs = null;
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc1 = new AuthOozieClient(oozieServerUrl);
                        jobs = oc1.getJobsInfo(filter);
                        log.info("jobs.size------" + jobs.size());
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return jobs;
        }

    }

    public String getOozieJobDefinition(String jobId,String oozieServerUrl){
        final String[] jobDefinition = {""};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServerUrl);
            try {
                return oc.getJobDefinition(jobId);
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
        }else{
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    XOozieClient oc = new AuthOozieClient(oozieServerUrl);
                    jobDefinition[0] = oc.getJobDefinition(jobId);
                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jobDefinition[0];
    }

    public WorkflowJob getWorkflowJob(String jobId,String oozieServerUrl){
        final WorkflowJob[] job = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServerUrl);
            try {
                job[0] = oc.getJobInfo(jobId);
                log.info("jobs.id------" + job[0].getId());
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return job[0];
        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServerUrl);
                        job[0] = oc.getJobInfo(jobId);
                        log.info("jobs.id------" + job[0].getId());
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return job[0];
        }
    }

    /**
     * @param kvs
     *            name : the workflow application name from the workflow definition. 格式：name=<N>
     *            user : the user that submitted the job. 格式： user=<U>
     *            group : the group for the job. 格式：group=<G>
     *            status : the status of the job. 格式：status=<S>
     *            startcreatedtime : the start of time window in specifying createdtime range filter. 格式：yyyy-MM-dd'T'HH:mm'Z'
     *            endcreatedtime : the end of time window in specifying createdtime range filter. 格式：yyyy-MM-dd'T'HH:mm'Z'
     *
     * @return
     */
    public List<WorkflowJob> getWorkflowJobsInfoByMap(Map<String, String> kvs,String oozieServerUrl) {
        String filter = "";
        for (Map.Entry<String, String> entry : kvs.entrySet()) {
            if ("name".equals(entry.getKey())) {
                filter += ("name=" + entry.getValue());
            } else if ("user".equals(entry.getKey())) {
                filter += (";user=" + entry.getValue());
            } else if ("group".equals(entry.getKey())) {
                filter += ("group=" + entry.getValue());
            } else if ("status".equals(entry.getKey())) {
                filter += ("status=" + entry.getValue());
            } else if ("startcreatedtime".equals(entry.getKey())) {
                filter += ("startcreatedtime=" + entry.getValue());
            } else if ("endcreatedtime".equals(entry.getKey())) {
                filter += ("endcreatedtime=" + entry.getValue());
            }
        }
        return getWorkflowJobsInfo(filter,oozieServerUrl);
    }

    public List<WorkflowJob> getWorkflowJobsInfoByUser(String oozieUser,String oozieServerUrl) {
        String filter = "user=" + oozieUser;
        return getWorkflowJobsInfo(filter,oozieServerUrl);
    }

    public List<WorkflowJob> getWorkflowJobsInfoByName(String workflowName,String oozieServerUrl) {
        String filter = "name=" + workflowName;
        return getWorkflowJobsInfo(filter,oozieServerUrl);
    }

    public List<WorkflowJob> getWorkflowJobsInfoByNameAndUser(String workflowName, String oozieUser,String oozieServerUrl) {
        String filter = "name=" + workflowName + ";user=" + oozieUser;
        return getWorkflowJobsInfo(filter,oozieServerUrl);
    }

    public List<WorkflowJob> getWorkflowJobsInfoByUser(String oozieServerUrl,String oozieUser, int start, int len) {
        String filter = "user=" + oozieUser;
        return getWorkflowJobsInfo(oozieServerUrl,filter, start, len);
    }

    public List<WorkflowJob> getWorkflowJobsInfoByName(String oozieServerUrl,String workflowName, int start, int len) {
        String filter = "name=" + workflowName;
        return getWorkflowJobsInfo(oozieServerUrl,filter, start, len);
    }

    public List<WorkflowJob> getWorkflowJobsInfoByNameAndUser(String oozieServerUrl, String workflowName, String oozieUser, int start, int len) {
        String filter = "name=" + workflowName + ";user=" + oozieUser;
        return getWorkflowJobsInfo(oozieServerUrl,filter, start, len);
    }

    /**
     * @param filter
     *            filter：过滤器
     *            有效的过滤名称如下：
     *            name : the workflow application name from the workflow definition. 格式：name=<N>
     *            user : the user that submitted the job. 格式： user=<U>
     *            group : the group for the job. 格式：group=<G>
     *            status : the status of the job. 格式：status=<S>
     *            startcreatedtime : the start of time window in specifying createdtime range filter. 格式：yyyy-MM-dd'T'HH:mm'Z'
     *            endcreatedtime : the end of time window in specifying createdtime range filter. 格式：yyyy-MM-dd'T'HH:mm'Z'
     * @return
     */
    public List<WorkflowJob> getWorkflowJobsInfo(final String oozieServerUrl, final String filter, final int start, final int len) {
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServerUrl);
            List<WorkflowJob> jobs = null;
            try {
                jobs = oc.getJobsInfo(filter, start, len);
                log.info("jobs.size------" + jobs.size());
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return jobs;
        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServerUrl);
                        jobs = oc.getJobsInfo(filter, start, len);
                        log.info("jobs.size------" + jobs.size());
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return jobs;
        }
    }

    public int queryWorkflowJobsCount(final String filter, final int start, final int len) {

        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            List<WorkflowJob> jobs = null;
            try {
                jobs = oc.getJobsInfo(filter, 1, len);

                log.info("jobs.size------" + jobs.size());
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            if (jobs == null) {
                return 0;
            }
            return jobs.size();
        } else {

            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        jobs = oc.getJobsInfo(filter, 1, len);
                        log.info("jobs.size------" + jobs.size());
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (jobs == null) {
                return 0;
            }
            return jobs.size();
        }

    }

    public List<WorkflowAction> getWorkflowActions(String workflowJobId) {
        WorkflowJob wJob = getWorkflowJobInfo(workflowJobId);
        return wJob.getActions();
    }

    /**
     * @param filter
     *            name: 格式：name=<N>
     *            user: the user that submitted the job. 格式： user=<U>
     *            group: the group for the job. 格式：group=<G>
     *            status: the status of the job. 格式：status=<S>
     *            frequency: the frequency of the Coordinator job. 格式：frequency=<F>
     *            unit: the time unit.
     *            It can take one of the following four values: months, days, hours or minutes.
     *            Time unit should be added only when frequency is specified. 格式：'months', 'days', 'hours' or 'minutes'
     * @param start
     * @param len
     * @return
     */
    public List<CoordinatorJob> getCoordJobsInfo(final String filter, final int start, final int len) {
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            List<CoordinatorJob> jobs = null;
            try {
                jobs = oc.getCoordJobsInfo(filter, start, len);
                log.info("jobs.size------" + jobs.size());
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return jobs;
        } else {
            cjobs = null;
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        cjobs = oc.getCoordJobsInfo(filter, start, len);
                        log.info("jobs.size------" + cjobs.size());
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return cjobs;
        }

    }

    public int queryCordJobCount(final String filter, final int start, final int len) {

        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            List<CoordinatorJob> jobs = null;
            try {
                jobs = oc.getCoordJobsInfo(filter, start, len);
                log.info("jobs.size------" + jobs.size());
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            if (jobs == null) {
                return 0;
            }
            return jobs.size();
        } else {
            cjobs = null;
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {

                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        cjobs = oc.getCoordJobsInfo(filter, start, len);
                        log.info("jobs.size------" + cjobs.size());
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (cjobs == null) {
                return 0;
            }
            return cjobs.size();
        }

    }

    public List<CoordinatorJob> getCoordJobsInfoByUser(String oozieUser, int start, int len) {
        String filter = "user=" + oozieUser;
        return getCoordJobsInfo(filter, start, len);
    }

    public List<CoordinatorJob> getCoordJobsInfoByName(String name, int start, int len) {
        String filter = "name=" + name;
        return getCoordJobsInfo(filter, start, len);
    }

    /**
     * @param kvs
     *            name: the workflow application name from the workflow definition. 格式：name=<N>
     *            user: the user that submitted the job. 格式： user=<U>
     *            group: the group for the job. 格式：group=<G>
     *            status: the status of the job. 格式：status=<S>
     *            frequency: the frequency of the Coordinator job. 格式：frequency=<F>
     *            unit: the time unit.
     *            It can take one of the following four values: months, days, hours or minutes.
     *            Time unit should be added only when frequency is specified. 格式：'months', 'days', 'hours' or 'minutes'
     * @return
     */
    public List<CoordinatorJob> getCoordJobsInfoByMap(Map<String, String> kvs, int start, int len) {
        String filter = "";
        for (Map.Entry<String, String> entry : kvs.entrySet()) {
            if ("name".equals(entry.getKey())) {
                filter += ("name=" + entry.getValue());
            } else if ("user".equals(entry.getKey())) {
                filter += ("user=" + entry.getValue());
            } else if ("group".equals(entry.getKey())) {
                filter += ("group=" + entry.getValue());
            } else if ("status".equals(entry.getKey())) {
                filter += ("status=" + entry.getValue());
            } else if ("frequency".equals(entry.getKey())) {
                filter += ("frequency=" + entry.getValue());
            } else if ("unit".equals(entry.getKey())) {
                filter += ("unit=" + entry.getValue());
            }
        }
        return getCoordJobsInfo(filter, start, len);
    }

    public CoordinatorJob getCoordJobInfo(final String coordJobId) {
        final CoordinatorJob[] cjob = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            try {
                cjob[0] = oc.getCoordJobInfo(coordJobId);
                log.info("job------" + cjob[0]);
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return cjob[0];
        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        cjob[0] = oc.getCoordJobInfo(coordJobId);
                        log.info("job------" + cjob[0]);
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return cjob[0];
        }
    }

    /**
     * @param
     * @param filter
     *            ,过滤CoordinatorJob的action用的 支持两个：status=？以及nominaltime的 >、>=、<、<=
     * @param start
     * @param len
     * @param order
     *            ,"desc"按执行顺序倒排，null为正排
     * @return
     */
    public CoordinatorJob getCoordJobInfo(final String coordJobId, final String filter, final int start, final int len, final String order) {
        final CoordinatorJob[] cjob = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            try {
                cjob[0] = oc.getCoordJobInfo(coordJobId, filter, start, len, order);
                log.info("cjob------" + cjob);
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return cjob[0];
        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        cjob[0] = oc.getCoordJobInfo(coordJobId, filter, start, len, order);
                        log.info("cjob------" + cjob);
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return cjob[0];
        }

    }

    public List<CoordinatorAction> getCoordinatorActions(String coordJobId) {
        CoordinatorJob cJob = getCoordJobInfo(coordJobId);
        return cJob.getActions();
    }

    public List<CoordinatorAction> getCoordinatorActions(String coordJobId, String filter, int start, int len, String order) {
        CoordinatorJob cJob = getCoordJobInfo(coordJobId, filter, start, len, order);
        return cJob.getActions();
    }

    public CoordinatorAction getCoordActionInfo(final String coordActionId) {
        final CoordinatorAction[] caction = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            try {
                caction[0] = oc.getCoordActionInfo(coordActionId);
                log.info("caction------" + caction[0]);

            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return caction[0];
        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        caction[0] = oc.getCoordActionInfo(coordActionId);
                        log.info("caction------" + caction[0]);
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return caction[0];
        }
    }

    public List<WorkflowJob> getWfjobForCoord(final String coordJobId) {
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            try {
                List<WorkflowJob> wfjobs = oc.getWfsForCoordAction(coordJobId);
                return wfjobs;
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            wfjobs = null;
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {

                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        wfjobs = oc.getWfsForCoordAction(coordJobId);
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return wfjobs;
        }

    }

    public WorkflowJob getWfjobForCoordAction(final CoordinatorAction cac) {
        final WorkflowJob[] wjob = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            try {
                String wfjobId = cac.getExternalId();
                wjob[0] = oc.getJobInfo(wfjobId);
                log.info("wjob------" + wjob[0]);
                return wjob[0];
            } catch (OozieClientException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            wjob[0] = null;
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        String wfjobId = cac.getExternalId();
                        wjob[0] = oc.getJobInfo(wfjobId);
                        log.info("wjob------" + wjob[0]);
                        return null;
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return wjob[0];
        }
    }

    /**
     * @param filter
     *            name: the workflow application name from the workflow definition. 格式：name=<N>
     *            user: the user that submitted the job. 格式： user=<U>
     *            group: the group for the job. 格式：group=<G>
     *            status: the status of the job. 格式：status=<S>
     * @param start
     * @param len
     * @return
     */
    public List<BundleJob> getBundleJobsInfo(final String filter, final int start, final int len) {
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            List<BundleJob> bjobs = null;
            try {
                bjobs = oc.getBundleJobsInfo(filter, start, len);
                log.info("bjobs.size------" + bjobs.size());
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return bjobs;
        } else {
            bjobs = null;
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {

                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        bjobs = oc.getBundleJobsInfo(filter, start, len);
                        log.info("bjobs.size------" + bjobs.size());
                        return null;
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return bjobs;
        }

    }

    public List<BundleJob> getBundleJobsInfoByMap(Map<String, String> kvs, int start, int len) {
        String filter = "";
        for (Map.Entry<String, String> entry : kvs.entrySet()) {
            if ("name".equals(entry.getKey())) {
                filter += ("name=" + entry.getValue());
            } else if ("user".equals(entry.getKey())) {
                filter += ("user=" + entry.getValue());
            } else if ("group".equals(entry.getKey())) {
                filter += ("group=" + entry.getValue());
            } else if ("status".equals(entry.getKey())) {
                filter += ("status=" + entry.getValue());
            }
        }
        return getBundleJobsInfo(filter, start, len);
    }

    public BundleJob getBundleJobInfo(final String bundleJobId) {
        final BundleJob[] bjob = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServer);
            try {
                bjob[0] = oc.getBundleJobInfo(bundleJobId);
                log.info("bjob------" + bjob);
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return bjob[0];

        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServer);
                        bjob[0] = oc.getBundleJobInfo(bundleJobId);
                        log.info("bjob------" + bjob[0]);
                        return null;
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return bjob[0];
        }

    }

    public List<CoordinatorJob> getCoordJobsInfoByBundle(String bundleJobId) {
        BundleJob bjob = getBundleJobInfo(bundleJobId);
        return bjob.getCoordinators();
    }

    public WorkflowAction getWorkflowActionByActionId(final String actionId,String oozieServerUrl) {
        final WorkflowAction[] wac = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServerUrl);
            try {
                wac[0] = oc.getWorkflowActionInfo(actionId);
                log.info("waction------" + wac[0]);
            } catch (OozieClientException e) {
                e.printStackTrace();
            }
            return wac[0];
        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    try {
                        XOozieClient oc = new AuthOozieClient(oozieServerUrl);
                        wac[0] = oc.getWorkflowActionInfo(actionId);
                        log.info("waction------" + wac[0]);
                        return null;
                    } catch (OozieClientException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return wac[0];
        }
    }

    public CordActionVo getLastCoordinatorActionByTaskName(final String oozieUser, final String taskName,String oozieServerUrl) {
        final CordActionVo[] cAction = {null};
        if (!isAuthentication) {
            XOozieClient oc = new XOozieClient(oozieServerUrl);
            return getCordActionVo(oozieUser,taskName,oc);
        } else {
            try {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Void>) () -> {
                    XOozieClient oc = new AuthOozieClient(oozieServerUrl);
                    cAction[0]=getCordActionVo(oozieUser,taskName,oc);
                    return null;
                });
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return cAction[0];
        }
    }

    public CordActionVo getCordActionVo(String oozieUser,String taskName,XOozieClient oc){
        final CordActionVo[] cAction = {null};
        try {
            String filter = "name=" + taskName + ";user=" + oozieUser;
            List<CoordinatorJob> coordinatorJobs = oc.getCoordJobsInfo(filter, 0, 10);
            List<CordActionVo> tmp_actions = new ArrayList<CordActionVo>();
            if (coordinatorJobs != null && coordinatorJobs.size() > 0) {
                for (CoordinatorJob coordinatorJob : coordinatorJobs) {
                    CoordinatorJob vo = oc.getCoordJobInfo(coordinatorJob.getId());
                    List<CoordinatorAction> actions = vo.getActions();
                    if (actions != null && actions.size() > 0) {
                        for (CoordinatorAction coordinatorAction : actions) {
                            CordActionVo cordActionVo = new CordActionVo(coordinatorAction, vo);
                            tmp_actions.add(cordActionVo);
                        }
                    }
                }
            }
            if (tmp_actions.size() > 0) {
                ListSortUtil<CordActionVo> utils = new ListSortUtil<CordActionVo>();
                utils.sort(tmp_actions, "nominalTime", "desc");
                cAction[0] = tmp_actions.get(0);
            }
        } catch (OozieClientException e) {
            e.printStackTrace();
        }
        return cAction[0];
    }


}
