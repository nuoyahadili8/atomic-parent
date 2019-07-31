package com.atomic.scheduler.util;

import com.atomic.hadoop.oozie.domain.OozieStrategy;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/10/010 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@DisallowConcurrentExecution
public class StrategyQuartzDisallowConcurrentExecution extends AbstractQuartzStrategy {
    @Override
    protected void doExecute(JobExecutionContext context, OozieStrategy oozieStrategy) throws Exception {
        StrategyInvokeUtil.invokeMethod(oozieStrategy);
    }
}
