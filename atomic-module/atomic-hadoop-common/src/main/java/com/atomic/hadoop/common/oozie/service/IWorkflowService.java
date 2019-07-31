package com.atomic.hadoop.common.oozie.service;

import com.atomic.hadoop.common.oozie.model.workflow.design.FlowApp;
import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.atomic.hadoop.common.oozie.vo.TaskVo;

import java.util.List;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2018/12/18/018 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public interface IWorkflowService {

    String workflowJsonToXml(String flowName, String flowJson);

    String validJson(String flowName, String flowJson);

    @Deprecated
    String workflowJsonToXml(TaskVo taskVo, String flowJson);

    String workflowJsonToXml(String flowName, String flowJson, List<ConfCredentialVo> credList);

    List<Integer> getNodeIds(String flowJson);


}
