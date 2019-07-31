package com.atomic.scheduler.config;

import com.atomic.common.exception.job.TaskException;
import com.atomic.hadoop.oozie.domain.OozieStrategy;
import com.atomic.hadoop.oozie.mapper.OozieStrategyMapper;
import com.atomic.scheduler.util.ScheduleTools;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/21/021 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Configuration
public class StrategyConfig {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private OozieStrategyMapper oozieStrategyMapper;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        List<OozieStrategy> oozieStrategyList = oozieStrategyMapper.selectStrategyAll();
        for (OozieStrategy oozieStrategy : oozieStrategyList) {
            CronTrigger cronTrigger = ScheduleTools.getCronTrigger(scheduler, Long.parseLong(oozieStrategy.getId()+""));
            // 如果不存在，则创建
            if (cronTrigger == null) {
                ScheduleTools.createScheduleJob(scheduler, oozieStrategy);
            } else {
                ScheduleTools.updateScheduleJob(scheduler, oozieStrategy);
            }
        }
    }
}
