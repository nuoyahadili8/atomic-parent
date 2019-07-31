package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class OwStreaming {
	private static final String MAPPER_SCRIPT="mapper.script";
	private static final String REDUCE_SCRIPT="reduce.script";
	private static final String RECORD_READER="record.reader";
	private static final String RECORD_READER_MAP="record.reader.map";
	private static final String STREAMING_ENV="streaming.env";
	private String mapper;
	private String reducer;
	private String record_reader;
	private List<String> record_reader_mapping;
	private List<String> env;
	
	public OwStreaming(JSONObject streamJson) {
		super();
		if(streamJson!=null){
			this.mapper=streamJson.getString(MAPPER_SCRIPT);
			this.reducer=streamJson.getString(REDUCE_SCRIPT);
			this.record_reader=streamJson.getString(RECORD_READER);
			JSONArray rmappings = streamJson.getJSONArray(RECORD_READER_MAP);
			if(rmappings!=null){
				this.record_reader_mapping = new ArrayList<String>();
				for(int i=0;i<rmappings.size();i++){
					record_reader_mapping.add(rmappings.getString(i));
				}
			}
			JSONArray envs = streamJson.getJSONArray(STREAMING_ENV);
			if(envs!=null){
				this.env = new ArrayList<String>();
				for(int i=0;i<envs.size();i++){
					env.add(envs.getString(i));
				}
			}
		}
	}
	public OwStreaming() {
		super();
	}

	public String getMapper() {
		return mapper;
	}
	public void setMapper(String mapper) {
		this.mapper = mapper;
	}
	public String getReducer() {
		return reducer;
	}
	public void setReducer(String reducer) {
		this.reducer = reducer;
	}
	public String getRecord_reader() {
		return record_reader;
	}
	public void setRecord_reader(String record_reader) {
		this.record_reader = record_reader;
	}
	public List<String> getRecord_reader_mapping() {
		return record_reader_mapping;
	}
	public void setRecord_reader_mapping(List<String> record_reader_mapping) {
		this.record_reader_mapping = record_reader_mapping;
	}
	public List<String> getEnv() {
		return env;
	}
	public void setEnv(List<String> env) {
		this.env = env;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((env == null) ? 0 : env.hashCode());
		result = prime * result + ((mapper == null) ? 0 : mapper.hashCode());
		result = prime * result
				+ ((record_reader == null) ? 0 : record_reader.hashCode());
		result = prime
				* result
				+ ((record_reader_mapping == null) ? 0 : record_reader_mapping
						.hashCode());
		result = prime * result + ((reducer == null) ? 0 : reducer.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OwStreaming other = (OwStreaming) obj;
		if (env == null) {
			if (other.env != null)
				return false;
		} else if (!env.equals(other.env))
			return false;
		if (mapper == null) {
			if (other.mapper != null)
				return false;
		} else if (!mapper.equals(other.mapper))
			return false;
		if (record_reader == null) {
			if (other.record_reader != null)
				return false;
		} else if (!record_reader.equals(other.record_reader))
			return false;
		if (record_reader_mapping == null) {
			if (other.record_reader_mapping != null)
				return false;
		} else if (!record_reader_mapping.equals(other.record_reader_mapping))
			return false;
		if (reducer == null) {
			if (other.reducer != null)
				return false;
		} else if (!reducer.equals(other.reducer))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "OwStreaming [mapper=" + mapper + ", reducer=" + reducer
				+ ", record_reader=" + record_reader
				+ ", record_reader_mapping=" + record_reader_mapping + ", env="
				+ env + "]";
	}
	public static OwStreaming parseXml(Element sele) {
		OwStreaming steaming=new OwStreaming();
		steaming.mapper = sele.elementTextTrim("mapper");
		steaming.reducer = sele.elementTextTrim("reducer");
		steaming.record_reader = sele.elementTextTrim("record-reader");
		
		List<Element> mapEs = sele.elements("record-reader-mapping");
		if (mapEs != null) {
			steaming.record_reader_mapping = new ArrayList<String>();
			for (Element mapE : mapEs) {
				steaming.record_reader_mapping.add(mapE.getTextTrim());
			}
		}
		
		List<Element> envEs = sele.elements("env");
		if (envEs != null) {
			steaming.env = new ArrayList<String>();
			for (Element envE : envEs) {
				steaming.env.add(envE.getTextTrim());
			}
		}
		
		return steaming;
	}
	public JSONObject toAttributes(JSONObject attObj) {
		if(this.mapper!=null){
			attObj.put(MAPPER_SCRIPT, mapper);
		}
		if(this.reducer!=null){
			attObj.put(REDUCE_SCRIPT, mapper);
		}
		if(this.record_reader!=null){
			attObj.put(RECORD_READER, mapper);
		}
		if(this.record_reader_mapping!=null){
			attObj.put(RECORD_READER_MAP, record_reader_mapping);
		}
		if(this.env!=null){
			attObj.put(STREAMING_ENV, env);
		}
		return attObj;
	}
}
