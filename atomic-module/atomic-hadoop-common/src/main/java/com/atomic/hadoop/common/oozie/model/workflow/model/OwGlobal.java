package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwConfiguration;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwProperty;
import com.atomic.hadoop.common.oozie.vo.GlobalVo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("global")
public class OwGlobal {
	@XStreamAlias("job-tracker")
	private String job_tracker;
	@XStreamAlias("name-node")
	private String name_node;
	@XStreamImplicit(itemFieldName = "job-xml")
	private List<String> job_xmls;
	private OwConfiguration configuration;

	public OwGlobal(GlobalVo globalVo) {
		OwGlobal global = new OwGlobal();
		global.job_tracker = globalVo.getJob_tracker();
		global.name_node = globalVo.getName_node();

		if (!StringUtils.isEmpty(globalVo.getConfiguration())) {
			configuration = new OwConfiguration();
			List<OwProperty> owPropertys = new ArrayList<OwProperty>();
			String[] paras = globalVo.getConfiguration().split(";");
			for (String para : paras) {
				String[] kvs = para.split("#");
				OwProperty owProperty = new OwProperty();
				owProperty.setName(kvs[0]);
				owProperty.setValue(kvs[1]);
				owPropertys.add(owProperty);
			}
			configuration.setPropertys(owPropertys);
		}

		if (!StringUtils.isEmpty(globalVo.getJob_xml())) {
			job_xmls = new ArrayList<String>();
			String[] jobXmls = globalVo.getJob_xml().split(";");
			for (String jobXml : jobXmls) {
				job_xmls.add(jobXml);
			}
		}

	}

	public OwGlobal() {
		// TODO 自动生成的构造函数存根
	}

	public static OwGlobal parseXml(Element ele) {
		OwGlobal global = new OwGlobal();
		global.job_tracker = ele.elementText("job-tracker");
		global.name_node = ele.elementText("name-node");

		List<Element> jobxmls = ele.elements("job-xml");
		if (jobxmls != null && jobxmls.size() > 0) {
			global.job_xmls = new ArrayList<String>();
			for (Element jobxml : jobxmls) {
				global.job_xmls.add(jobxml.getTextTrim());
			}
		}
		if (ele.element("configuration") != null) {
			global.configuration = OwConfiguration.parseXml(ele.element("configuration"));
		}

		return global;
	}

	public String getJob_tracker() {
		return job_tracker;
	}

	public void setJob_tracker(String job_tracker) {
		this.job_tracker = job_tracker;
	}

	public String getName_node() {
		return name_node;
	}

	public void setName_node(String name_node) {
		this.name_node = name_node;
	}

	public List<String> getJob_xmls() {
		return job_xmls;
	}

	public void setJob_xmls(List<String> job_xmls) {
		this.job_xmls = job_xmls;
	}

	public OwConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(OwConfiguration configuration) {
		this.configuration = configuration;
	}

}
