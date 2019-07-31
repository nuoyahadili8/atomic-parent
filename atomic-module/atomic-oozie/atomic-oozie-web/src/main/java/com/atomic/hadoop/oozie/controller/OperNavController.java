package com.atomic.hadoop.oozie.controller;

import com.atomic.hadoop.tenant.domain.HadoopProject;
import com.atomic.hadoop.tenant.service.IHadoopProjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/5/19/019 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Controller
@RequestMapping("ooize/opernav")
public class OperNavController {

    private String prefix = "oozie/opernav";

    @Autowired
    private IHadoopProjectService hadoopProjectService;

    @RequiresPermissions("hadoop:hadoopProject:list")
    @RequestMapping("index")
    public String index(ModelMap mmap){
        List<HadoopProject> list = hadoopProjectService.selectHadoopProjectList(new HadoopProject());
        mmap.put("projectList",list);
        return prefix + "/index";
    }
}
