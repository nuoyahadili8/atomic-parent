package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwAbstractActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwConfiguration;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwDelete;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwMkdir;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.List;

@XStreamAlias("wash")
public class OwWash extends OwAbstractActionNode implements OwActionNode {
	private static String INPUT_DIR = "input.dir";
	private static String OUTPUT_DIR = "output.dir";

	private static String SPLIT_STR = "wash.split.str";
	private static String WASH_COLUMN_NUMBER = "wash.column.number";
	private static String WASH_NO_NULL_CLOUMN = "wash.no.null.cloumn";
	private static String WASH_REGEXS = "wash.regExs";

	@XStreamAsAttribute
	private String xmlns = "uri:oozie:wash-action:0.1";

	public OwWash() {
		super();
	}

	public OwWash(FlowNode flowNode) {
		super(flowNode);

		OwConfiguration config = super.getConfiguration();
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);

		OwProperty propP1 = new OwProperty();
		propP1.setName("input.dir");
		propP1.setValue(attJson.getString(INPUT_DIR));
		config.getPropertys().add(propP1);

		OwProperty propP2 = new OwProperty();
		propP2.setName("output.dir");
		propP2.setValue(attJson.getString(OUTPUT_DIR));
		config.getPropertys().add(propP2);

		OwProperty propP3 = new OwProperty();
		propP3.setName("split.str");
		propP3.setValue(attJson.getString(SPLIT_STR));
		config.getPropertys().add(propP3);

		OwProperty propP4 = new OwProperty();
		propP4.setName("column.number");
		propP4.setValue(attJson.getString(WASH_COLUMN_NUMBER));
		config.getPropertys().add(propP4);

		String columnsNoNulls = attJson.getString(WASH_NO_NULL_CLOUMN);
		if (columnsNoNulls != null && !StringUtils.isEmpty(columnsNoNulls.trim())) {
			OwProperty propP5 = new OwProperty();
			propP5.setName("no.null.cloumn");
			propP5.setValue(columnsNoNulls.trim());
			config.getPropertys().add(propP5);
		}

		String washRegexs = attJson.getString(WASH_REGEXS);
		if (washRegexs != null && !StringUtils.isEmpty(washRegexs.trim())) {
			OwProperty propP6 = new OwProperty();
			propP6.setName("wash.regExs");
			propP6.setValue(washRegexs.trim());
			config.getPropertys().add(propP6);
		}
	}

	public static OwWash parseXml(Element element) {
		OwWash wash = new OwWash();
		wash.parseXmls(element);

		return wash;
	}

	public Object toAttributes() {
		JSONObject attObj = super.toAttributesJSONOject();
		if (super.getPrepare() != null) {
			if (super.getPrepare().getDeletes() != null) {
				String deletes = "";
				for (OwDelete owDel : super.getPrepare().getDeletes()) {
					if (!deletes.isEmpty()) {
						deletes += ";";
					}

					deletes += owDel.getPath();
				}
				attObj.put("delete", deletes);
			}
			if (super.getPrepare().getMkdirs() != null) {
				String mkdirs = "";
				for (OwMkdir owdMk : super.getPrepare().getMkdirs()) {
					if (!mkdirs.isEmpty()) {
						mkdirs += ";";
					}

					mkdirs += owdMk.getPath();
				}
				attObj.put("mkdir", mkdirs);
			}
		}
		if (this.configuration != null) {
			List<OwProperty> props = this.configuration.getPropertys();
			if (props != null) {
				for (OwProperty prop : props) {
					if ("input.dir".equals(prop.getName())) {
						attObj.put(INPUT_DIR, prop.getValue());
					} else if ("output.dir".equals(prop.getName())) {
						attObj.put(OUTPUT_DIR, prop.getValue());
					} else if ("split.str".equals(prop.getName())) {
						attObj.put(SPLIT_STR, prop.getValue());
					} else if ("column.number".equals(prop.getName())) {
						attObj.put(WASH_COLUMN_NUMBER, prop.getValue());
					} else if ("no.null.cloumn".equals(prop.getName())) {
						attObj.put(WASH_NO_NULL_CLOUMN, prop.getValue());
					} else if ("wash.regExs".equals(prop.getName())) {
						attObj.put(WASH_REGEXS, prop.getValue());
					}
				}
			}
		}
		return attObj.toJSONString();
	}
}
