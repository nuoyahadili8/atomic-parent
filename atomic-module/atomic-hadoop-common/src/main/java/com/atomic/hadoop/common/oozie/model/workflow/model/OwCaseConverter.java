package com.atomic.hadoop.common.oozie.model.workflow.model;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class OwCaseConverter implements Converter {

	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(OwCase.class);  
	}

	 public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext acontext) { 
		 OwCase owCase = (OwCase) value;  
		  if (owCase != null) {  
		   if (null != owCase.getTo()) {  
		    writer.addAttribute("to", owCase.getTo());  
		   }  
		   writer.setValue(owCase.getCASE() == null ? "" : owCase.getCASE());  
		  }  
	}

	@Override
	 public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext arg1) { 
		OwCase owCase = new OwCase();  
		  String to = reader.getAttribute("to");  
		  owCase.setTo(to);
		  
		  String value = reader.getValue();  
		  
		  owCase.setCASE(value);  
		  
		  return owCase; 
	}

}
