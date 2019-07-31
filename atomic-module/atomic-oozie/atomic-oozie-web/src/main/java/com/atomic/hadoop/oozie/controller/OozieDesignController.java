package com.atomic.hadoop.oozie.controller;

import cn.hutool.core.collection.CollUtil;
import com.atomic.common.core.domain.R;
import com.atomic.common.utils.StringUtils;
import com.atomic.hadoop.common.oozie.service.IWorkflowService;
import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.atomic.hadoop.oozie.domain.OozieHiveSql;
import com.atomic.hadoop.oozie.domain.OoziePackageTemplate;
import com.atomic.hadoop.oozie.service.IOozieHiveSqlService;
import com.atomic.hadoop.oozie.service.IOoziePackageTemplateService;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.service.IOozieCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/5/4/004 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Controller
@RequestMapping("oozie")
public class OozieDesignController {

    private String prefix = "oozie/design";

    @Autowired
    private IWorkflowService workflowService;
    @Autowired
    private IOozieHiveSqlService oozieHiveSqlService;
    @Autowired
    private IOoziePackageTemplateService ooziePackageTemplateService;
    @Autowired
    private IOozieCredentialService oozieCredentialService;

    @RequestMapping("design")
    public String toDesign(ModelMap mmap) {
        String flowJson = "{\"node\":[{\"x\":20,\"y\":20,\"text\":\"start\",\"type\":\"start\",\"nodeid\":1},{\"x\":753,\"y\":458,\"text\":\"end\",\"type\":\"end\",\"nodeid\":2},{\"x\":326,\"y\":206,\"text\":\"fs1\",\"type\":\"1\",\"nodeid\":3,\"attribute\":{\"fs_delete\":\"/user/example/data.txt\",\"fs_mkdir\":\"\",\"move\":\"\",\"chmod\":\"\",\"touchz\":\"\",\"chgrp\":\"\",\"queue\":\"cdrapp\"}}],\"linkarray\":[{\"node1id\":1,\"node2id\":3},{\"node1id\":3,\"node2id\":2}]}";
        mmap.put("flowJson",flowJson);
        return prefix + "/design";
    }

    @RequestMapping("design/parkage/{packageId}")
    public String toDesignPackage(@PathVariable("packageId") String packageId,ModelMap mmap) {
        OoziePackageTemplate ooziePackageTemplate = ooziePackageTemplateService.selectOoziePackageTemplateById(packageId);
        mmap.put("ooziePackageTemplate",ooziePackageTemplate);
        return prefix + "/packageDesign";
    }

    @RequestMapping("action/{action}")
    public String toFsAction(@PathVariable("action") String action, HttpServletRequest request, ModelMap mmap) {
        String nodeId = request.getParameter("nodeId");
        String mapType = request.getParameter("mapType");
        mmap.put("nodeId", nodeId);
        mmap.put("mapType", mapType);
        return prefix + "/action/" + action;
    }

    @RequestMapping("getFlowXml")
    @ResponseBody
    public String getFlowXml(HttpServletRequest request, HttpServletResponse response){
        String packageId = request.getParameter("taskId");
        String flowJson = request.getParameter("flowJson");
        OoziePackageTemplate ooziePackageTemplate = ooziePackageTemplateService.selectOoziePackageTemplateById(packageId);
        String flowXml = workflowService.workflowJsonToXml(ooziePackageTemplate.getNameEn(), flowJson);
        return flowXml;
    }

    /**
     * 新版获取workflow.xml
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getWorkFlowXml")
    @ResponseBody
    public String getWorkFlowXml(HttpServletRequest request, HttpServletResponse response){
        String packageId = request.getParameter("taskId");
        String flowJson = request.getParameter("flowJson");
        OoziePackageTemplate ooziePackageTemplate = ooziePackageTemplateService.selectOoziePackageTemplateById(packageId);
        HadoopPlatform hadoopPlatform = ooziePackageTemplate.getHadoopPlatform();
        List<ConfCredentialVo> credList = oozieCredentialService.getPackageCredList(hadoopPlatform.getId(), ooziePackageTemplate.getConHive2Id(), ooziePackageTemplate.getConHcatalogId(), ooziePackageTemplate.getConHbaseId());
        String flowXml = "";
        if (credList !=null && credList.size()>0){
            flowXml = workflowService.workflowJsonToXml(ooziePackageTemplate.getNameEn(), flowJson, credList);
        }else {
            flowXml = workflowService.workflowJsonToXml(ooziePackageTemplate.getNameEn(), flowJson);
        }
        return flowXml;
    }

    @RequestMapping("/validFlow")
    @ResponseBody
    public String validFlow(HttpServletRequest request){
        String flowJson = request.getParameter("flowJson");

        String validStr = null;
        try {
            validStr = workflowService.validJson("xx",flowJson);
        } catch (Exception e) {
            e.printStackTrace();
            validStr = e.getMessage();
        }
        if (validStr == null) {
            validStr = "流程校验通过！";
        } else {
            validStr = "流程校验异常：" + validStr;
        }
        return validStr;
    }

    @RequestMapping("saveFlow")
    @ResponseBody
    public R saveFlow(HttpServletRequest request){
        String flowJson = request.getParameter("flowJson");
        String packageId = request.getParameter("taskId");
        return R.ok().put("data","success");
    }

    @PostMapping("saveWorkflow")
    @ResponseBody
    public R saveWorkflow(HttpServletRequest request) throws Exception{
        String flowJson = request.getParameter("flowJson");
        String packageId = request.getParameter("taskId");
        int result = ooziePackageTemplateService.saveOoziePackageTemplateAndUploadHdfs(packageId,flowJson);
        if (result>0){
            return R.ok().put("data","success");
        }
        return R.ok().put("data","error");
    }

    @RequestMapping("toHive2SQL")
    public String toHive2SQL(OozieHiveSql oozieHiveSql, ModelMap mmap){
        List<OozieHiveSql> oozieHiveSqls = oozieHiveSqlService.selectOozieHiveSqlList(oozieHiveSql);
        if (CollUtil.isEmpty(oozieHiveSqls)){
            oozieHiveSql.setHsql("");
        }else{
            oozieHiveSql=oozieHiveSqls.get(0);
        }
        mmap.put("oozieHiveSql",oozieHiveSql);
        return prefix + "/action/hive2Sql";
    }

    @PostMapping("saveHSQLToDB")
    @ResponseBody
    public R saveHSQLToDB(OozieHiveSql oozieHiveSql) throws IOException {
        oozieHiveSql.setHsql(oozieHiveSql.getHsql().replace("%2E","\n"));
        int result = oozieHiveSqlService.saveHSQLToDB(oozieHiveSql);
        return R.ok().put("data",result);
    }

    @RequestMapping("openRetry")
    public String openRetry(){
        return prefix + "/retryCred";
    }

}
