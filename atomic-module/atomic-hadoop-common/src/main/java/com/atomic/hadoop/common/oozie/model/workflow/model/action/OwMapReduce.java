package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwAbstractActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("map-reduce")
public class OwMapReduce extends OwAbstractActionNode implements OwActionNode {

	private static String JOB_INPUT_FORMAT_CLASS_TAG = "job.input.format.class";
	private static String JOB_OUTPUT_FORMAT_CLASS_TAG = "job.output.format.class";

	private static String MAPRED_INPUT_FORMAT_CLASS = "mapred.input.format.class";
	private static String MAPRED_OUTPUT_FORMAT_CLASS = "mapred.output.format.class";

	private static String MAPREDUCE_INPUT_FORMAT_CLASS = "mapreduce.job.inputformat.class";
	private static String MAPREDUCE_OUTPUT_FORMAT_CLASS = "mapreduce.job.outputformat.class";

	private static String MAP_CLASS_TAG = "mapred.mapper.class";
	private static String REDUCER_CLASS_TAG = "mapred.reducer.class";
	private static String MAP_INPUT_DIR_TAG = "mapred.input.dir";
	private static String MAP_OUTPUT_DIR_TAG = "mapred.output.dir";
	private static String COMBINE_CLASS_TAG = "mapred.combine.class";

	private static String JOB_OUTPUT_KEY_CLASS_TAG = "job.output.key.class";
	private static String JOB_OUTPUT_VALUE_CLASS_TAG = "job.output.value.class";
	private static String MAP_OUTPUT_KEY_CLASS_TAG = "map.output.key.class";
	private static String MAP_OUTPUT_VALUE_CLASS_TAG = "map.output.value.class";

	private static String MAP_TASKS_TAG = "mapred.map.tasks";
	private static String REDUCE_TASKS_TAG = "mapred.reduce.tasks";
	private static String VERSION_TAG = "mrversion";

	private static String MAPRED_MAP_TASKS = "mapred.map.tasks";
	private static String MAPRED_REDUCE_TASKS = "mapred.reduce.tasks";

	private static String MAPRED_MAPPER_CLASS = "mapred.mapper.class";
	private static String MAPRED_REDUCER_CLASS = "mapred.reducer.class";
	private static String MAPRED_INPUT_DIR = "mapred.input.dir";
	private static String MAPRED_OUTPUT_DIR = "mapred.output.dir";
	private static String MAPREDUCE_COMBINE_CLASS = "mapreduce.combine.class";
	private static String MAPRED_OUTPUT_KEY_CLASS = "mapred.output.key.class";
	private static String MAPRED_OUTPUT_VALUE_CLASS = "mapred.output.value.class";

	private static String MAPRED_MAPOUTPUT_KEY_CLASS = "mapred.mapoutput.key.class ";
	private static String MAPRED_MAPOUTPUT_VALUE_CLASS = "mapred.mapoutput.value.clas";

	private static String MAPREDUCE_JOB_MAP_CLASS = "mapreduce.job.map.class";
	private static String MAPREDUCE_JOB_REDUCE_CLASS = "mapreduce.job.reduce.class";
	private static String MAPREDUCE_INPUT_FILEINPUTFORMAT_INPUTDIR = "mapreduce.input.fileinputformat.inputdir";
	private static String MAPREDUCE_OUTPUT_FILEOUTPUTFORMAT_OUTPUTDIR = "mapreduce.output.fileoutputformat.outputdir";

	private static String MAPREDUCE_JOB_COMBINE_CLASS = "mapreduce.job.combine.class";
	private static String MAPREDUCE_JOB_OUTPUT_KEY_CLASS = "mapreduce.job.output.key.class";
	private static String MAPREDUCE_JOB_OUTPUT_VALUE_CLASS = "mapreduce.job.output.value.class";
	private static String MAPREDUCE_MAP_OUTPUT_KEY_CLASS = "mapreduce.map.output.key.class";
	private static String MAPREDUCE_MAP_OUTPUT_VALUE_CLASS = "mapreduce.map.output.value.class";

	private static String MAPRED_VERSION = "mrversion";

	private static String MAPPPER_MAPPER_NEWAPI = "mapred.mapper.new-api";
	private static String MAPPPER_REDUCER_NEWAPI = "mapred.reducer.new-api";

	private OwStreaming streaming;
	private OwPipes pipes;

	@XStreamAlias("config-class")
	private String config_class;

	@XStreamImplicit(itemFieldName = "file")
	private List<String> files;
	@XStreamImplicit(itemFieldName = "archive")
	private List<String> archives;

	public OwMapReduce(FlowNode flowNode) {
		super(flowNode);
		String att = flowNode.getAttribute().toString();
		JSONObject attJson = JSON.parseObject(att);
		String mapper = attJson.getString(MAP_CLASS_TAG);
		String reduce = attJson.getString(REDUCER_CLASS_TAG);
		String input = attJson.getString(MAP_INPUT_DIR_TAG);
		String output = attJson.getString(MAP_OUTPUT_DIR_TAG);
		String combine = attJson.getString(COMBINE_CLASS_TAG);
		String outputKey = attJson.getString(JOB_OUTPUT_KEY_CLASS_TAG);
		String outputValue = attJson.getString(JOB_OUTPUT_VALUE_CLASS_TAG);

		String mapOutputKey = attJson.getString(MAP_OUTPUT_KEY_CLASS_TAG);
		String mapOutputValue = attJson.getString(MAP_OUTPUT_VALUE_CLASS_TAG);

		String inputFormart = attJson.getString(JOB_INPUT_FORMAT_CLASS_TAG);
		String outputFormart = attJson.getString(JOB_OUTPUT_FORMAT_CLASS_TAG);

		String mapTasks = attJson.getString(MAP_TASKS_TAG);
		String reduceTask = attJson.getString(REDUCE_TASKS_TAG);
		String mrversion = attJson.getString(VERSION_TAG);

		String moreParas = attJson.getString("moreParas");

		boolean isNew = "new".equals(mrversion);
		setPropByVersion(isNew, MAPRED_MAPPER_CLASS, MAPREDUCE_JOB_MAP_CLASS, mapper);
		setPropByVersion(isNew, MAPRED_REDUCER_CLASS, MAPREDUCE_JOB_REDUCE_CLASS, reduce);
		setPropByVersion(isNew, MAPRED_INPUT_DIR, MAPREDUCE_INPUT_FILEINPUTFORMAT_INPUTDIR, input);
		setPropByVersion(isNew, MAPRED_OUTPUT_DIR, MAPREDUCE_OUTPUT_FILEOUTPUTFORMAT_OUTPUTDIR, output);
		setPropByVersion(isNew, MAPREDUCE_COMBINE_CLASS, MAPREDUCE_JOB_COMBINE_CLASS, combine);

		setPropByVersion(isNew, MAPRED_OUTPUT_KEY_CLASS, MAPREDUCE_JOB_OUTPUT_KEY_CLASS, outputKey);
		setPropByVersion(isNew, MAPRED_OUTPUT_VALUE_CLASS, MAPREDUCE_JOB_OUTPUT_VALUE_CLASS, outputValue);

		setPropByVersion(isNew, MAPRED_MAPOUTPUT_KEY_CLASS, MAPREDUCE_MAP_OUTPUT_KEY_CLASS, mapOutputKey);
		setPropByVersion(isNew, MAPRED_MAPOUTPUT_VALUE_CLASS, MAPREDUCE_MAP_OUTPUT_VALUE_CLASS, mapOutputValue);

		setPropByVersion(isNew, MAPRED_INPUT_FORMAT_CLASS, MAPREDUCE_INPUT_FORMAT_CLASS, inputFormart);
		setPropByVersion(isNew, MAPRED_OUTPUT_FORMAT_CLASS, MAPREDUCE_OUTPUT_FORMAT_CLASS, outputFormart);

		setPropByVersion(isNew, MAPRED_MAP_TASKS, MAPRED_MAP_TASKS, mapTasks);
		setPropByVersion(isNew, MAPRED_REDUCE_TASKS, MAPRED_REDUCE_TASKS, reduceTask);

		OwConfiguration config = super.getConfiguration();
		OwProperty prop1 = new OwProperty();

		prop1.setName(MAPPPER_MAPPER_NEWAPI);
		prop1.setValue("true");
		OwProperty prop2 = new OwProperty();
		prop2.setName(MAPPPER_REDUCER_NEWAPI);
		prop2.setValue("true");

		if ("new".equals(mrversion)) {
			config.getPropertys().add(prop1);
			config.getPropertys().add(prop2);
		}

		if (moreParas != null && !StringUtils.isEmpty(moreParas.trim())) {
			String[] moreParass = moreParas.trim().split(";");
			for (String paras : moreParass) {
				String[] para = paras.split("=");
				if (para.length > 1) {
					OwProperty propP = new OwProperty();
					propP.setName(para[0]);
					propP.setValue(para[1]);
					config.getPropertys().add(propP);
				}
			}
		}
		String fileStrs = attJson.getString("file");
		if (fileStrs != null && !StringUtils.isEmpty(fileStrs.trim())) {
			String[] filesStrs = fileStrs.trim().split(";");
			files = new ArrayList<String>();
			for (String fileStr : filesStrs)
				files.add(fileStr);
		}
		String archiveStrs = attJson.getString("archive");
		if (archiveStrs != null && !StringUtils.isEmpty(archiveStrs.trim())) {
			String[] archivesStrs = archiveStrs.split(";");
			archives = new ArrayList<String>();
			for (String archStr : archivesStrs)
				archives.add(archStr);
		}

		String mapType = attJson.getString("mapType");
		if (mapType.equals("1")) {
			this.streaming = new OwStreaming(attJson);
		} else if (mapType.equals("2")) {
			this.pipes = new OwPipes(attJson);
		}
	}

	private void setPropByVersion(boolean isNewVer, String oldKey, String newKey, String value) {
		if (value != null && !value.trim().isEmpty()) {
			OwProperty propTmp = new OwProperty();
			if (isNewVer) {
				propTmp.setName(newKey);
			} else {
				propTmp.setName(oldKey);
			}
			propTmp.setValue(value.trim());
			super.getConfiguration().getPropertys().add(propTmp);
		}
	}

	public OwMapReduce() {
		super();
	}

	public static OwMapReduce parseXml(Element ele) {
		OwMapReduce mapreduce = new OwMapReduce();
		mapreduce.parseXmls(ele);

		if (ele.element("streaming") != null) {
			mapreduce.streaming = OwStreaming.parseXml(ele.element("streaming"));
		}
		if (ele.element("pipes") != null) {
			mapreduce.pipes = OwPipes.parseXml(ele.element("pipes"));
		}
		mapreduce.config_class = ele.elementText("config-class");

		List<Element> fileEs = ele.elements("file");
		if (fileEs != null) {
			mapreduce.files = new ArrayList<String>();
			for (Element fileE : fileEs) {
				mapreduce.files.add(fileE.getTextTrim());
			}
		}
		List<Element> archiveEs = ele.elements("archive");
		if (archiveEs != null) {
			mapreduce.archives = new ArrayList<String>();
			for (Element archiveE : archiveEs) {
				mapreduce.archives.add(archiveE.getTextTrim());
			}
		}
		return mapreduce;
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
				String moreParams = "";
				for (OwProperty prop : props) {
					if (MAPRED_MAPPER_CLASS.equals(prop.getName()) || MAPREDUCE_JOB_MAP_CLASS.equals(prop.getName())) {
						attObj.put(MAP_CLASS_TAG, prop.getValue());
					} else if (MAPRED_REDUCER_CLASS.equals(prop.getName()) || MAPREDUCE_JOB_REDUCE_CLASS.equals(prop.getName())) {
						attObj.put(REDUCER_CLASS_TAG, prop.getValue());
					} else if (MAPRED_INPUT_DIR.equals(prop.getName()) || MAPREDUCE_INPUT_FILEINPUTFORMAT_INPUTDIR.equals(prop.getName())) {
						attObj.put(MAP_INPUT_DIR_TAG, prop.getValue());
					} else if (MAP_OUTPUT_DIR_TAG.equals(prop.getName()) || MAPREDUCE_OUTPUT_FILEOUTPUTFORMAT_OUTPUTDIR.equals(prop.getName())) {
						attObj.put(MAP_OUTPUT_DIR_TAG, prop.getValue());
					} else if (MAPREDUCE_COMBINE_CLASS.equals(prop.getName()) || MAPREDUCE_JOB_COMBINE_CLASS.equals(prop.getName())) {
						attObj.put(COMBINE_CLASS_TAG, prop.getValue());
					} else if (MAPRED_OUTPUT_KEY_CLASS.equals(prop.getName()) || MAPREDUCE_JOB_OUTPUT_KEY_CLASS.equals(prop.getName())) {
						attObj.put(JOB_OUTPUT_KEY_CLASS_TAG, prop.getValue());
					} else if (MAPRED_OUTPUT_VALUE_CLASS.equals(prop.getName()) || MAPREDUCE_JOB_OUTPUT_VALUE_CLASS.equals(prop.getName())) {
						attObj.put(JOB_OUTPUT_VALUE_CLASS_TAG, prop.getValue());
					} else if (MAPRED_MAPOUTPUT_KEY_CLASS.equals(prop.getName()) || MAPREDUCE_MAP_OUTPUT_KEY_CLASS.equals(prop.getName())) {
						attObj.put(MAP_OUTPUT_KEY_CLASS_TAG, prop.getValue());
					} else if (MAPRED_MAPOUTPUT_VALUE_CLASS.equals(prop.getName()) || MAPREDUCE_MAP_OUTPUT_VALUE_CLASS.equals(prop.getName())) {
						attObj.put(MAP_OUTPUT_VALUE_CLASS_TAG, prop.getValue());
					} else if (MAPRED_INPUT_FORMAT_CLASS.equals(prop.getName()) || MAPREDUCE_INPUT_FORMAT_CLASS.equals(prop.getName())) {
						attObj.put(JOB_INPUT_FORMAT_CLASS_TAG, prop.getValue());
					} else if (MAPRED_OUTPUT_FORMAT_CLASS.equals(prop.getName()) || MAPREDUCE_OUTPUT_FORMAT_CLASS.equals(prop.getName())) {
						attObj.put(JOB_OUTPUT_FORMAT_CLASS_TAG, prop.getValue());
					} else if (MAPRED_MAP_TASKS.equals(prop.getName())) {
						attObj.put(MAP_TASKS_TAG, prop.getValue());
					} else if (MAPRED_REDUCE_TASKS.equals(prop.getName())) {
						attObj.put(REDUCE_TASKS_TAG, prop.getValue());
					} else if (MAPRED_VERSION.equals(prop.getName())) {
						attObj.put(VERSION_TAG, prop.getValue());
					} else if (!super.isPublicPara(prop.getName())) {
						if (!"".equals(moreParams)) {
							moreParams += ";";
						}
						moreParams += (prop.getName() + "=" + prop.getValue());
					}
				}
				if (!"".equals(moreParams)) {
					attObj.put("moreParas", moreParams);
				}
			}
		}
		if (config_class != null) {
			attObj.put("config-class", config_class);
		}

		if (files != null && !files.isEmpty()) {
			String fis = "";
			for (String file : files) {
				if (!fis.isEmpty()) {
					fis += ";";
				}
				fis += file;
			}
			attObj.put("file", fis);
		}

		if (archives != null && !archives.isEmpty()) {
			String ais = "";
			for (String arch : archives) {
				if (!ais.isEmpty()) {
					ais += ";";
				}
				ais += arch;
			}
			attObj.put("archive", ais);
		}

		if (this.streaming != null) {
			attObj = streaming.toAttributes(attObj);
		} else if (this.pipes != null) {
			attObj = pipes.toAttributes(attObj);
		} else {
			attObj.put("mapType", "0");
		}

		return attObj.toJSONString();
	}

	public OwStreaming getStreaming() {
		return streaming;
	}

	public void setStreaming(OwStreaming streaming) {
		this.streaming = streaming;
	}

	public OwPipes getPipes() {
		return pipes;
	}

	public void setPipes(OwPipes pipes) {
		this.pipes = pipes;
	}

	public String getConfig_class() {
		return config_class;
	}

	public void setConfig_class(String config_class) {
		this.config_class = config_class;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public List<String> getArchives() {
		return archives;
	}

	public void setArchives(List<String> archives) {
		this.archives = archives;
	}
}
