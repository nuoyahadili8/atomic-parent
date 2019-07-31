package com.atomic.hadoop.oozie.service;

import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.oozie.domain.OozieJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.oozie.client.WorkflowAction;

import java.io.IOException;
import java.util.List;

/**
 * 作业 服务层
 * 
 * @author atomic
 * @date 2019-05-20
 */
public interface IOozieJobService {
	/**
     * 查询作业信息
     * 
     * @param jobId 作业ID
     * @return 作业信息
     */
	OozieJob selectOozieJobById(String jobId);
	
	/**
     * 查询作业列表
     * 
     * @param oozieJob 作业信息
     * @return 作业集合
     */
	List<OozieJob> selectOozieJobList(OozieJob oozieJob);

	/**
	 * 查询除自己以为的作业
	 * @param oozieJob
	 * @return
	 */
	List<OozieJob> queryDepedencyJobList(OozieJob oozieJob);
	
	/**
     * 新增作业
     * 
     * @param oozieJob 作业信息
     * @return 结果
     */
	int insertOozieJob(OozieJob oozieJob);
	
	/**
     * 修改作业
     * 
     * @param oozieJob 作业信息
     * @return 结果
     */
	int updateOozieJob(OozieJob oozieJob);
		
	/**
     * 删除作业信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteOozieJobByIds(String ids);

	String getCoordinatorXml(OozieJob oozieJob);

	void upLoadToHdfs(Configuration configuration,OozieJob oozieJob) throws IOException, InterruptedException;

	String getFlowJsonFromXml(String flowXml);

	JSONObject getJobRunFlowJson(String workflowId,OozieJob oozieJob);

	WorkflowAction getWorkflowActionByActionId(String actionId, String oozieServerUrl);
	
}
