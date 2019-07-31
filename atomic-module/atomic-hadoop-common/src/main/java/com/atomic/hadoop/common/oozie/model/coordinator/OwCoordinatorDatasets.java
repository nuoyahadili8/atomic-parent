package com.atomic.hadoop.common.oozie.model.coordinator;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("datasets")
public class OwCoordinatorDatasets {
	private String include;
	
	@XStreamImplicit(itemFieldName = "dataset")
	private List<OwCoordinatorDataset> datasets = new ArrayList<OwCoordinatorDataset>();
	
	@XStreamImplicit(itemFieldName = "async-dataset")
	private List<OwCoordinatorAsyncDataset> async_datasets = new ArrayList<OwCoordinatorAsyncDataset>();
	
	public static OwCoordinatorDatasets parseXml(Element element){
		OwCoordinatorDatasets datasetsModel=new OwCoordinatorDatasets();
		List<Element> dataset_elements=element.elements();
		List<OwCoordinatorDataset> datasets_vo = new ArrayList<OwCoordinatorDataset>();
		for(Element dataset_element:dataset_elements){
			OwCoordinatorDataset datasetVo=new OwCoordinatorDataset();
			datasetVo.setName(dataset_element.attributeValue("name"));
			datasetVo.setFrequency(dataset_element.attributeValue("frequency"));
			datasetVo.setInitial_instance(dataset_element.attributeValue("initial-instance"));
			datasetVo.setTimezone(dataset_element.attributeValue("timezone"));
			datasetVo.setUri_template(dataset_element.element("uri-template").getText());
			datasetVo.setDone_flag(dataset_element.element("done-flag").getText());
			datasets_vo.add(datasetVo);
		}
		datasetsModel.setDatasets(datasets_vo);
		return datasetsModel;
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	public List<OwCoordinatorDataset> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<OwCoordinatorDataset> datasets) {
		this.datasets = datasets;
	}

	public List<OwCoordinatorAsyncDataset> getAsync_datasets() {
		return async_datasets;
	}

	public void setAsync_datasets(List<OwCoordinatorAsyncDataset> async_datasets) {
		this.async_datasets = async_datasets;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((async_datasets == null) ? 0 : async_datasets.hashCode());
		result = prime * result
				+ ((datasets == null) ? 0 : datasets.hashCode());
		result = prime * result + ((include == null) ? 0 : include.hashCode());
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
		OwCoordinatorDatasets other = (OwCoordinatorDatasets) obj;
		if (async_datasets == null) {
			if (other.async_datasets != null){
				return false;
			}
		} else if (!async_datasets.equals(other.async_datasets)){
			return false;
		}
		if (datasets == null) {
			if (other.datasets != null){
				return false;
			}
		} else if (!datasets.equals(other.datasets)){
			return false;
		}
		if (include == null) {
			if (other.include != null){
				return false;
			}
		} else if (!include.equals(other.include)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OwCoordinatorDatasets [include=" + include + ", datasets="
				+ datasets + ", async_datasets=" + async_datasets + "]";
	}
	
	
}
