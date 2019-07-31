package com.atomic.scheduler.task;

import com.atomic.hadoop.common.oozie.utils.OozieClientUtils;
import com.atomic.hadoop.oozie.domain.OozieJob;
import com.atomic.hadoop.oozie.domain.OozieStrategy;
import com.atomic.hadoop.oozie.mapper.OozieJobMapper;
import com.atomic.hadoop.oozie.mapper.OozieStrategyMapper;
import com.atomic.hadoop.oozie.service.IOozieStrategyService;
import com.atomic.hadoop.oozie.service.IOozieTaskToolService;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.mapper.HadoopPlatformMapper;
import com.atomic.hadoop.tenant.service.IHadoopPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/5/29/029 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Component("atomicTask")
public class AtomicTask {

    @Autowired
    private OozieStrategyMapper oozieStrategyMapper;

    @Autowired
    private HadoopPlatformMapper hadoopPlatformMapper;

    @Autowired
    private OozieJobMapper oozieJobMapper;

    @Autowired
    private IOozieTaskToolService oozieTaskToolService;

    @Autowired
    private OozieClientUtils oozieClientUtils;

    public void doWorkDemo1(String param){
        System.out.println("执行有参方法：" + param);
    }

    /**
     *
     * @param maxCount          并发数
     * @param hadoopPlatformId  集群平台ID
     * @param oozieStrategyId   调度策略ID
     */
    public void executHadoopOozieJob(String maxCount,String hadoopPlatformId,String oozieStrategyId){
        OozieStrategy oozieStrategy = oozieStrategyMapper.selectOozieStrategyById(Integer.parseInt(oozieStrategyId));
        System.out.println("执行hadoop Oozie Job任务：" +maxCount + "hadoop Conf配置参数：" + oozieStrategy.getHadoopPlatform().getDefaultFs());

        Map<String, String> oozieConf = oozieTaskToolService.createOozieConf("20190521180905x9xISxuTrOIQHQFfgiE", "atomic", "coordinator");

        for (String key : oozieConf.keySet()) {
            String value = oozieConf.get(key);
            System.out.println("Key = " + key + ", Value = " + value);
        }

        System.out.println("===="+oozieClientUtils.getClass());
        HadoopPlatform hadoopPlatform = hadoopPlatformMapper.selectHadoopPlatformById(hadoopPlatformId);

        OozieJob oozieJob1 = new OozieJob();
        //可用状态
        oozieJob1.setEnable("1");
        oozieJob1.setPlatformId(Integer.parseInt(hadoopPlatformId));

        List<OozieJob> oozieJobs = oozieJobMapper.selectOozieJobList(oozieJob1);
        for (OozieJob oozieJob: oozieJobs){
            System.out.println(oozieJob.getCoordinatorXml());
            System.out.println("=======================================");
        }

    }
}
