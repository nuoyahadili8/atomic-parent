package com.atomic.hadoop.common.oozie.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.oozie.client.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
public class OozieRunTask extends AbstractOozieTask {
    public OozieRunTask(String oozieUser, String jobId, String jobName, String type, Map<String, String> kvs) {
        super(oozieUser, jobId, jobName, type, kvs);
    }

    public OozieRunTask(final String oozieUser, final String jobName, final String type, final Map<String, String> kvs) {
        super(oozieUser, null, jobName, type, kvs);
    }

    @Override
    public Map<String, Object> call() {
        Map<String, Object> messageMap = new HashMap();

        try {
            XOozieClient xOozieClient = new AuthOozieClient(oozieServer);
            Properties conf = createConf(xOozieClient, oozieUser, jobName, type, kvs);

            jobId = xOozieClient.run(conf);
            if (type.equals("workflow")) {
                WorkflowJob workflowJob = xOozieClient.getJobInfo(jobId);
                log.info("---------job-----------" + workflowJob);
                messageMap.put("resultMessage", workflowJob);
            } else if (type.equals("coordinator")) {
                Job job = xOozieClient.getCoordJobInfo(jobId);
                log.info("---------job-----------" + job);
                messageMap.put("resultMessage", job);
            } else if (type.equals("bundle")) {
                Job job = xOozieClient.getBundleJobInfo(jobId);
                log.info("---------job-----------" + job);
                messageMap.put("resultMessage", job);
            }
            messageMap.put("resultCode", "0");
        } catch (OozieClientException e) {
            e.printStackTrace();
            messageMap.put("resultCode", "1");
            messageMap.put("resultMessage", e.getMessage());
        }
        return messageMap;
    }
}
