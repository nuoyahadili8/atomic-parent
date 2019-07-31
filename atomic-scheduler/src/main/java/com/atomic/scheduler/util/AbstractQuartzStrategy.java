package com.atomic.scheduler.util;

import com.atomic.common.constant.Constants;
import com.atomic.common.constant.ScheduleConstants;
import com.atomic.common.utils.ExceptionUtil;
import com.atomic.common.utils.bean.BeanUtils;
import com.atomic.common.utils.spring.SpringUtils;
import com.atomic.hadoop.oozie.domain.OozieStrategy;
import com.atomic.scheduler.domain.SysJobLog;
import com.atomic.scheduler.service.ISysJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/11/011 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Slf4j
public abstract class AbstractQuartzStrategy implements Job {

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        OozieStrategy oozieStrategy = new OozieStrategy();
        BeanUtils.copyBeanProp(oozieStrategy, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try {
            before(context, oozieStrategy);
            if (oozieStrategy != null) {
                doExecute(context, oozieStrategy);
            }
            after(context, oozieStrategy, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, oozieStrategy, e);
        }
    }

    /**
     * 执行前
     */
    protected void before(JobExecutionContext context, OozieStrategy oozieStrategy) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     */
    protected void after(JobExecutionContext context, OozieStrategy oozieStrategy, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(oozieStrategy.getStrategyClass());
        sysJobLog.setJobGroup(oozieStrategy.getStrategyName());
        sysJobLog.setMethodName(oozieStrategy.getMethodName());
        sysJobLog.setMethodParams(oozieStrategy.getMaxCount()+"");
        sysJobLog.setStartTime(startTime);
        sysJobLog.setEndTime(new Date());
        long runMs = sysJobLog.getEndTime().getTime() - sysJobLog.getStartTime().getTime();
        sysJobLog.setJobMessage(sysJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            sysJobLog.setStatus(Constants.FAIL);
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
        } else {
            sysJobLog.setStatus(Constants.SUCCESS);
        }

        // 写入数据库当中
        SpringUtils.getBean(ISysJobLogService.class).addJobLog(sysJobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param oozieStrategy  系统策略通道
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, OozieStrategy oozieStrategy) throws Exception;
}
