package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;


public abstract class OwAbstractActionNodeFS implements OwActionNode{
	private static String MAPRED_JOB_QUEUE_NAME = "mapred.job.queue.name";
	private static String MAPREDUCE_JOB_QUEUENAME = "mapreduce.job.queuename";
	private static String MAPRED_MAP_CHILD_JAVA_OPTS = "mapred.map.child.java.opts";
	private static String MAPREDUCE_MAP_JAVA_OPTS = "mapreduce.map.java.opts";
	private static String MAPRED_REDUCE_CHILD_JAVA_OPTS = "mapred.reduce.child.java.opts";
	private static String MAPREDUCE_REDUCE_JAVA_OPTS = "mapreduce.reduce.java.opts";
	
	
//	@XStreamAlias("job-tracker")
//	protected String job_tracker="${jobTracker}";
	@XStreamAlias("name-node")	
	protected String name_node="${nameNode}";
	
	protected OwPrepare prepare;
	
	@XStreamImplicit(itemFieldName = "job-xml")
	protected List<String> job_xmls;
	
	protected OwConfiguration configuration;
	
	protected boolean isPublicPara(String paraName){
		return (MAPRED_JOB_QUEUE_NAME.equals(paraName)
				|| MAPREDUCE_JOB_QUEUENAME.equals(paraName)
				|| MAPRED_MAP_CHILD_JAVA_OPTS.equals(paraName)
				|| MAPREDUCE_MAP_JAVA_OPTS.equals(paraName)
				|| MAPRED_REDUCE_CHILD_JAVA_OPTS.equals(paraName)
				|| MAPREDUCE_REDUCE_JAVA_OPTS.equals(paraName)
				); 
	}
	
	public OwAbstractActionNodeFS(FlowNode flowNode) {
		String att = flowNode.getAttribute().toString();
	    JSONObject attJson =  JSON.parseObject(att);
	    
	    
	    String queue = attJson.getString("queue");
		OwProperty prop = new OwProperty();
		prop.setName(MAPRED_JOB_QUEUE_NAME);
		
		OwProperty prop1 = new OwProperty();
		prop1.setName(MAPREDUCE_JOB_QUEUENAME);
		
		if(queue==null|| queue.trim().isEmpty()){
			prop.setValue("${queueName}");
			prop1.setValue("${queueName}");
		}else{
			prop.setValue(queue);
			prop1.setValue(queue);
		}
		
		List<OwProperty> props = new ArrayList<OwProperty>();
		props.add(prop);
		props.add(prop1);
	  
	    
	    String mapOpts = attJson.getString(MAPRED_MAP_CHILD_JAVA_OPTS);
	    if(mapOpts!=null && !mapOpts.isEmpty()){
	    	OwProperty prop2 = new OwProperty();
	    	prop2.setName(MAPRED_MAP_CHILD_JAVA_OPTS);
	    	prop2.setValue(mapOpts);
	    	props.add(prop2);
	    	
	    	OwProperty prop22 = new OwProperty();
	    	prop22.setName(MAPREDUCE_MAP_JAVA_OPTS);
	    	prop22.setValue(mapOpts);
	    	props.add(prop22);
	    }
	    
	    String reduceOpts = attJson.getString(MAPRED_REDUCE_CHILD_JAVA_OPTS);
	    if(reduceOpts!=null && !reduceOpts.isEmpty()){
	    	OwProperty prop3 = new OwProperty();
	    	prop3.setName(MAPRED_REDUCE_CHILD_JAVA_OPTS);
	    	prop3.setValue(reduceOpts);
	    	props.add(prop3);
	    	
	    	OwProperty prop33 = new OwProperty();
	    	prop33.setName(MAPREDUCE_REDUCE_JAVA_OPTS);
	    	prop33.setValue(reduceOpts);
	    	props.add(prop33);
	    }
	    
	    configuration = new OwConfiguration();
	    configuration.setPropertys(props);		
	    
	    
	    String jobXmls = attJson.getString("job-xml");
	    if(jobXmls!=null && !jobXmls.trim().isEmpty()){
	    	job_xmls = new ArrayList<String>();
	    	String[] jobXmla = jobXmls.trim().split("[;]");
	    	for(String jobXml:jobXmla){
	    		if(!jobXml.trim().isEmpty()){
	    			job_xmls.add(jobXml);
	    		}
	    	}
	    }
	    String delete = attJson.getString("delete");
	    String mkdir = attJson.getString("mkdir");
	    prepare = null;
	    if(delete!=null && !delete.trim().isEmpty()){
	    	String[] deletes = delete.trim().split("[;]");
	    	List<OwDelete> owdels =new ArrayList<OwDelete>();
	    	for(String del:deletes){
	    		if(!del.trim().isEmpty()){
		    		OwDelete owDel =new OwDelete(del.trim());
		    		owdels.add(owDel);
	    		}
	    	}
	    	prepare = new OwPrepare();
	    	prepare.setDeletes(owdels);
	    }
	    if(mkdir!=null&& !mkdir.trim().isEmpty()){
	    	String[] mkdirs = mkdir.trim().split("[;]");
	    	List<OwMkdir> owMks =new ArrayList<OwMkdir>();
	    	for(String mk:mkdirs){
	    		if(!mk.trim().isEmpty()){
		    		OwMkdir owMk =new OwMkdir(mk.trim());
		    		owMks.add(owMk);	
	    		}
	    	}
	    	
	    	if(prepare==null){
				prepare = new OwPrepare();
			}

	    	prepare.setMkdirs(owMks);
	    }	    
	}
	public OwAbstractActionNodeFS() {
		// TODO 自动生成的构造函数存根
	}
	public void parseXmls(Element ele){
//		this.job_tracker = ele.elementText("job-tracker");
		this.name_node = ele.elementText("name-node");
		
		List<Element> jobxmls = ele.elements("job-xml");
		if(jobxmls!=null && jobxmls.size()>0){
			job_xmls = new ArrayList<String>();
			for(Element jobxml:jobxmls){
				job_xmls.add(jobxml.getTextTrim());
			}
		}
		if(ele.element("configuration")!=null){
			this.configuration = OwConfiguration.parseXml(ele.element("configuration"));
		}
		if(ele.element("prepare")!=null){
			this.prepare = OwPrepare.parseXml(ele.element("prepare"));
		}
	}
	
	public JSONObject toAttributesJSONOject() {
		JSONObject attObj = new JSONObject();
		if(this.configuration!=null){
			List<OwProperty> props = this.configuration.getPropertys();
			if(props!=null){
				for(OwProperty prop:props){
					if(MAPRED_JOB_QUEUE_NAME.equals(prop.getName())
							|| MAPREDUCE_JOB_QUEUENAME.equals(prop.getName())){
						attObj.put("queue", prop.getValue());
					}else if(MAPRED_MAP_CHILD_JAVA_OPTS.equals(prop.getName())
							|| MAPREDUCE_MAP_JAVA_OPTS.equals(prop.getName())){
						attObj.put(MAPRED_MAP_CHILD_JAVA_OPTS, prop.getValue());
					}else if(MAPRED_REDUCE_CHILD_JAVA_OPTS.equals(prop.getName())
							|| MAPREDUCE_REDUCE_JAVA_OPTS.equals(prop.getName())){
						attObj.put(MAPRED_REDUCE_CHILD_JAVA_OPTS, prop.getValue());
					}
				}
			}
		}
		return attObj;
	}
	
/*	public String getJob_tracker() {
		return job_tracker;
	}
	public void setJob_tracker(String job_tracker) {
		this.job_tracker = job_tracker;
	}*/
	public String getName_node() {
		return name_node;
	}
	public void setName_node(String name_node) {
		this.name_node = name_node;
	}
	public OwConfiguration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(OwConfiguration configuration) {
		this.configuration = configuration;
	}
	public OwPrepare getPrepare() {
		return prepare;
	}
	public void setPrepare(OwPrepare prepare) {
		this.prepare = prepare;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((configuration == null) ? 0 : configuration.hashCode());
/*		result = prime * result
				+ ((job_tracker == null) ? 0 : job_tracker.hashCode());
*/		result = prime * result
				+ ((name_node == null) ? 0 : name_node.hashCode());

		result = prime * result + ((prepare == null) ? 0 : prepare.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}

		if (obj == null){
			return false;
		}

		if (getClass() != obj.getClass()){
			return false;
		}

		OwAbstractActionNodeFS other = (OwAbstractActionNodeFS) obj;
		if (configuration == null) {
			if (other.configuration != null){
				return false;
			}

		} else if (!configuration.equals(other.configuration)){
			return false;
		}

/*		if (job_tracker == null) {
			if (other.job_tracker != null)
				return false;
		} else if (!job_tracker.equals(other.job_tracker))
			return false;*/

		if (name_node == null) {
			if (other.name_node != null){
				return false;
			}
		} else if (!name_node.equals(other.name_node)){
			return false;
		}
		if (prepare == null) {
			if (other.prepare != null){
				return false;
			}
		} else if (!prepare.equals(other.prepare)){
			return false;
		}

		return true;
	}
	@Override
	public String toString() {
		return "OwAbstractActionNode ["//job_tracker=" + job_tracker
				+ " job_xmls=" + job_xmls +  ", name_node=" + name_node + ", configuration="
				+ configuration + ", prepare=" + prepare + "]";
	}
	
}
