package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.action.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("action")
public class OwAction implements OwChoiceNode {
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String cred;
	@XStreamAsAttribute
	@XStreamAlias("retry-max")
	private String retry_max;
	@XStreamAsAttribute
	@XStreamAlias("retry-interval")
	private String retry_interval;

	@XStreamAlias("sub-workflow")
	private OwSubflow subflow;
	private OwEmail email;
	private OwFtp ftp;
	private OwFtpNew ftpNew;
	private OwSftp sftp;
	private OwHive hive;
	private OwHive2 hive2;
	private OwImpala impala;
	private OwGbase gbase;
	@XStreamAlias("map-reduce")
	private OwMapReduce map_reduce;
	private OwSpark spark;
	private OwSqoop sqoop;
	private OwShell shell;
	private OwSsh ssh;
	private OwFs fs;
	private OwJava java;
	private OwDistcp distcp;
	private OwWash wash;
	private OwEsLoad es;
	private OwActionTransition ok;
	private OwActionTransition error;
	private String actionType;

	public OwAction(FlowNode flowNode) {
		name = flowNode.getText();
		ok = new OwActionTransition();
		ok.setTo(flowNode.getTo().get(0));
		error = new OwActionTransition();
		error.setTo("fail");
		this.retry_max = flowNode.getRetryInt();
		this.retry_interval = flowNode.getRetryInt();
		this.cred = flowNode.getCred();
	}

	public OwAction() {
		error = new OwActionTransition();
		error.setTo("fail");
	}

	public static List<OwAction> parseXml(List<Element> aElms) {
		List<OwAction> actions = new ArrayList<OwAction>();
		for (Element elm : aElms) {
			OwAction action = parseXml(elm);
			actions.add(action);
		}
		return actions;
	}

	private static OwAction parseXml(Element elm) {
		OwAction ac = new OwAction();
		ac.name = elm.attributeValue("name");
		ac.cred = elm.attributeValue("cred");
		ac.retry_max = elm.attributeValue("retry-max");
		ac.retry_interval = elm.attributeValue("retry-interval");
		ac.ok = OwActionTransition.parseXml(elm.element("ok"));
		if (elm.element("email") != null) {
			ac.email = OwEmail.parseXml(elm.element("email"));
			ac.actionType = "email";
		} else if (elm.element("ftp") != null) {
			ac.ftp = OwFtp.parseXml(elm.element("ftp"));
			ac.actionType = "ftp";
		} else if (elm.element("ftpNew") != null) {
			ac.ftpNew = OwFtpNew.parseXml(elm.element("ftpNew"));
			ac.actionType = "ftpNew";
		} else if (elm.element("sftp") != null) {
			ac.sftp = OwSftp.parseXml(elm.element("sftp"));
			ac.actionType = "sftp";
		} else if (elm.element("hive") != null) {
			ac.hive = OwHive.parseXml(elm.element("hive"));
			ac.actionType = "hive";
		} else if (elm.element("hive2") != null) {
			ac.hive2 = OwHive2.parseXml(elm.element("hive2"));
			ac.actionType = "hive2";
		} else if (elm.element("impala") != null) {
			ac.impala = OwImpala.parseXml(elm.element("impala"));
			ac.actionType = "impala";
		} else if (elm.element("gbase") != null) {
			ac.impala = OwImpala.parseXml(elm.element("gbase"));
			ac.actionType = "gbase";
		} else if (elm.element("map-reduce") != null) {
			ac.map_reduce = OwMapReduce.parseXml(elm.element("map-reduce"));
			ac.actionType = "map-reduce";
		} else if (elm.element("spark") != null) {
			ac.spark = OwSpark.parseXml(elm.element("spark"));
			ac.actionType = "spark";
		} else if (elm.element("sqoop") != null) {
			ac.sqoop = OwSqoop.parseXml(elm.element("sqoop"));
			ac.actionType = "sqoop";
		} else if (elm.element("shell") != null) {
			ac.shell = OwShell.parseXml(elm.element("shell"));
			ac.actionType = "shell";
		} else if (elm.element("ssh") != null) {
			ac.ssh = OwSsh.parseXml(elm.element("ssh"));
			ac.actionType = "ssh";
		} else if (elm.element("subflow") != null) {
			ac.subflow = OwSubflow.parseXml(elm.element("subflow"));
			ac.actionType = "subflow";
		} else if (elm.element("fs") != null) {
			ac.fs = OwFs.parseXml(elm.element("fs"));
			ac.actionType = "fs";
		} else if (elm.element("wash") != null) {
			ac.wash = OwWash.parseXml(elm.element("wash"));
			ac.actionType = "wash";
		} else if (elm.element("esload") != null) {
			ac.es = OwEsLoad.parseXml(elm.element("esload"));
			ac.actionType = "esload";
		} else if (elm.element("distcp") != null) {
			ac.distcp = OwDistcp.parseXml(elm.element("distcp"));
			ac.actionType = "distcp";
		}
		return ac;
	}

	public FlowNode toFlowNode() {
		FlowNode fn = new FlowNode();
		fn.setText(name);

		List<String> tos = new ArrayList<String>();
		tos.add(ok.getTo());
		fn.setTo(tos);

		if (email != null) {
			fn.setType("7");
			fn.setAttribute(email.toAttributes());
		} else if (ftp != null) {
			fn.setType("8");
			fn.setAttribute(ftp.toAttributes());
		} else if (ftpNew != null) {
			fn.setType("8");
			fn.setAttribute(ftpNew.toAttributes());
		} else if (hive != null) {
			fn.setType("9");
			fn.setAttribute(hive.toAttributes());
		} else if (hive2 != null) {
			fn.setType("15");
			fn.setAttribute(hive2.toAttributes());
		} else if (impala != null) {
			fn.setType("impala");
			fn.setAttribute(impala.toAttributes());
		} else if (gbase != null) {
			fn.setType("gbase");
			fn.setAttribute(gbase.toAttributes());
		} else if (map_reduce != null) {
			fn.setType("3");
			fn.setAttribute(map_reduce.toAttributes());
		} else if (spark != null) {
			fn.setType("17");
			fn.setAttribute(spark.toAttributes());
		} else if (sqoop != null) {
			fn.setType("10");
			fn.setAttribute(sqoop.toAttributes());
		} else if (shell != null) {
			fn.setType("13");
			fn.setAttribute(shell.toAttributes());
		} else if (ssh != null) {
			fn.setType("14");
			fn.setAttribute(ssh.toAttributes());
		} else if (distcp != null) {
			fn.setType("16");
			fn.setAttribute(distcp.toAttributes());
		} else if (subflow != null) {
			fn.setType("31");
			fn.setAttribute(subflow.toAttributes());
		} else if (fs != null) {
			fn.setType("1");
			fn.setAttribute(fs.toAttributes());
		} else if (wash != null) {
			fn.setType("33");
			fn.setAttribute(wash.toAttributes());
		} else if (es != null) {
			fn.setType("34");
			fn.setAttribute(es.toAttributes());
		}

		if (!StringUtils.isEmpty(retry_max)) {
			fn.setRetryMax(retry_max);
			fn.setRetryInt(retry_interval);
		}
		if (!StringUtils.isEmpty(cred)) {
			fn.setCred(cred);
		}
		return fn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCred() {
		return cred;
	}

	public void setCred(String cred) {
		this.cred = cred;
	}

	public String getRetry_max() {
		return retry_max;
	}

	public void setRetry_max(String retry_max) {
		this.retry_max = retry_max;
	}

	public String getRetry_interval() {
		return retry_interval;
	}

	public void setRetry_interval(String retry_interval) {
		this.retry_interval = retry_interval;
	}

	public OwSubflow getSubflow() {
		return subflow;
	}

	public void setSubflow(OwSubflow subflow) {
		this.subflow = subflow;
	}

	public OwEmail getEmail() {
		return email;
	}

	public void setEmail(OwEmail email) {
		this.email = email;
	}

	public OwFtp getFtp() {
		return ftp;
	}

	public void setFtp(OwFtp ftp) {
		this.ftp = ftp;
	}

	public OwFtpNew getFtpNew() {
		return ftpNew;
	}

	public void setFtpNew(OwFtpNew ftpNew) {
		this.ftpNew = ftpNew;
	}

	public OwSftp getSftp() {
		return sftp;
	}

	public void setSftp(OwSftp sftp) {
		this.sftp = sftp;
	}

	public OwHive getHive() {
		return hive;
	}

	public void setHive(OwHive hive) {
		this.hive = hive;
	}

	public OwHive2 getHive2() {
		return hive2;
	}

	public void setHive2(OwHive2 hive2) {
		this.hive2 = hive2;
	}

	public OwImpala getImpala() {
		return impala;
	}

	public void setImpala(OwImpala impala) {
		this.impala = impala;
	}

	public OwGbase getGbase() {
		return gbase;
	}

	public void setGbase(OwGbase gbase) {
		this.gbase = gbase;
	}

	public OwMapReduce getMap_reduce() {
		return map_reduce;
	}

	public void setMap_reduce(OwMapReduce map_reduce) {
		this.map_reduce = map_reduce;
	}

	public OwSpark getSpark() {
		return spark;
	}

	public void setSpark(OwSpark spark) {
		this.spark = spark;
	}

	public OwSqoop getSqoop() {
		return sqoop;
	}

	public void setSqoop(OwSqoop sqoop) {
		this.sqoop = sqoop;
	}

	public OwShell getShell() {
		return shell;
	}

	public void setShell(OwShell shell) {
		this.shell = shell;
	}

	public OwSsh getSsh() {
		return ssh;
	}

	public void setSsh(OwSsh ssh) {
		this.ssh = ssh;
	}

	public OwFs getFs() {
		return fs;
	}

	public void setFs(OwFs fs) {
		this.fs = fs;
	}

	public OwJava getJava() {
		return java;
	}

	public void setJava(OwJava java) {
		this.java = java;
	}

	public OwDistcp getDistcp() {
		return distcp;
	}

	public void setDistcp(OwDistcp distcp) {
		this.distcp = distcp;
	}

	public OwWash getWash() {
		return wash;
	}

	public void setWash(OwWash wash) {
		this.wash = wash;
	}

	public OwEsLoad getEs() {
		return es;
	}

	public void setEs(OwEsLoad es) {
		this.es = es;
	}

	public OwActionTransition getOk() {
		return ok;
	}

	public void setOk(OwActionTransition ok) {
		this.ok = ok;
	}

	public OwActionTransition getError() {
		return error;
	}

	public void setError(OwActionTransition error) {
		this.error = error;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
}
