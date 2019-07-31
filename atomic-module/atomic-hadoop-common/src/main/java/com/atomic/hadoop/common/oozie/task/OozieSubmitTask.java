package com.atomic.hadoop.common.oozie.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.apache.oozie.client.AuthOozieClient;
import org.apache.oozie.client.Job;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.apache.oozie.client.XOozieClient;

@Slf4j
public class OozieSubmitTask extends AbstractOozieTask {

	public OozieSubmitTask(final String oozieUser, final String jobId, final String jobName, final String type, final Map<String, String> kvs) {
		super(oozieUser, jobId, jobName, type, kvs);
	}

	@Override
	public Map<String, Object> call() {
		Map<String, Object> messageMap = new HashMap<String, Object>();
		try {
			XOozieClient xOozieClient = new AuthOozieClient(oozieServer);
			Properties conf = createConf(xOozieClient, oozieUser, jobName, type, kvs);
			jobId = xOozieClient.submit(conf);

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
