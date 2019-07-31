package com.atomic.scheduler.dubbo;

import com.atomic.common.constant.ScheduleConstants;
import com.atomic.common.core.text.Convert;
import com.atomic.dubbo.api.IStrategyService;
import com.atomic.hadoop.oozie.domain.OozieStrategy;
import com.atomic.hadoop.oozie.mapper.OozieStrategyMapper;
import com.atomic.scheduler.util.CronUtils;
import com.atomic.scheduler.util.ScheduleTools;
import org.apache.dubbo.config.annotation.Service;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/11/011 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Service(
        version = "1.0.0",
        timeout = 5000
)
public class StrategyServiceImpl implements IStrategyService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private OozieStrategyMapper oozieStrategyMapper;

    @Override
    public int pauseOozieStrategy(Long oozieStrategyId) throws Exception {
        ScheduleTools.pauseJob(scheduler, oozieStrategyId);
        return 1;
    }

    @Override
    public int resumeOozieStrategy(Long oozieStrategyId) throws Exception {
        ScheduleTools.resumeJob(scheduler, oozieStrategyId);
        return 1;
    }

    @Override
    public int deleteOozieStrategy(Long oozieStrategyId) throws Exception {
        ScheduleTools.deleteScheduleJob(scheduler, oozieStrategyId);
        return 1;
    }

    @Override
    public void deleteOozieStrategyByIds(String ids) throws Exception {
        Long[] jobIds = Convert.toLongArray(ids);
        for (Long idd : jobIds) {
            OozieStrategy oozieStrategy = oozieStrategyMapper.selectOozieStrategyById(Integer.parseInt(idd+""));
            deleteOozieStrategy(Long.parseLong(oozieStrategy.getId()+""));
        }
    }

    @Override
    public int changeStatus(Long oozieStrategyId) throws Exception {
        int rows = 0;
        OozieStrategy oozieStrategy = oozieStrategyMapper.selectOozieStrategyById(Integer.parseInt(oozieStrategyId + ""));
        String status = oozieStrategy.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            rows = pauseOozieStrategy(Long.parseLong(oozieStrategy.getId()+""));
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            rows = resumeOozieStrategy(Long.parseLong(oozieStrategy.getId()+""));
        }
        return rows;
    }

    @Override
    public void run(Long oozieStrategyId) throws Exception {

    }

    @Override
    public int insertOozieStrategyCron(Long oozieStrategyId) throws Exception {
        ScheduleTools.run(scheduler, oozieStrategyMapper.selectOozieStrategyById(Integer.parseInt(oozieStrategyId+"")));
        return 1;
    }

    @Override
    public int updateOozieStrategyCron(Long oozieStrategyId) throws Exception {
        OozieStrategy oozieStrategy = oozieStrategyMapper.selectOozieStrategyById(Integer.parseInt(oozieStrategyId + ""));
        ScheduleTools.createScheduleJob(scheduler, oozieStrategy);
        return 1;
    }

    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) throws Exception {
        return CronUtils.isValid(cronExpression);
    }
}
