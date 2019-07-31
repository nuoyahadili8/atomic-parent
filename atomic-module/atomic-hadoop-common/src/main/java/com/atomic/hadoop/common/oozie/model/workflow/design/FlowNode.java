package com.atomic.hadoop.common.oozie.model.workflow.design;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.model.*;
import com.atomic.hadoop.common.oozie.model.workflow.model.action.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FlowNode {

	public int x;
	public int y;

	private String retryMax;
	private String retryInt;
	private String cred;

	private String type;
	private String text;
	private int nodeid;
	private Object attribute;
	private List<String> to = new ArrayList<String>();

	public void addTo(String to) {
		this.to.add(to);
	}

	public FlowNode() {
	}

	public OwChoiceNode convertTo() {
		OwAction ac = null;
		switch (this.getType()) {
		case "1":
			ac = new OwAction(this);
			OwFs fs = new OwFs(this);
			ac.setFs(fs);
			return ac;
		case "2":
			ac = new OwAction(this);
			OwJava java = new OwJava(this);
			ac.setJava(java);
			break;
		case "3":
			ac = new OwAction(this);
			OwMapReduce mapreduce = new OwMapReduce(this);
			ac.setMap_reduce(mapreduce);
			break;
		case "4":
			return new OwDecision(this);
		case "5":
			return new OwFork(this);
		case "6":
			return new OwJoin(this);
		case "7":
			ac = new OwAction(this);
			OwEmail email = new OwEmail(this);
			ac.setEmail(email);
			break;
		case "8":
			JSONObject attributeJson = JSON.parseObject(this.attribute.toString());
			String type = attributeJson.getString("type");

			ac = new OwAction(this);
			if ("ftp".equalsIgnoreCase(type)) {
				OwFtpNew ftpNew = new OwFtpNew(this);
				ac.setFtpNew(ftpNew);
			} else if ("sftp".equalsIgnoreCase(type)) {
				OwSftp sftp = new OwSftp(this);
				ac.setSftp(sftp);
			}
			break;
		case "9":
			ac = new OwAction(this);
			OwHive hive = new OwHive(this);
			ac.setHive(hive);
			break;
		case "10":
			ac = new OwAction(this);
			OwSqoop sqoop = new OwSqoop(this);
			ac.setSqoop(sqoop);
			break;
		case "11":
			break;
		case "13":
			ac = new OwAction(this);
			OwShell shell = new OwShell(this);
			ac.setShell(shell);
			break;
		case "14":
			ac = new OwAction(this);
			OwSsh ssh = new OwSsh(this);
			ac.setSsh(ssh);
			break;
		case "15":
			if (this.cred == null || this.cred.isEmpty()) {
//				if (flowVo.getConHive2Vo() != null) {
//					this.cred = flowVo.getConHive2Vo().getName();
//				}
			}
			ac = new OwAction(this);
			OwHive2 hive2 = new OwHive2(this);
			ac.setHive2(hive2);
			break;
		case "impala":
			if (this.cred == null || this.cred.isEmpty()) {
//				if (flowVo.getConHive2Vo() != null) {
//					this.cred = flowVo.getConHive2Vo().getName();
//				}
			}
			ac = new OwAction(this);
			OwImpala impala = new OwImpala(this);
			ac.setImpala(impala);
			break;
		case "gbase":
			ac = new OwAction(this);
			OwGbase gbase = new OwGbase(this);
			ac.setGbase(gbase);
			break;
		case "16":
			ac = new OwAction(this);
			OwDistcp distcp = new OwDistcp(this);
			ac.setDistcp(distcp);
			break;
		case "17":
			ac = new OwAction(this);
			OwSpark spark = new OwSpark(this);
			ac.setSpark(spark);
			break;
		case "31":
			ac = new OwAction(this);
			OwSubflow sub = new OwSubflow(this);
			ac.setSubflow(sub);
			break;
		case "33":
			ac = new OwAction(this);
			OwWash wash = new OwWash(this);
			ac.setWash(wash);
			break;
		case "34":
			ac = new OwAction(this);
			OwEsLoad esload = new OwEsLoad(this);
			ac.setEs(esload);
			break;

		default:

		}

		if (this.retryMax != null && StringUtils.isNumeric(this.retryMax)) {
			ac.setRetry_max(this.retryMax);
			ac.setRetry_interval(this.retryInt);
		}
		if (this.cred != null) {
			ac.setCred(cred);
		}
		return ac;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getRetryMax() {
		return retryMax;
	}

	public void setRetryMax(String retryMax) {
		this.retryMax = retryMax;
	}

	public String getRetryInt() {
		return retryInt;
	}

	public void setRetryInt(String retryInt) {
		this.retryInt = retryInt;
	}

	public String getCred() {
		return cred;
	}

	public void setCred(String cred) {
		this.cred = cred;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getNodeid() {
		return nodeid;
	}

	public void setNodeid(int nodeid) {
		this.nodeid = nodeid;
	}

	public Object getAttribute() {
		return attribute;
	}

	public void setAttribute(Object attribute) {
		this.attribute = attribute;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}
}
