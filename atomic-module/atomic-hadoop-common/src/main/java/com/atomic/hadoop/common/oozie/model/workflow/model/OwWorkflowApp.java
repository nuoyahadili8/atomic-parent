package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.atomic.hadoop.common.hdfs.HadoopConstants;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwParameters;
import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.atomic.hadoop.common.oozie.vo.TaskVo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("workflow-app")
public class OwWorkflowApp {

	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String xmlns = "uri:oozie:workflow:0.5";
	private OwParameters parametes;
	private OwGlobal global;
	private OwCredentials credentials;
	private OwStart start;
	@XStreamImplicit(itemFieldName = "decision")
	private List<OwDecision> decisions = new ArrayList<OwDecision>();
	@XStreamImplicit(itemFieldName = "fork")
	private List<OwFork> forks = new ArrayList<OwFork>();
	@XStreamImplicit(itemFieldName = "join")
	private List<OwJoin> joins = new ArrayList<OwJoin>();
	@XStreamImplicit(itemFieldName = "action")
	private List<OwAction> actions = new ArrayList<OwAction>();
	@XStreamImplicit(itemFieldName = "kill")
	private List<OwKill> kills = new ArrayList<OwKill>();
	private OwEnd end;

	public OwWorkflowApp() {
		OwKill kill = new OwKill();
		getKills().add(kill);
	}

	public OwWorkflowApp(TaskVo flowVo) {
		this.name = flowVo.getNameEn();
		if (flowVo.getGlobalVo() != null) {
			global = new OwGlobal(flowVo.getGlobalVo());
		}

		if (flowVo.getConHbaseVo() != null || flowVo.getConHcatlogVo() != null || flowVo.getConHive2Vo() != null) {
			this.credentials = new OwCredentials(flowVo.getConHbaseVo(), flowVo.getConHcatlogVo(), flowVo.getConHive2Vo());
		}

		OwKill kill = new OwKill();
		getKills().add(kill);
	}

	public OwWorkflowApp(String workflowNameEn, List<ConfCredentialVo> confCredentialVos){
		this.name = workflowNameEn;
		ConfCredentialVo conHbaseVo = null;
		ConfCredentialVo conHcatlogVo = null;
		ConfCredentialVo conHive2Vo = null;
		for (ConfCredentialVo confCredentialVo: confCredentialVos){
			if (HadoopConstants.OOZIE_CRED_HIVE.equals(confCredentialVo.getType())){
				conHive2Vo = confCredentialVo;
			}

			if (HadoopConstants.OOZIE_CRED_HCAT.equals(confCredentialVo.getType())){
				conHcatlogVo = confCredentialVo;
			}

			if (HadoopConstants.OOZIE_CRE_HBASE.equals(confCredentialVo.getType())){
				conHbaseVo = confCredentialVo;
			}
		}

		if (conHbaseVo != null || conHcatlogVo != null || conHive2Vo != null){
			this.credentials = new OwCredentials(conHbaseVo,conHcatlogVo,conHive2Vo);
		}

		OwKill kill = new OwKill();
		getKills().add(kill);
	}

	public String valid() {
		String invalidStr = null;
		if (!decisions.isEmpty()) {
			for (OwDecision dec : decisions) {
				invalidStr = dec.valid();
				if (invalidStr != null) {
					return invalidStr;
				}
			}
		}
		if (!forks.isEmpty()) {
			for (OwFork fork : forks) {
				invalidStr = fork.valid();
				if (invalidStr != null) {
					return invalidStr;
				}
			}
		}
		if (!joins.isEmpty()) {
			for (OwJoin join : joins) {
				invalidStr = join.valid();
				if (invalidStr != null) {
					return invalidStr;
				}
			}
		}
		return null;
	}

	public String toXml() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		return "<?xml version='1.0' encoding='UTF-8'?>\n" + xstream.toXML(this).replaceAll("__", "_");
	}

	public void parseXml(String flowXml) {
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(new StringReader(flowXml));
			Element rootElm = document.getRootElement();
			this.name = rootElm.attributeValue("name");
			this.start = OwStart.parseXml(rootElm.element("start"));
			this.end = OwEnd.parseXml(rootElm.element("end"));
			Element parasElm = rootElm.element("parametes");
			if (parasElm != null) {
				this.parametes = OwParameters.parseXml(parasElm);
			}
			Element globalElm = rootElm.element("global");
			if (globalElm != null) {
				this.global = OwGlobal.parseXml(globalElm);
			}
			Element credentialsElm = rootElm.element("credentials");
			if (credentialsElm != null) {
				this.credentials = OwCredentials.parseXml(credentialsElm);
			}
			List<Element> dElms = rootElm.elements("decision");
			if (dElms != null) {
				this.decisions = OwDecision.parseXml(dElms);
			}
			List<Element> fElms = rootElm.elements("fork");
			if (fElms != null) {
				this.forks = OwFork.parseXml(fElms);
			}
			List<Element> jElms = rootElm.elements("join");
			if (jElms != null) {
				this.joins = OwJoin.parseXml(jElms);
			}
			List<Element> aElms = rootElm.elements("action");
			if (aElms != null) {
				this.actions = OwAction.parseXml(aElms);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OwStart getStart() {
		return start;
	}

	public void setStart(OwStart start) {
		this.start = start;
	}

	public OwParameters getParametes() {
		return parametes;
	}

	public void setParametes(OwParameters parametes) {
		this.parametes = parametes;
	}

	public OwGlobal getGlobal() {
		return global;
	}

	public void setGlobal(OwGlobal global) {
		this.global = global;
	}

	public OwCredentials getCredentials() {
		return credentials;
	}

	public void setCredentials(OwCredentials credentials) {
		this.credentials = credentials;
	}

	public List<OwDecision> getDecisions() {
		return decisions;
	}

	public void setDecisions(List<OwDecision> decisions) {
		this.decisions = decisions;
	}

	public List<OwFork> getForks() {
		return forks;
	}

	public void setForks(List<OwFork> forks) {
		this.forks = forks;
	}

	public List<OwJoin> getJoins() {
		return joins;
	}

	public void setJoins(List<OwJoin> joins) {
		this.joins = joins;
	}

	public List<OwAction> getActions() {
		return actions;
	}

	public void setActions(List<OwAction> actions) {
		this.actions = actions;
	}

	public List<OwKill> getKills() {
		return kills;
	}

	public void setKills(List<OwKill> kills) {
		this.kills = kills;
	}

	public OwEnd getEnd() {
		return end;
	}

	public void setEnd(OwEnd end) {
		this.end = end;
	}
}
