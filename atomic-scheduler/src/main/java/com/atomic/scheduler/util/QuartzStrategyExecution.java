package com.atomic.scheduler.util;

import com.atomic.hadoop.oozie.domain.OozieStrategy;
import org.quartz.JobExecutionContext;

/**
 * @Project:
 * @Description: 定时策略通道处理（允许并发执行）
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/10/010 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public class QuartzStrategyExecution extends AbstractQuartzStrategy {
    @Override
    protected void doExecute(JobExecutionContext context, OozieStrategy oozieStrategy) throws Exception {
        StrategyInvokeUtil.invokeMethod(oozieStrategy);
    }
}
