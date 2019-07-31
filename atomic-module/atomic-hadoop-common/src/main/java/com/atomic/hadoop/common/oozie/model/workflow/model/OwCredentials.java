package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("credentials")
public class OwCredentials {
	@XStreamImplicit(itemFieldName = "credential")
	private List<OwCredential> credentials;

	public OwCredentials(ConfCredentialVo conHbaseVo, ConfCredentialVo conHcatlogVo,
						 ConfCredentialVo conHive2Vo) {
		credentials = new ArrayList<OwCredential>();
		if(conHbaseVo!=null){
			credentials.add(new OwCredential(conHbaseVo));
		}
		if(conHcatlogVo!=null){
			credentials.add(new OwCredential(conHcatlogVo));
		}
		if(conHive2Vo!=null){
			credentials.add(new OwCredential(conHive2Vo));
		}
	}

	public OwCredentials() {
		// TODO 自动生成的构造函数存根
	}

	public List<OwCredential> getCredentials() {
		return credentials;
	}

	public void setCredentials(List<OwCredential> credentials) {
		this.credentials = credentials;
	}

	public static OwCredentials parseXml(Element credentialsElm) {
		OwCredentials ocs =new OwCredentials();
		List<Element> credEs = credentialsElm.elements("credential");
		ocs.credentials=OwCredential.parseXml(credEs);
		return ocs;
	}
}
