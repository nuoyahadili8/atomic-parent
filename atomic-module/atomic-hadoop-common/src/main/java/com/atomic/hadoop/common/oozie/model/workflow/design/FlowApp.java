package com.atomic.hadoop.common.oozie.model.workflow.design;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.model.*;
import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.atomic.hadoop.common.oozie.vo.TaskVo;

import java.util.*;

public class FlowApp {
	private Map<String, FlowNode> flowNodes;
	private Map<Integer, FlowNode> nodes;
	private Set<FlowLink> links;

	public FlowApp(OwWorkflowApp wkApp) {
		flowNodes = new HashMap<String, FlowNode>();
		OwStart start = wkApp.getStart();
		FlowNode sfn = start.toFlowNode();
		flowNodes.put("start", sfn);

		OwEnd end = wkApp.getEnd();
		FlowNode efn = end.toFlowNode();
		flowNodes.put("end", efn);

		List<OwAction> actions = wkApp.getActions();
		if (actions != null) {
			for (OwAction ac : actions) {
				FlowNode acfn = ac.toFlowNode();
				flowNodes.put(acfn.getText(), acfn);
			}
		}
		List<OwDecision> decs = wkApp.getDecisions();
		if (decs != null) {
			for (OwDecision dc : decs) {
				FlowNode dcfn = dc.toFlowNode();
				flowNodes.put(dcfn.getText(), dcfn);
			}
		}
		List<OwFork> forks = wkApp.getForks();
		if (forks != null) {
			for (OwFork fc : forks) {
				FlowNode fcfn = fc.toFlowNode();
				flowNodes.put(fcfn.getText(), fcfn);
			}
		}
		List<OwJoin> joins = wkApp.getJoins();
		if (joins != null) {
			for (OwJoin join : joins) {
				FlowNode joinfn = join.toFlowNode();
				flowNodes.put(joinfn.getText(), joinfn);
			}
		}

		nodes = new HashMap<Integer, FlowNode>(flowNodes.size());
		nodes.put(1, sfn);
		links = new HashSet<FlowLink>();

		// 开始节点的位置设置为20，20
		sfn.x = 20;
		sfn.y = 20;
		createPos(sfn, 2);
	}

	/**
	 * 递归设置后续节点位置与nodeid，同时生成nodes与links
	 *
	 */
	private int createPos(FlowNode fn, int num) {

		int XX = fn.x + 100;
		int YY = fn.y + 50;
		int node2id;
		if (fn.getTo() != null){
			for (String next : fn.getTo()) {
				FlowNode nextF = this.flowNodes.get(next);

				// 位置判断
				if (nextF.x == 0) {
					nextF.x = XX;
					nextF.y = YY;
				}

				// 如果next已经存在，只连线
				if (nextF.getNodeid() > 0) {
					node2id = nextF.getNodeid();
				} else { // 如果不存在，增加点，并连线,而且向下递归
					num++;
					node2id = num;
					nextF.setNodeid(node2id);
					num = createPos(nextF, num);
				}

				// 不论怎么再放一次节点
				this.nodes.put(node2id, nextF);

				FlowLink link = new FlowLink();
				link.setNode1id(fn.getNodeid());
				link.setNode2id(node2id);
				this.links.add(link);

				YY += 100;
			}
		}
		return num;
	}

	public String toFlowJson() {
		JSONObject attObj = new JSONObject();
		JSONArray nodeArr = new JSONArray();
		for (FlowNode node : this.nodes.values()) {
			nodeArr.add(JSON.toJSON(node));
		}
		JSONArray linkArr = new JSONArray();
		for (FlowLink link : this.links) {
			linkArr.add(JSON.toJSON(link));
		}
		attObj.put("node", nodeArr);
		attObj.put("linkarray", linkArr);
		String jsonStr = attObj.toJSONString();
		return jsonStr;
	}

	public FlowApp(Map<Integer, FlowNode> nodes, Set<FlowLink> links) {
		super();

		for (FlowLink link : links) {
			int node1id = link.getNode1id();
			int node2id = link.getNode2id();
			nodes.get(node1id).addTo(nodes.get(node2id).getText());
		}
		flowNodes = new HashMap<String, FlowNode>();
		for (FlowNode node : nodes.values()) {
			flowNodes.put(node.getText(), node);
		}
	}

	public OwWorkflowApp createOwWorkflowApp(TaskVo flowVo) {
		OwWorkflowApp wkApp = new OwWorkflowApp(flowVo);

		OwEnd owEnd = new OwEnd();
		FlowNode end = flowNodes.get("end");
		owEnd.setName(end.getText());
		OwStart owStart = new OwStart();
		FlowNode start = flowNodes.get("start");
		owStart.setTo(start.getTo().get(0));

		wkApp.setEnd(owEnd);
		wkApp.setStart(owStart);
		Set<String> tmpTos = new HashSet();
		tmpTos.add("end");

		findNextNodes(wkApp, tmpTos, start, flowVo);

		return wkApp;
	}

	public OwWorkflowApp createOwWorkflowApp(String workflowNameEn,List<ConfCredentialVo> confCredentialVos){
		OwWorkflowApp wkApp = new OwWorkflowApp(workflowNameEn,confCredentialVos);

		OwEnd owEnd = new OwEnd();
		FlowNode end = flowNodes.get("end");
		owEnd.setName(end.getText());
		OwStart owStart = new OwStart();
		FlowNode start = flowNodes.get("start");
		owStart.setTo(start.getTo().get(0));

		wkApp.setEnd(owEnd);
		wkApp.setStart(owStart);
		Set<String> tmpTos = new HashSet();
		tmpTos.add("end");

		findNextNodes(wkApp, tmpTos, start, null);

		return wkApp;
	}

	private void findNextNodes(OwWorkflowApp wkApp, Set<String> tmpTos, FlowNode currentNode, TaskVo flowVo) {
		List<String> tos = currentNode.getTo();
		for (String to : tos) {
			if (tmpTos.contains(to)) {
				continue;
			}
			tmpTos.add(to);

			FlowNode nextNode = flowNodes.get(to);
			OwChoiceNode choiceNode = nextNode.convertTo();
			if (choiceNode instanceof OwDecision) {
				wkApp.getDecisions().add((OwDecision) choiceNode);
			} else if (choiceNode instanceof OwFork) {
				wkApp.getForks().add((OwFork) choiceNode);
			} else if (choiceNode instanceof OwJoin) {
				wkApp.getJoins().add((OwJoin) choiceNode);
			} else if (choiceNode instanceof OwKill) {
				wkApp.getKills().add((OwKill) choiceNode);
			} else if (choiceNode instanceof OwAction) {
				wkApp.getActions().add((OwAction) choiceNode);
			}

			findNextNodes(wkApp, tmpTos, nextNode, flowVo);
		}
	}
}
