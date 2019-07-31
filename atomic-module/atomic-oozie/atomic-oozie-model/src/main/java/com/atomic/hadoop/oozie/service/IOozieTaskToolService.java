package com.atomic.hadoop.oozie.service;

import com.alibaba.fastjson.JSONObject;

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
public interface IOozieTaskToolService {

    Map<String,String> createOozieConf(String jobId, String oozieUser, String type);

    int runJob(String jobId,String startTime,String type);

    JSONObject getJobRunFlowJson(String jobId);
}
