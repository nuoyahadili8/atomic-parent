package com.atomic.scheduler.util;

import com.atomic.common.utils.spring.SpringUtils;
import com.atomic.hadoop.oozie.domain.OozieStrategy;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Project:
 * @Description: 执行策略通道工具
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/10/010 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public class StrategyInvokeUtil {
    /**
     * 执行方法
     *
     * @param oozieStrategy 策略通道
     */
    public static void invokeMethod(OozieStrategy oozieStrategy) throws Exception {
        Object bean = SpringUtils.getBean(oozieStrategy.getStrategyClass());
        String methodName = oozieStrategy.getMethodName();
        String methodParams = oozieStrategy.getMaxCount()+"";
        invokeSpringBeanWithHadoopPlatform(bean, methodName, methodParams, String.valueOf(oozieStrategy.getPlatformId()), String.valueOf(oozieStrategy.getId()));
    }

    /**
     * 调用任务方法
     *
     * @param bean         目标对象
     * @param methodName   方法名称
     * @param methodParams 方法参数
     */
    private static void invokeSpringBean(Object bean, String methodName, String methodParams)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (StringUtils.isNotEmpty(methodParams)) {
            Method method = bean.getClass().getDeclaredMethod(methodName, String.class);
            method.invoke(bean, methodParams);
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.invoke(bean);
        }
    }

    private static void invokeSpringBeanWithHadoopPlatform(Object bean, String methodName, String methodParams, String hadoopPlatformId, String oozieStrategyId)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException{
        if (StringUtils.isNotEmpty(methodParams) && StringUtils.isNotEmpty(hadoopPlatformId) && StringUtils.isNotEmpty(oozieStrategyId)) {
            Method method = bean.getClass().getDeclaredMethod(methodName, String.class, String.class, String.class);
            method.invoke(bean, methodParams, hadoopPlatformId, oozieStrategyId);
        }
    }
}
