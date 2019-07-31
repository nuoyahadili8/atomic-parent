package com.atomic.hadoop.oozie.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.atomic.hadoop.common.config.OozieClientConfig;
import com.atomic.hadoop.common.oozie.model.WorkflowJobVo;
import com.atomic.hadoop.common.oozie.utils.OozieClientUtils;
import com.atomic.hadoop.oozie.domain.OozieJob;
import com.atomic.hadoop.oozie.mapper.OozieJobMapper;
import com.atomic.hadoop.oozie.service.IOozieJobService;
import com.atomic.hadoop.oozie.service.IOozieTaskToolService;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.domain.HadoopTenant;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.WorkflowJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/7/8/008 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Service
public class OozieTaskToolServiceImpl implements IOozieTaskToolService {

    @Autowired
    private OozieJobMapper oozieJobMapper;
    @Autowired
    private OozieClientConfig oozieClientConfig;
    @Autowired
    private OozieClientUtils oozieClientUtils;

    /**
     * 获取oozie任务所需要的Properties
     *
     * @param jobId     Oozie Job ID
     * @param oozieUser 租户名
     * @param type      任务类型:workflow coordinator bundle
     * @return Properties
     */
    @Override
    public Map<String,String> createOozieConf(String jobId, String oozieUser, String type) {
        Map<String,String> kvs = new HashMap(32);
        OozieJob oozieJob = oozieJobMapper.selectOozieJobById(jobId);
        HadoopPlatform hadoopPlatform = oozieJob.getHadoopPlatform();
        Path hdfsRootDir = new Path(oozieClientConfig.getOozieAppRootHdfs() + oozieUser);
        Path hdfsAppDir = new Path(hdfsRootDir, type);
        Path hdfsRunDir = new Path(hdfsAppDir, oozieJob.getNameEn());
        HadoopTenant hadoopTenant = oozieJob.getHadoopTenant();

        kvs.put("mapred.mapper.new-api", "true");
        kvs.put("mapred.reducer.new-api", "true");
        kvs.put("oozie.use.system.libpath", "true");
        kvs.put("oozie.libpath", "/user/atomic/conf/lib");
        kvs.put("nameNode", hadoopPlatform.getDefaultFs());
        kvs.put("jobTracker", hadoopPlatform.getJobTracker());
        kvs.put("user.name",hadoopTenant.getTenant());
        kvs.put("oozie.server",hadoopPlatform.getOozieUrl());
        kvs.put("mapreduce.job.user.name",hadoopTenant.getTenant());

        if ("workflow".equals(type)) {
            kvs.put(OozieClient.APP_PATH, hdfsRunDir.toString());
        } else if ("coordinator".equals(type)) {
            kvs.put(OozieClient.COORDINATOR_APP_PATH, hdfsRunDir.toString());
        } else if ("bundle".equals(type)) {
            kvs.put(OozieClient.BUNDLE_APP_PATH, hdfsRunDir.toString());
        }
        String queuename = hadoopTenant.getYarnQueue();
        if (!StringUtils.isBlank(queuename)) {
            kvs.put("queueName", queuename);
        }
        return kvs;
    }

    @Override
    public int runJob(String jobId, String startTime,String type) {
        OozieJob oozieJob = oozieJobMapper.selectOozieJobById(jobId);
        String oozieUser=oozieJob.getHadoopTenant().getTenant();
        Map<String,String> kvs = createOozieConf(jobId,oozieUser,type);
        kvs.put("starttime",oozieClientUtils.getStartTimeByCycle(startTime,oozieJob.getCycle()));
        kvs.put("endtime",oozieClientUtils.getEndTimeByCycle(startTime,oozieJob.getCycle()));
        for (Map.Entry<String, String> entry : kvs.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("key=" + key + " value=" + value);
        }
        Map<String, Object> messageMap = oozieClientUtils.runCoordinator(oozieUser, oozieJob.getNameEn(), kvs);
        return 1;
    }

    @Override
    public JSONObject getJobRunFlowJson(String jobId) {
        WorkflowJobVo jobVo = null;
        String flowJson = "{}";
        WorkflowJob workflowJob = oozieClientUtils.getWorkflowJobInfo(jobId);

        return null;
    }

    public static void main(String[] args) {
        String startTime="2019-07-14";
        Date date = DateUtil.parse(startTime);
        String format=DateUtil.format(date,"yyyy-MM-dd HH:mm:ss");
        System.out.println(format);
    }
}
