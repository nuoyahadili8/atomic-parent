package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.atomic.hadoop.common.oozie.model.workflow.model.help.OwProperty;
import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("credential")
public class OwCredential {
	private static final String CREDENTIAL_TYPE_HIVE2="hive2";
	private static final String CREDENTIAL_TYPE_HBASE="hbase";
	private static final String CREDENTIAL_TYPE_HCAT="hcat";
	
	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private String type;
	
	
	@XStreamImplicit(itemFieldName = "property")
	private List<OwProperty> propertys;

	public OwCredential(ConfCredentialVo conAutoVo) {
		name = conAutoVo.getName();
		type = conAutoVo.getType();
		
		propertys = new ArrayList<OwProperty>();
		
		if(CREDENTIAL_TYPE_HIVE2.equalsIgnoreCase(conAutoVo.getType())){
			OwProperty property1 =new OwProperty();
			property1.setName("hive2.jdbc.url");
			property1.setValue(conAutoVo.getUrl());
			propertys.add(property1);
			
			OwProperty property2 =new OwProperty();
			property2.setName("hive2.server.principal");
			property2.setValue(conAutoVo.getPrincipal());
			propertys.add(property2);			
		}else if(CREDENTIAL_TYPE_HBASE.equalsIgnoreCase(conAutoVo.getType())){
			
		}else if(CREDENTIAL_TYPE_HCAT.equalsIgnoreCase(conAutoVo.getType())){
			OwProperty property1 =new OwProperty();
			property1.setName("hcat.metastore.uri");
			property1.setValue(conAutoVo.getUrl());
			propertys.add(property1);
			
			OwProperty property2 =new OwProperty();
			property2.setName("hcat.metastore.principal");
			property2.setValue(conAutoVo.getPrincipal());
			propertys.add(property2);			
		}
		
	}

	public OwCredential() {
		// TODO 自动生成的构造函数存根
	}

	public List<OwProperty> getPropertys() {
		return propertys;
	}

	public void setPropertys(List<OwProperty> propertys) {
		this.propertys = propertys;
	}

	public static List<OwCredential> parseXml(List<Element> credEs) {
		List<OwCredential>  creds =new ArrayList<OwCredential> ();
		for(Element element:credEs){
			OwCredential oc = new OwCredential();
			oc.name = element.attributeValue("name");
			oc.type = element.attributeValue("type");
			
			List<Element> propEs = element.elements("property");
			oc.propertys=OwProperty.parseXml(propEs);
			creds.add(oc);
		}
		return creds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
