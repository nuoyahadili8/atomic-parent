package com.atomic.hadoop.common.oozie.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowApp;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowLink;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwWorkflowApp;
import com.atomic.hadoop.common.oozie.service.IWorkflowService;
import com.atomic.hadoop.common.oozie.utils.OozieValidUtils;
import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.atomic.hadoop.common.oozie.vo.TaskVo;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2018/12/18/018 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Service
public class WorkflowService implements IWorkflowService {
    @Override
    public String workflowJsonToXml(String flowName, String flowJson) {
        FlowApp app = parseFromJson(flowJson);
        TaskVo taskVo=new TaskVo();
        taskVo.setNameEn(flowName);
        OwWorkflowApp workflowApp = new OwWorkflowApp();
        try {
            workflowApp = app.createOwWorkflowApp(taskVo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return workflowApp.toXml();
    }

    @Override
    public String workflowJsonToXml(TaskVo taskVo, String flowJson) {
        FlowApp app = parseFromJson(flowJson);
        OwWorkflowApp workflowApp = new OwWorkflowApp();
        try {
            workflowApp = app.createOwWorkflowApp(taskVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workflowApp.toXml();
    }

    @Override
    public String workflowJsonToXml(String flowName, String flowJson, List<ConfCredentialVo> credList) {
        FlowApp app = parseFromJson(flowJson);
        OwWorkflowApp workflowApp = new OwWorkflowApp();
        try{
            workflowApp = app.createOwWorkflowApp(flowName,credList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return workflowApp.toXml();
    }

    @Override
    public String validJson(String flowName,String flowJson) {
        FlowApp app = parseFromJson(flowJson);
        OwWorkflowApp workflowApp = new OwWorkflowApp();
        TaskVo taskVo=new TaskVo();
        taskVo.setNameEn(flowName);
        try {
            workflowApp = app.createOwWorkflowApp(taskVo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String owVaildStr = workflowApp.valid();
        if (owVaildStr != null){
            return owVaildStr;
        }

        String flowXml = workflowApp.toXml();
        return OozieValidUtils.isValidXml(flowXml);
    }

    @Override
    public List<Integer> getNodeIds(String flowJson){
        JSONObject flowObj = JSON.parseObject(flowJson);
        JSONArray nodeArray = flowObj.getJSONArray("node");
        List<Integer> nodeIds=new ArrayList<>();

        Map<Integer, FlowNode> nodes = new HashMap<>();

        for (int i = 0; i < nodeArray.size(); i++) {
            FlowNode node = JSON.toJavaObject(nodeArray.getJSONObject(i), FlowNode.class);
            nodeIds.add(node.getNodeid());
        }
        return nodeIds;
    }

    private FlowApp parseFromJson(String flowJson) {
        JSONObject flowObj = JSON.parseObject(flowJson);
        JSONArray nodeArray = flowObj.getJSONArray("node");
        JSONArray linkArray = flowObj.getJSONArray("linkarray");

        Map<Integer, FlowNode> nodes = new HashMap<>();
        Set<FlowLink> links = new HashSet<>();

        for (int i = 0; i < nodeArray.size(); i++) {
            FlowNode node = JSON.toJavaObject(nodeArray.getJSONObject(i), FlowNode.class);
            nodes.put(node.getNodeid(), node);
        }
        for (int i = 0; i < linkArray.size(); i++) {
            FlowLink link = JSON.toJavaObject(linkArray.getJSONObject(i), FlowLink.class);
            links.add(link);
        }
        return new FlowApp(nodes, links);
    }

    private FlowApp parseFromJson(String flowJson,List<ConfCredentialVo> credList){
        JSONObject flowObj = JSON.parseObject(flowJson);
        JSONArray nodeArray = flowObj.getJSONArray("node");
        JSONArray linkArray = flowObj.getJSONArray("linkarray");

        Map<Integer, FlowNode> nodes = new HashMap<>();
        Set<FlowLink> links = new HashSet<>();

        for (int i = 0; i < nodeArray.size(); i++) {
            FlowNode node = JSON.toJavaObject(nodeArray.getJSONObject(i), FlowNode.class);
            StringBuffer sb=new StringBuffer();
            for (ConfCredentialVo confCredentialVo:credList){
                sb.append(confCredentialVo.getName()+",");
            }
            String credStr=sb.toString();
            node.setCred(credStr.substring(0,credStr.lastIndexOf(",")));
            nodes.put(node.getNodeid(), node);
        }
        for (int i = 0; i < linkArray.size(); i++) {
            FlowLink link = JSON.toJavaObject(linkArray.getJSONObject(i), FlowLink.class);
            links.add(link);
        }
        return new FlowApp(nodes, links);
    }
}
