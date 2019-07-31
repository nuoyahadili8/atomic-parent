package com.atomic.hadoop.common.oozie.model.workflow.model.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.oozie.model.workflow.design.FlowNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwAbstractActionNodeFS;
import com.atomic.hadoop.common.oozie.model.workflow.model.OwActionNode;
import com.atomic.hadoop.common.oozie.model.workflow.model.help.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;


@XStreamAlias("fs")
public class OwFs extends OwAbstractActionNodeFS implements OwActionNode {
	@XStreamImplicit(itemFieldName = "delete")
	private List<OwDelete> deletes;
	@XStreamImplicit(itemFieldName = "mkdir")
	private List<OwMkdir> mkdirs;
	@XStreamImplicit(itemFieldName = "move")
	private List<OwMove> moves;
	@XStreamImplicit(itemFieldName = "chmod")
	private List<OwChmod> chmods;
	@XStreamImplicit(itemFieldName = "touchz")
	private List<OwTouchz> touchzs;
	@XStreamImplicit(itemFieldName = "chgrp")
	private List<OwChgrp> chgrps;
	
	public OwFs(){
		super();
	}
	public OwFs(FlowNode flowNode) {
		super(flowNode);

		Object atts = flowNode.getAttribute();
		JSONObject attObj = JSON.parseObject(atts.toString());
		String delete = attObj.getString("fs_delete");	     
	    if(delete!=null && !delete.trim().isEmpty()){
	    	String[] deleteStrs = delete.trim().split("[;]");
	    	deletes =new ArrayList<OwDelete>();
	    	for(String del:deleteStrs){
	    		if(!del.trim().isEmpty()){
		    		OwDelete owDel =new OwDelete(del.trim());
		    		deletes.add(owDel);
	    		}
	    	}
	    }
	    
	    String mkdir = attObj.getString("fs_mkdir");
	    if(mkdir!=null && !mkdir.trim().isEmpty()){
	    	String[] mkdirStrs = mkdir.trim().split("[;]");
	    	mkdirs =new ArrayList<OwMkdir>();
	    	for(String mk:mkdirStrs){
	    		if(!mk.trim().isEmpty()){
		    		OwMkdir owMk =new OwMkdir(mk.trim());
		    		mkdirs.add(owMk);
	    		}
	    	}
	    }
	    
	    String move = attObj.getString("move");
	    if(move!=null && !move.trim().isEmpty()){
	    	String[] moveStrs = move.trim().split("[;]");
	    	moves =new ArrayList<OwMove>();
	    	for(String mv:moveStrs){
	    		if(!mv.trim().isEmpty()){
	    			OwMove owMv =new OwMove(mv.trim());
	    			moves.add(owMv);
	    		}
	    	}
	    }
	    String chmod = attObj.getString("chmod");
	    if(chmod!=null && !chmod.trim().isEmpty()){
	    	String[] chmodStrs = chmod.trim().split("[;]");
	    	chmods =new ArrayList<OwChmod>();
	    	for(String chmodstr:chmodStrs){
	    		if(!chmodstr.trim().isEmpty()){
	    			OwChmod owCmd =new OwChmod(chmodstr.trim().split(":"));
	    			chmods.add(owCmd);
	    		}
	    	}
	    }
	    String touchz = attObj.getString("touchz");
	    if(touchz!=null && !touchz.trim().isEmpty()){
	    	String[] touchzStrs = touchz.trim().split("[;]");
	    	touchzs =new ArrayList<OwTouchz>();
	    	for(String tou:touchzStrs){
	    		if(!tou.trim().isEmpty()){
	    			OwTouchz owMk =new OwTouchz(tou.trim());
	    			touchzs.add(owMk);
	    		}
	    	}
	    }
	    String chgrp = attObj.getString("chgrp");
	    if(chgrp!=null && !chgrp.trim().isEmpty()){
	    	String[] chgrpStrs = chgrp.trim().split("[;]");
	    	chgrps =new ArrayList<OwChgrp>();
	    	for(String chgrpstr:chgrpStrs){
	    		if(!chgrpstr.trim().isEmpty()){
	    			OwChgrp owChg =new OwChgrp(chgrpstr.trim().split(":"));
	    			chgrps.add(owChg);
	    		}
	    	}
	    }
	}

	public static OwFs parseXml(Element element) {
		OwFs fs =new OwFs();
		fs.parseXmls(element);
		List<Element> des = element.elements("delete");
		if(des!=null && !des.isEmpty()){
			fs.deletes = OwDelete.parseXml(des);
		}
		List<Element> mes = element.elements("mkdir");
		if(mes!=null && !mes.isEmpty()){
			fs.mkdirs = OwMkdir.parseXml(mes);
		}
		List<Element> mos = element.elements("move");
		if(mos!=null && !mos.isEmpty()){
			fs.moves = OwMove.parseXml(mos);
		}
		List<Element> chms = element.elements("chmod");
		if(chms!=null && !chms.isEmpty()){
			fs.chmods = OwChmod.parseXml(chms);
		}
		List<Element> tos = element.elements("touchz");
		if(tos!=null && !tos.isEmpty()){
			fs.touchzs = OwTouchz.parseXml(tos);
		}
		List<Element> chgs = element.elements("chgrp");
		if(chgs!=null && !chgs.isEmpty()){
			fs.chgrps = OwChgrp.parseXml(chgs);
		}

		return fs;
	}
	public Object toAttributes() {
		JSONObject attObj = new JSONObject();
		if(this.deletes!=null){
			String dels="";
			for(OwDelete owDel:deletes){
				if(!dels.isEmpty()){
					dels +=";";
				}
				
				dels += owDel.getPath();
			}
			attObj.put("delete", dels);
		}
		if(this.mkdirs!=null){
			String mks="";
			for(OwMkdir owDel:mkdirs){
				if(!mks.isEmpty()){
					mks +=";";
				}
				
				mks += owDel.getPath();
			}
			attObj.put("mkdir", mks);
		}
		if(this.moves!=null){
			String mvs="";
			for(OwMove owmv:moves){
				if(!mvs.isEmpty()){
					mvs +=";";
				}				
				mvs += (owmv.source+":"+owmv.target);
			}
			attObj.put("move", mvs);
		}
		if(this.chmods!=null){
			String chms="";
			for(OwChmod ochmod:chmods){
				if(!chms.isEmpty()){
					chms +=";";
				}				
				chms += (ochmod.getPath()+":"+ochmod.permissions+":"+ochmod.dir_files);
			}
			attObj.put("chmod", chms);
		}
		if(this.touchzs!=null){
			String tous="";
			for(OwTouchz owTou:touchzs){
				if(!tous.isEmpty()){
					tous +=";";
				}
				
				tous += owTou.getPath();
			}
			attObj.put("touchz", tous);
		}
		if(this.chgrps!=null){
			String chgs="";
			for(OwChgrp ochgs:chgrps){
				if(!chgs.isEmpty()){
					chgs +=";";
				}				
				chgs += (ochgs.getPath()+":"+ochgs.group+":"+ochgs.dir_files);
			}
			attObj.put("chgrp", chgs);
		}
		return attObj.toJSONString();
	}
}
