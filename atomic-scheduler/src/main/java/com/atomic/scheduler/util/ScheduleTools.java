package com.atomic.scheduler.util;

import com.atomic.common.constant.ScheduleConstants;
import com.atomic.common.exception.job.TaskException;
import com.atomic.hadoop.oozie.domain.OozieStrategy;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @Project:
 * @Description: 定时策略工具类
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/10/010 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Slf4j
public class ScheduleTools {

    /**
     * 得到quartz任务类
     *
     * @param oozieStrategy 策略通道
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(OozieStrategy oozieStrategy) {
        boolean isConcurrent = "0".equals(oozieStrategy.getConcurrent());
        return isConcurrent ? QuartzStrategyExecution.class : StrategyQuartzDisallowConcurrentExecution.class;
    }

    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId);
    }

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            log.error("getCronTrigger 异常：", e);
        }
        return null;
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, OozieStrategy oozieStrategy) throws SchedulerException, TaskException {
        Class<? extends Job> jobClass = getQuartzJobClass(oozieStrategy);
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(Long.parseLong(oozieStrategy.getId()+""))).build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(oozieStrategy.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(oozieStrategy, cronScheduleBuilder);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(Long.parseLong(oozieStrategy.getId()+"")))
                .withSchedule(cronScheduleBuilder).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, oozieStrategy);

        scheduler.scheduleJob(jobDetail, trigger);

        // 暂停任务
        if (oozieStrategy.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
            pauseJob(scheduler, Long.parseLong(oozieStrategy.getId()+""));
        }
    }

    /**
     * 更新定时任务
     */
    public static void updateScheduleJob(Scheduler scheduler, OozieStrategy oozieStrategy) throws SchedulerException, TaskException {
        JobKey jobKey = getJobKey(Long.parseLong(oozieStrategy.getId()+""));

        // 判断是否存在
        if (scheduler.checkExists(jobKey)) {
            // 先移除，然后做更新操作
            scheduler.deleteJob(jobKey);
        }

        createScheduleJob(scheduler, oozieStrategy);

        // 暂停任务
        if (oozieStrategy.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
            pauseJob(scheduler, Long.parseLong(oozieStrategy.getId()+""));
        }
    }

    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, OozieStrategy oozieStrategy) throws SchedulerException {
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, oozieStrategy);

        scheduler.triggerJob(getJobKey(Long.parseLong(oozieStrategy.getId()+"")), dataMap);
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Long jobId) throws SchedulerException {
        scheduler.pauseJob(getJobKey(jobId));
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, Long jobId) throws SchedulerException {
        scheduler.resumeJob(getJobKey(jobId));
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) throws SchedulerException {
        scheduler.deleteJob(getJobKey(jobId));
    }

    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(OozieStrategy oozieStrategy, CronScheduleBuilder cb)
            throws TaskException {
        switch (oozieStrategy.getMisfirePolicy()) {
            case ScheduleConstants.MISFIRE_DEFAULT:
                return cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new TaskException("The task misfire policy '" + oozieStrategy.getMisfirePolicy()
                        + "' cannot be used in cron schedule tasks", TaskException.Code.CONFIG_ERROR);
        }
    }
}
