package com.atomic.hadoop.tenant.controller;

import cn.hutool.db.SqlRunner;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.sql.SqlExecutor;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.atomic.common.annotation.Log;
import com.atomic.common.core.controller.BaseController;
import com.atomic.common.core.domain.AjaxResult;
import com.atomic.common.core.domain.R;
import com.atomic.common.core.page.TableDataInfo;
import com.atomic.common.enums.BusinessType;
import com.atomic.common.utils.StringUtils;
import com.atomic.common.utils.poi.ExcelUtil;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.common.hdfs.HadoopConstants;
import com.atomic.hadoop.tenant.domain.HadoopDefaultTenant;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.domain.OozieCredential;
import com.atomic.hadoop.tenant.service.IHadoopDefaultTenantService;
import com.atomic.hadoop.tenant.service.IHadoopPlatformService;
import com.atomic.hadoop.tenant.service.IHadoopService;
import com.atomic.hadoop.tenant.service.IOozieCredentialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Hadoop平台模型 信息操作处理
 *
 * @author atomic
 * @date 2019-04-19
 */
@Slf4j
@Controller
@RequestMapping("/hadoop/hadoopPlatform")
public class HadoopPlatformController extends BaseController {
    private String prefix = "hadoop/hadoopPlatform";

    @Value("${Atomic.profile}")
    private String keytabBasePath;

    @Autowired
    private IHadoopPlatformService hadoopPlatformService;

    @Autowired
    private IHadoopDefaultTenantService hadoopDefaultTenantService;

    @Autowired
    private IHadoopService hadoopService;

    @Autowired
    private IOozieCredentialService oozieCredentialService;

    @RequiresPermissions("hadoop:hadoopPlatform:view")
    @GetMapping()
    public String hadoopPlatform() {
        return prefix + "/hadoopPlatform";
    }

    /**
     * 查询Hadoop平台模型列表
     */
    @RequiresPermissions("hadoop:hadoopPlatform:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HadoopPlatform hadoopPlatform) {
        startPage();
        List<HadoopPlatform> list = hadoopPlatformService.selectHadoopPlatformList(hadoopPlatform);
        return getDataTable(list);
    }


    /**
     * 导出Hadoop平台模型列表
     */
    @RequiresPermissions("hadoop:hadoopPlatform:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HadoopPlatform hadoopPlatform) {
        List<HadoopPlatform> list = hadoopPlatformService.selectHadoopPlatformList(hadoopPlatform);
        ExcelUtil<HadoopPlatform> util = new ExcelUtil(HadoopPlatform.class);
        return util.exportExcel(list, "hadoopPlatform");
    }

    /**
     * 新增Hadoop平台模型
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存Hadoop平台模型
     */
    @RequiresPermissions("hadoop:hadoopPlatform:add")
    @Log(title = "Hadoop平台模型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HadoopPlatform hadoopPlatform) {
        hadoopPlatform.setCreateUser(ShiroUtils.getLoginName());
        hadoopPlatform.setCreateTime(new Date());
        return toAjax(hadoopPlatformService.insertHadoopPlatform(hadoopPlatform));
    }

    /**
     * 修改Hadoop平台模型
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        HadoopPlatform hadoopPlatform = hadoopPlatformService.selectHadoopPlatformById(id);
        mmap.put("hadoopPlatform", hadoopPlatform);
        return prefix + "/edit";
    }

    /**
     * 修改保存Hadoop平台模型
     */
    @RequiresPermissions("hadoop:hadoopPlatform:edit")
    @Log(title = "Hadoop平台模型", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HadoopPlatform hadoopPlatform) {
        hadoopPlatform.setUpdateTime(new Date());
        hadoopPlatform.setUpdateUser(ShiroUtils.getLoginName());
        return toAjax(hadoopPlatformService.updateHadoopPlatform(hadoopPlatform));
    }

    /**
     * 删除Hadoop平台模型
     */
    @RequiresPermissions("hadoop:hadoopPlatform:remove")
    @Log(title = "Hadoop平台模型", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(hadoopPlatformService.deleteHadoopPlatformByIds(ids));
    }

    /**
     * 跳转Hadoop平台详情页面
     *
     * @param platformId
     * @param mmap
     * @return
     */
    @RequiresPermissions("hadoop:hadoopPlatform:edit")
    @RequestMapping("/openDetail/{platformId}")
    public String openDetail(@PathVariable("platformId") int platformId, ModelMap mmap) {
        HadoopPlatform hadoopPlatform = hadoopPlatformService.selectHadoopPlatformById(platformId + "");

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date createTime = hadoopPlatform.getCreateTime();
        if (createTime != null) {
            mmap.put("createTime", format.format(hadoopPlatform.getCreateTime()));
        }
        Date updateTime = hadoopPlatform.getUpdateTime();
        if (updateTime != null) {
            mmap.put("updateTime", format.format(hadoopPlatform.getUpdateTime()));
        }

        OozieCredential oozieCredential = new OozieCredential();
        oozieCredential.setPlatformId(platformId);
        List<OozieCredential> oozieCredentials = oozieCredentialService.selectOozieCredentialList(oozieCredential);

        OozieCredential hiveCredential=new OozieCredential();
        OozieCredential hcatLogCredential=new OozieCredential();
        for (OozieCredential oozieCredential1 : oozieCredentials){
            switch (oozieCredential1.getType()){
                case "hive2" : hiveCredential = oozieCredential1;
                case "hcat" : hcatLogCredential = oozieCredential1;
            }
        }

        mmap.put("hadoopPlatform", hadoopPlatform);
        mmap.put("hiveCredential", hiveCredential);
        mmap.put("hcatLogCredential", hcatLogCredential);
        return prefix + "/platformDetail";
    }

    @RequestMapping("/getDefaultTenantByPlatformId")
    @ResponseBody
    public R getDefaultTenantByPlatformId(@RequestParam("platformId") int platformId) throws IOException {
        HadoopDefaultTenant hadoopDefaultTenant = new HadoopDefaultTenant(platformId);
        List<HadoopDefaultTenant> hadoopDefaultTenants = hadoopDefaultTenantService.selectHadoopDefaultTenantList(hadoopDefaultTenant);
        if (hadoopDefaultTenants != null && hadoopDefaultTenants.size() > 0) {
            hadoopDefaultTenant = hadoopDefaultTenants.get(0);
            // 从数据下载配置文件
            hadoopService.downLoadHadoopConfigFile(keytabBasePath,hadoopDefaultTenant);
        }
        return R.ok().put("data", hadoopDefaultTenant);
    }

    /**
     * 上传keytab文件
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public R uploadFile(HttpServletRequest request) throws Exception {
        String platformId = request.getParameter("platformId");
        request.setCharacterEncoding("UTF-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map = multipartRequest.getFileMap();
        for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            multipartFile = (MultipartFile) map.get(obj);
        }
        /** 获取文件的后缀* */
        String filename = multipartFile.getOriginalFilename();
        String fullFileName = keytabBasePath + HadoopConstants.DIR_KEYTAB + HadoopConstants.FILE_SEPARATOR + platformId + HadoopConstants.FILE_SEPARATOR + filename;
        FileUtils.writeByteArrayToFile(new File(fullFileName), multipartFile.getBytes());
        String type=request.getParameter("type");
        return R.ok().put("file", filename).put("path", fullFileName);
    }

    /**
     * 获取配置hadoop平台的配置项
     *
     * @param platformId
     * @return
     */
    @RequestMapping("/getHadoopConfig")
    @ResponseBody
    public R getPlatformHadoopConfig(@RequestParam("platformId") String platformId) {
        HadoopPlatform hadoopPlatform = hadoopPlatformService.selectHadoopPlatformById(platformId);
        String conf = hadoopPlatform.getHadoopConfig();
        JSONObject hadoopSiteParamsAsJsonObject = null;
        if (StringUtils.isNotEmpty(conf)) {
            hadoopSiteParamsAsJsonObject = JSONObject.parseObject(conf, Feature.OrderedField);
            return R.ok().put("data", hadoopSiteParamsAsJsonObject.getInnerMap());
        }
        return R.ok().put("data", "none");
    }

    /**
     * 连接测试
     *
     * @param request
     * @return
     */
    @RequestMapping("checkHadoopPlatform")
    @ResponseBody
    public R checkHadoopPlatform(HttpServletRequest request) {
        String platformId = request.getParameter("platformId");
        String configJson = request.getParameter("configJson");
        String tenantId = request.getParameter("tenantId");
        String tenant = request.getParameter("tenant");
        String tenantGroup = request.getParameter("tenantGroup");
        String principal = request.getParameter("principal");
        String keytabName = request.getParameter("keytabName");
        String krb5ConfName = request.getParameter("krb5ConfName");
        String urlHive2 = request.getParameter("urlHive2");
        String principalHive2 = request.getParameter("principalHive2");
        String principalHcatLog = request.getParameter("principalHcatLog");
        String urlHcatLog = request.getParameter("urlHcatLog");
        String isOnlySave = request.getParameter("isOnlySave");
        boolean isOnlySaveBoolean = Boolean.parseBoolean(isOnlySave);
        HadoopPlatform hadoopPlatform=hadoopPlatformService.selectHadoopPlatformById(platformId);
        //用于hadoop使用
        String keytabFullPath = "";
        String krb5confFullPath = "";
        //用于本地文件存储到数据库使用
        String localKeytabPath = "";
        String localKrb5ConfPath = "";
        if (StringUtils.isNotEmpty(keytabName)) {
            keytabFullPath = HadoopConstants.FILE_SYSTEM_ROOT_PATH + keytabBasePath + HadoopConstants.DIR_KEYTAB + HadoopConstants.FILE_SEPARATOR + platformId + HadoopConstants.FILE_SEPARATOR + keytabName;
            krb5confFullPath = HadoopConstants.FILE_SYSTEM_ROOT_PATH + keytabBasePath + HadoopConstants.DIR_KEYTAB + HadoopConstants.FILE_SEPARATOR + platformId + HadoopConstants.FILE_SEPARATOR + krb5ConfName;
            localKeytabPath = keytabFullPath.substring(8);
            localKrb5ConfPath = krb5confFullPath.substring(8);
        }
        if (!isOnlySaveBoolean){
            try {
                boolean b = hadoopService.checkHadoopEnv(platformId, configJson, tenant, tenantGroup, principal, keytabFullPath, localKrb5ConfPath);
                if (b) {
                    //更新集群平台状态的可用性
                    hadoopPlatform.setIsEnable("1");
                    hadoopPlatformService.updateHadoopPlatform(hadoopPlatform);
                } else {
                    return R.ok().put("data","error1");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("发生异常：" + e.getMessage());
                return R.ok().put("data","error1");
            }
        }else{
            try{
                hadoopPlatformService.saveDefaultTenantAndCred(platformId,principalHive2,urlHive2,principalHcatLog,urlHcatLog,tenantId,tenant,tenantGroup,hadoopPlatform,principal,localKeytabPath,localKrb5ConfPath);
            }catch (Exception e){
                e.printStackTrace();
                log.error("发生异常：" + e.getMessage());
                return R.ok().put("data","error2");
            }
        }

        return R.ok().put("data", "success");
    }

    @RequestMapping("checkMysqlServer")
    @ResponseBody
    public R checkMysqlServer(HttpServletRequest request) {
        String oozieMysqlUrl = request.getParameter("oozieMysqlUrl");
        String oozieMysqlUser = request.getParameter("oozieMysqlUser");
        String oozieMysqlPasswd = request.getParameter("oozieMysqlPasswd");
        String platformId = request.getParameter("platformId");
        DataSource ds = new SimpleDataSource(oozieMysqlUrl, oozieMysqlUser, oozieMysqlPasswd);
        Connection conn = null;
        String result;
        try {
            conn=ds.getConnection();
//            SqlExecutor.query(conn,"")
            HadoopPlatform hadoopPlatform = hadoopPlatformService.selectHadoopPlatformById(platformId);
            hadoopPlatform.setOozieMysqlUrl(oozieMysqlUrl);
            hadoopPlatform.setOozieMysqlUser(oozieMysqlUser);
            hadoopPlatform.setOozieMysqlPasswd(oozieMysqlPasswd);
            hadoopPlatformService.updateHadoopPlatform(hadoopPlatform);
            result = "success";
        } catch (SQLException e) {
            result = "fail";
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    result = "fail";
                }
            }
        }
        return R.ok().put("data",result);
    }

}
