package com.atomic.hadoop.common.oozie.model.workflow.model.help;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Element;

public class OwPipes {
	private static final String PIPES_MAP="pipes.map";
	private static final String PIPES_REDUCE="pipes.reduce";
	private static final String PIPES_IMPUTFORMAT="pipes.inputformat";
	private static final String PIPES_PARTITIONER="pipes.partitioner";
	private static final String PIPES_WRITER="pipes.writer";
	private static final String PIPES_PROGRAM="pipes.program";
	
	private String map;
	private String reduce;
	private String inputformat;
	private String partitioner;
	private String writer;
	private String program;
	public OwPipes(JSONObject pipesJson) {
		super();
		if(pipesJson!=null){
			this.map=pipesJson.getString(PIPES_MAP);
			this.reduce=pipesJson.getString(PIPES_REDUCE);
			this.inputformat=pipesJson.getString(PIPES_IMPUTFORMAT);
			this.partitioner=pipesJson.getString(PIPES_PARTITIONER);
			this.writer=pipesJson.getString(PIPES_WRITER);
			this.program=pipesJson.getString(PIPES_PROGRAM);
		}
	}
	
	public JSONObject toAttributes(JSONObject attObj) {
		if(this.map!=null){
			attObj.put(PIPES_MAP, map);
		}
		if(this.reduce!=null){
			attObj.put(PIPES_REDUCE, reduce);
		}
		if(this.inputformat!=null){
			attObj.put(PIPES_IMPUTFORMAT, inputformat);
		}
		if(this.partitioner!=null){
			attObj.put(PIPES_PARTITIONER, partitioner);
		}
		if(this.writer!=null){
			attObj.put(PIPES_WRITER, writer);
		}
		if(this.program!=null){
			attObj.put(PIPES_PROGRAM, program);
		}
		return attObj;
	}
	public OwPipes() {
		super();
	}
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
	}
	public String getReduce() {
		return reduce;
	}
	public void setReduce(String reduce) {
		this.reduce = reduce;
	}
	public String getInputformat() {
		return inputformat;
	}
	public void setInputformat(String inputformat) {
		this.inputformat = inputformat;
	}
	public String getPartitioner() {
		return partitioner;
	}
	public void setPartitioner(String partitioner) {
		this.partitioner = partitioner;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inputformat == null) ? 0 : inputformat.hashCode());
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result
				+ ((partitioner == null) ? 0 : partitioner.hashCode());
		result = prime * result + ((program == null) ? 0 : program.hashCode());
		result = prime * result + ((reduce == null) ? 0 : reduce.hashCode());
		result = prime * result + ((writer == null) ? 0 : writer.hashCode());
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
		OwPipes other = (OwPipes) obj;
		if (inputformat == null) {
			if (other.inputformat != null)
				return false;
		} else if (!inputformat.equals(other.inputformat))
			return false;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (partitioner == null) {
			if (other.partitioner != null)
				return false;
		} else if (!partitioner.equals(other.partitioner))
			return false;
		if (program == null) {
			if (other.program != null)
				return false;
		} else if (!program.equals(other.program))
			return false;
		if (reduce == null) {
			if (other.reduce != null)
				return false;
		} else if (!reduce.equals(other.reduce))
			return false;
		if (writer == null) {
			if (other.writer != null)
				return false;
		} else if (!writer.equals(other.writer))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "OwPipes [map=" + map + ", reduce=" + reduce + ", inputformat="
				+ inputformat + ", partitioner=" + partitioner + ", writer="
				+ writer + ", program=" + program + "]";
	}
	public static OwPipes parseXml(Element pipE) {
		OwPipes pipes = new OwPipes();
		pipes.map = pipE.elementTextTrim("map");
		pipes.reduce = pipE.elementTextTrim("reduce");
		pipes.inputformat = pipE.elementTextTrim("inputformat");
		pipes.partitioner = pipE.elementTextTrim("partitioner");
		pipes.writer = pipE.elementTextTrim("writer");
		pipes.program = pipE.elementTextTrim("program");
		return pipes;
	}

}
